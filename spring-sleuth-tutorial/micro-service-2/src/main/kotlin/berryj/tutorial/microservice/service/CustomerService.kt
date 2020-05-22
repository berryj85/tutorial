package berryj.tutorial.microservice.service

import berryj.tutorial.microservice.domain.response.CustomerResponse
import berryj.tutorial.microservice.exception.DataNotFoundException
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class CustomerService {
    companion object {
        private val log = LogManager.getLogger(CustomerService::class.java)
    }

    fun getCustomerById(id: String): Mono<CustomerResponse> {
        return Mono.create<CustomerResponse> { sink ->
            if (id == "100")
                sink.error(DataNotFoundException())
            Thread.sleep(2000)
            sink.success(CustomerResponse(id, "customer-name-$id"))
        }.subscribeOn(Schedulers.elastic())
                .doFirst { log.info("Start CustomerService.getCustomerById($id)") }
                .doOnSuccess { log.info("End CustomerService.getCustomerById") }
                .doOnError { log.error(it.message) }
    }
}