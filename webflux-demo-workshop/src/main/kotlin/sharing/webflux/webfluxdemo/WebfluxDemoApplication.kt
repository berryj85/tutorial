package sharing.webflux.webfluxdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication
class WebfluxDemoApplication

fun main(args: Array<String>) {
	runApplication<WebfluxDemoApplication>(*args)
}
