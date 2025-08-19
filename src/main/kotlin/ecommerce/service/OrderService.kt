package ecommerce.service

import ecommerce.dto.PaymentRequest
import ecommerce.dto.PlaceOrderRequest
import ecommerce.dto.PlaceOrderResponse
import ecommerce.dto.StripeIntentResponse
import ecommerce.entity.Member
import ecommerce.entity.Order
import ecommerce.entity.OrderItem
import ecommerce.entity.Payment
import ecommerce.enums.OrderStatus
import ecommerce.enums.PaymentMethod
import ecommerce.infrastructure.StripeClient
import ecommerce.repository.CartItemRepositoryJpa
import ecommerce.repository.CartRepositoryJpa
import ecommerce.repository.OptionRepositoryJpa
import ecommerce.repository.OrderItemRepositoryJpa
import ecommerce.repository.OrderRepositoryJpa
import ecommerce.repository.PaymentRepositoryJpa
import ecommerce.util.MoneyUtil
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val optionRepository: OptionRepositoryJpa,
    private val orderRepository: OrderRepositoryJpa,
    private val orderItemRepository: OrderItemRepositoryJpa,
    private val paymentRepository: PaymentRepositoryJpa,
    private val cartItemRepository: CartItemRepositoryJpa,
    private val cartRepository: CartRepositoryJpa,
    private val stripeClient: StripeClient,
) {
    fun place(
        member: Member,
        req: PlaceOrderRequest,
    ): PlaceOrderResponse {
        val option =
            optionRepository.findById(req.optionId)
                .orElseThrow { IllegalArgumentException("Option not found: ${req.optionId}") }
        require(req.quantity > 0) { "Quantity must be positive" }
        require(option.quantity >= req.quantity) {
            "Insufficient stock. Available=${option.quantity}, requested=${req.quantity}"
        }

        val amountMinor = MoneyUtil.toMinorUnits(option.product.price, req.quantity)
        val stripeRes =
            stripeClient.createAndConfirmPayment(
                PaymentRequest(amountMinor, req.currency, req.paymentMethod),
            )
        if (stripeRes.status != "succeeded") {
            throw IllegalArgumentException("Payment not approved (status=${stripeRes.status}).")
        }

        val orderId =
            persistAfterStripeSuccess(
                member,
                option.id!!,
                req.quantity,
                amountMinor,
                req.currency,
                stripeRes,
                req.paymentMethod,
            )
        return PlaceOrderResponse(orderId, stripeRes.status, "Order placed and paid successfully.")
    }

    @Transactional
    fun persistAfterStripeSuccess(
        member: Member,
        optionId: Long,
        requestedQty: Long,
        amountMinor: Long,
        currency: String,
        stripeRes: StripeIntentResponse,
        paymentMethod: PaymentMethod,
    ): Long {
        val option =
            optionRepository.findById(optionId)
                .orElseThrow { IllegalArgumentException("Option not found during persist: $optionId") }
        require(option.quantity >= requestedQty) { "Insufficient stock during persist." }

        val order =
            orderRepository.save(
                Order(
                    memberId = member.id ?: error("Member must be persisted (id is null)"),
                    status = OrderStatus.PAID,
                    orderDateTime = LocalDateTime.now(),
                ),
            )

        orderItemRepository.save(
            OrderItem(
                order = order,
                productOption = option,
                quantity = requestedQty.toInt(),
                price = option.product.price,
                productName = option.product.name,
                optionName = option.name,
                productImageUrl = option.product.imageUrl,
            ),
        )

        paymentRepository.save(
            Payment(
                order = order,
                amount = amountMinor,
                currency = currency,
                status = "PAID",
                stripeSessionId = stripeRes.id,
                paymentMethod = paymentMethod,
            ),
        )

        option.decreaseQuantity(requestedQty)
        optionRepository.save(option)

        cartRepository.findByMemberId(member.id!!)?.let { cart ->
            cartItemRepository.findByCartIdAndProductOptionId(cart.id!!, option.id!!)?.let {
                cartItemRepository.delete(it)
            }
        }

        return order.id!!
    }
}
