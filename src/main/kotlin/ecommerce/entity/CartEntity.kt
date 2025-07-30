package ecommerce.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Positive

@Entity
@Table(name = "cart")
class CartEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val memberId: Long,
    @Column(nullable = false)
    val productId: Long,
    @field:Positive
    @Column(nullable = false)
    val quantity: Int,
)
