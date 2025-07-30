package ecommerce.repository

import ecommerce.entity.MemberEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class MemberRepositoryJpaTest(
    @Autowired
    private val repo: MemberRepositoryJpa,
) {
    @Test
    fun `save persists and assigns id`() {
        val sampleMemberEntity =
            MemberEntity(
                email = "alice@example.com",
                password = "secret",
                role = "USER",
                name = "Alice",
            )
        val saved = repo.save(sampleMemberEntity)
        assertThat(saved.id).isNotNull()
    }

    @Test
    fun `findByEmail returns member when email exists`() {
        val sampleMemberEntity =
            MemberEntity(
                email = "alice@example.com",
                password = "secret",
                role = "USER",
                name = "Alice",
            )
        repo.save(sampleMemberEntity)

        val found = repo.findByEmail("alice@example.com")

        assertThat(found).isNotNull
        assertThat(found!!.email).isEqualTo("alice@example.com")
    }

    @Test
    fun `findByEmail returns null when email does not exist`() {
        assertThat(repo.findByEmail("unknown@example.com")).isNull()
    }

    @Test
    fun `existsByEmail returns true when member with that email exists`() {
        val sampleMemberEntity =
            MemberEntity(
                email = "alice@example.com",
                password = "secret",
                role = "USER",
                name = "Alice",
            )
        repo.save(sampleMemberEntity)

        assertThat(repo.existsByEmail("alice@example.com")).isTrue()
    }

    @Test
    fun `existsByEmail returns false when member with that email does not exist`() {
        assertThat(repo.existsByEmail("alice@example.com")).isFalse()
    }
}
