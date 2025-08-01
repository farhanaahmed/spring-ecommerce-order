package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.entity.ProductEntity
import ecommerce.model.Product
import ecommerce.repository.ProductRepositoryJpa
import ecommerce.repository.ProductStore
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ProductService(
    @Qualifier("jdbcProductStore") private val productRepository: ProductStore,
    private val productRepositoryJpa: ProductRepositoryJpa,
) {
    fun createProduct(productRequest: ProductRequest): Product {
        if (productRepository.existsByName(productRequest.name)) {
            throw IllegalArgumentException("Product with name '${productRequest.name}' already exists.")
        }
        val product =
            Product(
                name = productRequest.name,
                price = productRequest.price,
                imageUrl = productRequest.imageUrl,
            )
        return productRepository.save(product)
    }

    fun getAllProducts(
        page: Int,
        size: Int,
        sortBy: String = "name",
        direction: Sort.Direction = Sort.Direction.ASC,
    ): Page<ProductEntity> {
        val pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))
        return productRepositoryJpa.findAll(pageable)
    }

    fun getProductsByPrice(
        price: Double,
        page: Int,
        size: Int,
    ): Page<ProductEntity> {
        val pageable = PageRequest.of(page, size)
        return productRepositoryJpa.findAllByPrice(price, pageable)
    }

    fun getPriceSlice(
        price: Double,
        page: Int,
        size: Int,
    ): Slice<ProductEntity> {
        val pageable = PageRequest.of(page, size)
        return productRepositoryJpa.findByPriceGreaterThan(price, pageable)
    }
}
