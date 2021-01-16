import berryj.tutorial.webflux.webfluxservice.client.CovidCase
import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.lang.Exception

class CovidClientTest {

    @Test
    fun `Should return covid cases`() {
        val log = LogManager.getLogger(CovidClientTest::class.java)
        WebClient.builder().build().get().uri("http://localhost:9091/webflux-service/covid")
            .accept(MediaType.APPLICATION_STREAM_JSON)
            .retrieve().bodyToFlux(CovidCase::class.java)
            .doOnNext {
                log.info(it)
            }
            .blockLast()
    }
}