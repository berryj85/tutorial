package berryj.demo.webflux.handler

import berryj.demo.webflux.repository.CustomerEntity
import berryj.demo.webflux.service.CustomerService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class CustomerHandler(val customerService: CustomerService) {

    fun getCustomer(request: ServerRequest): Mono<ServerResponse> {
        return request.pathVariable("customerId").toMono().flatMap {customerId->
            customerService.getCustomer(customerId).flatMap {
                ok().body(it, CustomerEntity::class.java)
            }
        }
    }

    fun saveCustomer(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(CustomerEntity::class.java)
            .doOnSuccess {
                customerService.saveCustomer(it)
            }.flatMap {
                ok().contentType(MediaType.APPLICATION_JSON).bodyValue(it)
            }

    }
}