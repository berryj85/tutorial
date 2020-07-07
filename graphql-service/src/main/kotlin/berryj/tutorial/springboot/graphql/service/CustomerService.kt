package berryj.tutorial.springboot.graphql.service

import berryj.tutorial.springboot.graphql.entity.Customer
import org.springframework.stereotype.Service

@Service
class CustomerService {
    companion object {
        private val mockCustomerData = listOf<Customer>(
                Customer("1", "Mr.Spring boot", "xample@mail.com", listOf("+66xxxxxxxx1")),
                Customer("2", "Ms.Spring cloud", "xampleCloud@mail.com", listOf("+66xxxxxxxx2", "+66xxxxxxxx3", "Tokyo,Japan")),
                Customer("3", "Mr.GraphQL Java", "xampleGraphQL@mail.com", listOf("+66xxxxxxxx4", "Bangkok,Thailand")),
                Customer("4", "Ms.Kibana Elastic", "xampleKibana@mail.com", listOf("+66xxxxxxxx5", "Bangkok,Thailand")),
                Customer("5", "Ms.Kibana LogStash", "xampleLogStash@mail.com", listOf("+66xxxxxxxx6", "Osaka,Japan"))
        )
    }

    fun getCustomerList(): List<Customer> {
        return mockCustomerData
    }

    fun getCustomerById(id: String): Customer {
        return mockCustomerData.find { it.customerId == id } ?: throw Exception("Data not found.")
    }
}
