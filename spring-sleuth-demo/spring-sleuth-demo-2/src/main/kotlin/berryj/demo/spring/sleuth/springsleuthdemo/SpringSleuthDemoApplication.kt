package berryj.demo.spring.sleuth.springsleuthdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringSleuthDemoApplication

fun main(args: Array<String>) {
	runApplication<SpringSleuthDemoApplication>(*args)
}
