package berryj.tutorial.springboot.protobuf.microservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MicroService2Application

fun main(args: Array<String>) {
    runApplication<MicroService2Application>(*args)
}
