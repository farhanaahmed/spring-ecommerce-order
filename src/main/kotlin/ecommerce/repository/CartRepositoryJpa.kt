package ecommerce.repository

import ecommerce.entity.CartEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface CartRepositoryJpa : JpaRepository<CartEntity, Long> {
    fun findByMemberId(memberId: Long): List<CartEntity>

    fun existsByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ): Boolean

    fun findByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ): CartEntity?

    @Transactional
    fun deleteByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ): Int

    @Transactional
    fun deleteByMemberId(memberId: Long): Int
}
