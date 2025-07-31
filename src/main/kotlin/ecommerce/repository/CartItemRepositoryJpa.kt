package ecommerce.repository

import ecommerce.entity.CartItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepositoryJpa : JpaRepository<CartItemEntity, Long> {
    fun findByProductId(productId: Long): List<CartItemEntity>
//
//    fun existsByMemberIdAndProductId(
//        memberId: Long,
//        productId: Long,
//    ): Boolean
//
//    fun findByMemberIdAndProductId(
//        memberId: Long,
//        productId: Long,
//    ): CartItemEntity?
//
//    @Transactional
//    fun deleteByMemberIdAndProductId(
//        memberId: Long,
//        productId: Long,
//    ): Int
//
//    @Transactional
//    fun deleteByMemberId(memberId: Long): Int
}
