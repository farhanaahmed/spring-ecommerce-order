package ecommerce.model

data class CartItem(
    val id: Long,
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
)
