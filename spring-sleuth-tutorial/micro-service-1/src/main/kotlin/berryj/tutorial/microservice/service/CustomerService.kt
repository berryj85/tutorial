package berryj.tutorial.microservice.service

import berryj.tutorial.microservice.client.MicroService2Client
import berryj.tutorial.microservice.response.CustomerResponse
import berryj.tutorial.microservice.exception.DataNotFoundException
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomerService {

    @Autowired
    private lateinit var client: MicroService2Client

    companion object {
        private val log = LogManager.getLogger(CustomerService::class.java)
    }

    fun getCustomerById(id: String): Mono<CustomerResponse> {
        return client.getCustomerById(id)
                .doFirst { log.info("Start CustomerService.getCustomerById($id)") }
                .doOnSuccess { log.info("End CustomerService.getCustomerById()") }
    }
}