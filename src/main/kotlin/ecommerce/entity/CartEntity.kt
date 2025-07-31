package ecommerce.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "cart")
class CartEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    val memberEntity: MemberEntity? = null,
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id", nullable = false)
    var CartItemEntities: MutableList<CartItemEntity>? = mutableListOf(),
)
