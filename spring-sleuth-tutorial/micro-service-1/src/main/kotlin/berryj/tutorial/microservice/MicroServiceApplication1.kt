package berryj.tutorial.microservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MicroServiceApplication1

fun main(args: Array<String>) {
    runApplication<MicroServiceApplication1>(*args)
}
