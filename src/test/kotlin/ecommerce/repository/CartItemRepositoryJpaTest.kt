package ecommerce.repository

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
internal class CartItemRepositoryJpaTest
    @Autowired
    constructor(
        private val repo: CartItemRepositoryJpa,
    ) {
        @Autowired
        private lateinit var em: TestEntityManager

        @AfterEach
        fun clean() {
            em.flush()
            em.clear()
        }

//        @Test
//        fun `persist a valid cart item`() {
//            val product =
//                ProductEntity(
//                    name = "Test Product",
//                    price = 9.99,
//                    imageUrl = "https://example.com/img.jpg",
//                ).let(em::persist)
//
//            val cartItem = CartItemEntity(product = product, quantity = 3)
//            //em.persistAndFlush(cartItem)
//
//            assertThat(cartItem.id).isNotNull()
//            assertThat(cartItem.quantity).isEqualTo(3)
//        }
//
//        @Test
//        fun `delete by id`() {
//            val product =
//                ProductEntity(
//                    name = "Test Product",
//                    price = 9.99,
//                    imageUrl = "https://example.com/img.jpg",
//                ).let(em::persist)
//            val item = repo.save(CartItemEntity(product = product, quantity = 1))
//            assertThat(repo.count()).isOne()
//
//            repo.deleteById(item.id!!)
//            assertThat(repo.findById(item.id!!)).isEmpty
//        }
//
//        @Test
//        fun `delete entity instance`() {
//            val product =
//                ProductEntity(
//                    name = "Test Product",
//                    price = 9.99,
//                    imageUrl = "https://example.com/img.jpg",
//                ).let(em::persist)
//            val item = repo.save(CartItemEntity(product = product, quantity = 1))
//            repo.delete(item)
//            assertThat(repo.count()).isZero()
//        }
    }
