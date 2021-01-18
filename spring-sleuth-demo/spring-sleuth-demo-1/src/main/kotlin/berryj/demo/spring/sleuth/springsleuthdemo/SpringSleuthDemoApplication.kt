package berryj.demo.spring.sleuth.springsleuthdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class SpringSleuthDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringSleuthDemoApplication>(*args)
}
