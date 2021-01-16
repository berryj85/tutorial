package berryj.tutorial.webflux.webfluxservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class WebfluxServiceApplication

fun main(args: Array<String>) {
    runApplication<WebfluxServiceApplication>(*args)
}
