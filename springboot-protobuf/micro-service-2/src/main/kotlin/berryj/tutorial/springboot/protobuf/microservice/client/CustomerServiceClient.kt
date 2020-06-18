package berryj.tutorial.springboot.protobuf.microservice.client

import berryj.tutorial.springboot.protobuf.microservice.message.Customer
import berryj.tutorial.springboot.protobuf.microservice.message.CustomerList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class CustomerServiceClient {
    @Autowired
    private lateinit var restTemplate: RestTemplate
    val headers = HttpHeaders().apply {
        this.set("accept","application/x-protobuf")
    }

    fun getCustomerById(customerId: String): Customer? {
        return restTemplate.exchange("http://localhost:8080/customer/$customerId",
                HttpMethod.GET,
                HttpEntity(null, headers),
                Customer::class.java).body

    }

    fun getAllCustomer(): CustomerList? {

        return restTemplate.exchange("http://localhost:8080/customer",
                HttpMethod.GET,
                HttpEntity(null, headers),
                CustomerList::class.java).body
    }

    fun insertCustomer(customer: Customer): Customer? {
        return restTemplate.exchange("http://localhost:8080/customer",
                HttpMethod.POST,
                HttpEntity(customer, headers),
                Customer::class.java).body
    }
}