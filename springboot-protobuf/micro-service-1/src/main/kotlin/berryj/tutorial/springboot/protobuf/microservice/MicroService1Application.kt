package berryj.tutorial.springboot.protobuf.microservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MicroService1Application

fun main(args: Array<String>) {
	runApplication<MicroService1Application>(*args)
}
