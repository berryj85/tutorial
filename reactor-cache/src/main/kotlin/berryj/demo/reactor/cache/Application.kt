package berryj.demo.reactor.cache
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class Application
fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
