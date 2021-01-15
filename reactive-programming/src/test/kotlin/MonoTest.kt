import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.function.Consumer

class MonoTest {

    val log = LogManager.getLogger(MonoTest::class.java)

    @Test
    fun createMono() {
        val data: String = "The data"
        val publisher: Mono<String> = Mono.just(data)
        log.info("start")
        publisher.map {
            it.replace(" ", "").also { mapped ->
                log.info(mapped)
            }
        }.subscribeOn(Schedulers.single()).block()

    }

    @Test
    fun createMono_1() {
        val data = "The Data"

        val result = Mono.create<String> { emitter ->
            log.info("start mono")
            if (data.isBlank())
                emitter.error(Exception("Throw exception"))
            else
                emitter.success(data.replace(" ", ""))
        }.subscribeOn(Schedulers.single()).block()
        log.info(result)

    }


    @Test
    fun createMono_1_1() {
        val publisher = Mono.create<Void> { emitter ->
            log.info("start mono")

        }.subscribeOn(Schedulers.single())

        log.info("starting mono")
        publisher.block()

        log.info("end mono")

    }


    @Test
    fun createMono_2_subscriber() {
        val data = "The data"
        val publisher = Mono.create(createConsumer(data))
        publisher.subscribeOn(Schedulers.single())
            .doOnSuccess {
                log.info("in do success $it")
            }.doOnError {
                log.error("in do error")
            }.doFinally {
                log.info("finally ${it.name}")
            }.doFirst {
                log.info("first")
            }.doOnSubscribe {
                log.info("subscribe")
            }.doOnNext {
                log.info(it)
            }
            .block()

        log.info("end main")
    }


    @Test
    fun createMono_2() {
        val data = "xxxx"
        val pool = Executors.newFixedThreadPool(100)
        val publisher = Mono.create(createConsumer(data))
        publisher.subscribeOn(Schedulers.fromExecutor(pool)).block()
    }


    private fun createConsumer(data: String): Consumer<MonoSink<String>> {
        return Consumer { emitter ->
            log.info("Mono start")
            if (data.isBlank()) {
                throw  Exception("Exception")
            } else {
                emitter.success(data.replace(" ", ""))
            }

        }
    }


    @Test
    fun operation_1() {
        val xxx = listOf<String>("a1","a2","a3").map {
            "<>>>$it"
        }

        val publisher1 = Mono.create(createConsumer("The data"))
            .map {
                "publisher1- $it"
            }
            .doOnNext { log.info(">>>>>>>>") }


        val publisher2 = Mono.create(createConsumer("The data2"))
            .map {
                "publisher2- $it"
            }

        Mono.zip(publisher1, publisher2).subscribeOn(Schedulers.parallel())

    }
    fun createMono_():Mono<String>{
        return Mono.create(createConsumer("The data"))
    }
    @Test
    fun unitest(){

        val mono = this.createMono_()
        StepVerifier.create(mono)
            .expectNext("Thedata")
            .verifyComplete()
    }

}