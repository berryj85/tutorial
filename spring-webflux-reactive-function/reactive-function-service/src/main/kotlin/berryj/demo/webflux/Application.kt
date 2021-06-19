package berryj.demo.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound

@SpringBootApplication
class Application {
}

fun main(args: Array<String>) {
    BlockHound.install()
    runApplication<Application>(*args)
}
