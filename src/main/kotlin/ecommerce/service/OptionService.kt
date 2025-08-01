package ecommerce.service

import ecommerce.dto.OptionCreateDto
import ecommerce.entity.OptionEntity
import ecommerce.entity.ProductEntity
import ecommerce.repository.OptionRepositoryJpa
import ecommerce.repository.ProductRepositoryJpa
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductOptionService(
    private val productRepositoryJpa: ProductRepositoryJpa,
    private val optionRepositoryJpa: OptionRepositoryJpa,
) {
    fun createProductWithOptions(
        name: String,
        price: Double,
        imageUrl: String,
        optionDtos: List<OptionCreateDto>,
    ): ProductEntity {
        val options = optionDtos.map { OptionEntity(name = it.name, quantity = it.quantity) }
        val product = ProductEntity(name = name, price = price, imageUrl = imageUrl, options = options.toMutableSet())
        return productRepositoryJpa.save(product)
    }

    fun decreaseOptionQuantity(
        optionId: Long,
        amount: Long,
    ) {
        val option =
            optionRepositoryJpa.findByIdOrNull(optionId)
                ?: throw NoSuchElementException("Option not found id=$optionId")
        option.decreaseQuantity(amount)
    }
}
