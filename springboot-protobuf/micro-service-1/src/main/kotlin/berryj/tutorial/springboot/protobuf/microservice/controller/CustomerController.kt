package berryj.tutorial.springboot.protobuf.microservice.controller

import berryj.tutorial.springboot.protobuf.microservice.message.Customer
import berryj.tutorial.springboot.protobuf.microservice.message.CustomerList
import berryj.tutorial.springboot.protobuf.microservice.service.CustomerService
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customer", produces = ["application/x-protobuf", MediaType.APPLICATION_JSON_VALUE])
class CustomerController {

    @Autowired
    private lateinit var customerService: CustomerService

    companion object {
        private val log = LogManager.getLogger(CustomerController::class.java)
    }

    @GetMapping()
    fun getAllCustomer(): CustomerList {
        log.info("Payload getAllCustomer")
        return customerService.getAll().map {
            Customer.newBuilder().apply {
                this.customerId = it?.customerId?.toInt() ?: 0
                this.firstname = it?.firstname ?: ""
                this.lastname = it?.lastname ?: ""
                this.addAllPhoneNumbers(it?.phoneNumbers ?: listOf())
                this.activeStatus = it?.activeStatus ?: true
            }.build()
        }.let {
            CustomerList.newBuilder().addAllCustomers(it).build()
        }.also {
            log.info("End getAllCustomer {$it}")
        }

    }

    @PostMapping
    fun insertCustomer(@RequestBody customer: Customer): Customer {
        log.info("Payload insertCustomer{$customer}")
        return customerService.insertCustomer(customer).let {
            Customer.newBuilder().apply {
                this.customerId = it?.customerId?.toInt() ?: 0
                this.firstname = it?.firstname ?: ""
                this.lastname = it?.lastname ?: ""
                this.addAllPhoneNumbers(it?.phoneNumbers ?: listOf())
                this.activeStatus = it?.activeStatus ?: true

            }.build()
        }
    }

    @GetMapping("/{customer_id}")
    fun getCustomerById(@PathVariable("customer_id") customerId: String): Customer {
        log.info("Payload getCustomerById($customerId)")
        return customerService.getByCustomerId(customerId).let {
            Customer.newBuilder().apply {
                this.customerId = it?.customerId?.toInt() ?: 0
                this.firstname = it?.firstname ?: ""
                this.lastname = it?.lastname ?: ""
                this.addAllPhoneNumbers(it?.phoneNumbers ?: listOf())
                this.activeStatus = it?.activeStatus ?: true

            }.build()
        }
    }
}