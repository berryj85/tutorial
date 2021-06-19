package berryj.demo.webflux.repository

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface CustomerRepository : ReactiveCrudRepository<CustomerEntity, Int> {

    fun getCustomerById(id: Int): Mono<CustomerEntity>
}
@Table("customer")
data class CustomerEntity(
    @Id
    val id: Int? = null,
    @Column("first_name")
    val firstName: String? = null,
    @Column("last_name")
    val lastName: String? = null
)
