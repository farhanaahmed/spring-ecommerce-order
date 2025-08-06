package ecommerce.repository

import ecommerce.dto.TopProductStatResponse
import ecommerce.entity.CartEntity
import ecommerce.entity.CartItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CartItemRepositoryJpa : JpaRepository<CartItemEntity, Long> {
    fun findByCartAndProductId(
        cart: CartEntity,
        productId: Long,
    ): CartItemEntity?

    fun findAllByQuantity(
        quantity: Int,
        pageable: Pageable,
    ): Page<CartItemEntity>

    override fun findAll(pageable: Pageable): Page<CartItemEntity>

    fun findAllByCartId(
        cartId: Long,
        pageable: Pageable,
    ): Page<CartItemEntity>

    @Query(
        value = """
            SELECT 
                p.product_name AS productName,
                COUNT(*) AS timesAdded,
                MAX(ci.created_at) AS mostRecentAddedTime
            FROM cart_item ci
            JOIN product p ON ci.product_id = p.id
            WHERE ci.created_at >= CURRENT_DATE - INTERVAL '30 days'
            GROUP BY ci.product_id, p.product_name
            ORDER BY timesAdded DESC, mostRecentAddedTime DESC
            LIMIT 5
        """,
        nativeQuery = true,
    )
    fun findTop5ProductsInLast30Days(): List<TopProductStatResponse>
}
