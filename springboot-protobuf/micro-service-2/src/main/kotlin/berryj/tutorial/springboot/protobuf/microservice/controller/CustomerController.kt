package berryj.tutorial.springboot.protobuf.microservice.controller

import berryj.tutorial.springboot.protobuf.microservice.client.CustomerServiceClient
import berryj.tutorial.springboot.protobuf.microservice.message.Customer
import berryj.tutorial.springboot.protobuf.microservice.message.CustomerList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customer", produces = ["application/x-protobuf", MediaType.APPLICATION_JSON_VALUE])
class CustomerController {
    @Autowired
    private lateinit var customerServiceClient: CustomerServiceClient

    @GetMapping("/{customer_id}")
    fun getCustomerById(@PathVariable("customer_id") customerId: String): Customer? {
        return customerServiceClient.getCustomerById(customerId)
    }

    @GetMapping
    fun getAllCustomer(): CustomerList? {
        return customerServiceClient.getAllCustomer()
    }

    @PostMapping
    fun insertCustomer(@RequestBody customer: Customer): Customer? {
        return customerServiceClient.insertCustomer(customer)
    }
}