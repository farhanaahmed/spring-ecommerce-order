package ecommerce.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Entity
@Table(
    name = "option",
    uniqueConstraints = [UniqueConstraint(columnNames = ["product_id", "name"])],
)
class OptionEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @field:Size(max = 50)
    @field:Pattern(
        regexp = "^[\\p{L}\\p{N}\\s()\\[\\]+\\-&/_]*\$",
        message = "Invalid characters in option name",
    )
    @Column(nullable = false)
    var name: String,
    @field:Min(1) @field:Max(99_999_999)
    @Column(nullable = false)
    var quantity: Long,
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    lateinit var product: ProductEntity
        internal set

    fun decreaseQuantity(amount: Long) {
        require(amount > 0) { "Amount must be positive" }
        check(quantity >= amount) { "Insufficient stock for option id=$id" }
        quantity -= amount
    }
}
