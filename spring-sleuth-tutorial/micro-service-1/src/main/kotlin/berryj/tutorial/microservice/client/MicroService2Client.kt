package berryj.tutorial.microservice.client

import berryj.tutorial.microservice.response.CustomerResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class MicroService2Client {

    @Autowired
    private lateinit var webClient: WebClient

    fun getCustomerById(id: String) = webClient.get().uri("http://localhost:8082/customers/$id")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(CustomerResponse::class.java)


}