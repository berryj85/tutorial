package berryj.demo.webflux.router

import berryj.demo.webflux.handler.CustomerHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class CustomerRouter {
    @Bean
    fun customerRoute(handler: CustomerHandler): RouterFunction<ServerResponse> =
        nest(
            RequestPredicates.path("/api/v1/customer"),
            route(GET("/{customerId}"), handler::getCustomer)
                .andRoute(POST("/save").and(contentType(MediaType.APPLICATION_JSON)), handler::saveCustomer)
        )

}