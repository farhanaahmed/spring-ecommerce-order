package ecommerce.repository

import ecommerce.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepositoryJpa : JpaRepository<ProductEntity, Long> {
    fun findByName(name: String): ProductEntity?

    fun existsByName(name: String): Boolean
}
