package ecommerce.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive

@Entity
@Table(name = "product")
class ProductEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @field:NotBlank
    @Column(nullable = false, unique = true)
    var name: String,
    @field:Positive
    @Column(nullable = false)
    val price: Double,
    @field:Pattern(
        regexp = "https?://.*",
        message = "Image URL must start with http:// or https://",
    )
    @Column(nullable = false, name = "image_url")
    val imageUrl: String,
    @OneToMany(
        // mappedBy = "product",
        cascade = [CascadeType.MERGE, CascadeType.PERSIST],
        orphanRemoval = true,
    )
    @Column(nullable = false, name = "option")
    val options: MutableList<OptionEntity> = mutableListOf(),
) {
    init {
        require(options.isNotEmpty()) { "A product must have at least one option" }
    }
}
