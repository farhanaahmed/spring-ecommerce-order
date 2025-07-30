package ecommerce.repository

import ecommerce.entity.CartEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class CartRepositoryJpaTest
    @Autowired
    constructor(
        private val repo: CartRepositoryJpa,
    ) {
        @Test
        fun `save persists and assigns id`() {
            val saved = repo.save(sampleCart(memberId = 1L, productId = 10L))
            assertThat(saved.id).isNotNull()
        }

        @Test
        fun `findByMemberId returns all rows for that member`() {
            repo.save(sampleCart(memberId = 1L, productId = 10L))
            repo.save(sampleCart(memberId = 1L, productId = 11L))
            repo.save(sampleCart(memberId = 2L, productId = 10L))

            val carts = repo.findByMemberId(1L)
            assertThat(carts).hasSize(2)
            assertThat(carts).extracting<Long> { it.productId }
                .containsExactlyInAnyOrder(10L, 11L)
        }

        @Test
        fun `findByMemberIdAndProductId returns single row when exists`() {
            repo.save(sampleCart(memberId = 1L, productId = 10L, quantity = 3))

            val cart = repo.findByMemberIdAndProductId(memberId = 1L, productId = 10L)

            assertThat(cart).isNotNull
            assertThat(cart!!.quantity).isEqualTo(3)
        }

        @Test
        fun `findByMemberIdAndProductId returns null when not found`() {
            assertThat(repo.findByMemberIdAndProductId(1L, 99L)).isNull()
        }

        @Test
        fun `existsByMemberIdAndProductId returns true when row exists`() {
            repo.save(sampleCart(memberId = 1L, productId = 10L))
            assertThat(repo.existsByMemberIdAndProductId(1L, 10L)).isTrue()
        }

        @Test
        fun `existsByMemberIdAndProductId returns false when row does not exist`() {
            assertThat(repo.existsByMemberIdAndProductId(1L, 99L)).isFalse()
        }

        @Test
        fun `deleteByMemberIdAndProductId removes exactly that row`() {
            repo.save(sampleCart(memberId = 1L, productId = 10L))
            repo.save(sampleCart(memberId = 1L, productId = 11L))

            val affected = repo.deleteByMemberIdAndProductId(memberId = 1L, productId = 10L)

            assertThat(affected).isOne()
            assertThat(repo.findByMemberId(1L)).hasSize(1)
            assertThat(repo.findByMemberIdAndProductId(1L, 10L)).isNull()
        }

        @Test
        fun `deleteByMemberId removes all rows for that member`() {
            repo.save(sampleCart(memberId = 1L, productId = 10L))
            repo.save(sampleCart(memberId = 1L, productId = 11L))
            repo.save(sampleCart(memberId = 2L, productId = 10L))

            val affected = repo.deleteByMemberId(1L)

            assertThat(affected).isEqualTo(2)
            assertThat(repo.findByMemberId(1L)).isEmpty()
        }

        private fun sampleCart(
            memberId: Long = 1L,
            productId: Long = 10L,
            quantity: Int = 1,
        ) = CartEntity(
            memberId = memberId,
            productId = productId,
            quantity = quantity,
        )
    }
