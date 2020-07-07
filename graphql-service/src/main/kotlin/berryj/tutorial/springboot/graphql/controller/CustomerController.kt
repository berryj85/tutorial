package berryj.tutorial.springboot.graphql.controller

import berryj.tutorial.springboot.graphql.entity.Customer
import berryj.tutorial.springboot.graphql.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customer", produces = [MediaType.APPLICATION_JSON_VALUE])
class CustomerController {
    @Autowired
    private lateinit var customerService: CustomerService

    @GetMapping
    fun getCustomer(): List<Customer> {
        return customerService.getCustomerList()
    }

    @GetMapping("/{customer_id}")
    fun getCustomerById(@PathVariable("customer_id") id: String): Customer {
        return customerService.getCustomerById(id)
    }

}