package berryj.tutorial.microservice.controller

import berryj.tutorial.microservice.domain.response.CustomerResponse
import berryj.tutorial.microservice.service.CustomerService
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/customers")
class CustomerController {

    companion object {
        private val log = LogManager.getLogger(CustomerController::class.java)
    }

    @Autowired
    private lateinit var customerService: CustomerService

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCustomerById(@PathVariable("id") id: String): Mono<ResponseEntity<CustomerResponse>> {
        return customerService.getCustomerById(id).map { ResponseEntity.ok(it) }
                .doFirst { log.info("Payload CustomerController.getCustomerById($id)") }
                .doOnSuccess { log.info("Response success ${it.body}") }
                .doOnError { log.error(it.message) }
    }
}