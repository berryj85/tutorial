package berryj85.logging.demo.webflux.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping(
    "/api/v1/customer",
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class CustomerController {

    @PostMapping
    fun addCustomer(@RequestBody customer: Customer): Mono<CommonResponse> {
        return Mono.just(CommonResponse(code = "200", "success"))
    }
}

data class Customer(
    val id: Int? = null,
    val name: String
)

data class CommonResponse(
    val code: String,
    val message: String,
    val data: Any? = null
)