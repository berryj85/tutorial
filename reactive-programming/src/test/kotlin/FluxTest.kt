import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class FluxTest {
    val log = LogManager.getLogger(FluxTest::class.java)

    @Test
    fun createFlux_1() {
        val data = "The data"

        val result = Flux.create<String> { emitter ->
            log.info("start flux")
            data.split(" ").forEach {
                emitter.next(it)
            }
            emitter.complete()

        }.subscribeOn(Schedulers.single()).blockFirst()
        log.info(result)
        log.info("End main")
    }

    @Test
    fun createFlux_2_subscriber() {
        val data = "The data"

        val publisher = Flux.create<String> { emitter ->
            log.info("start flux")
            data.split(" ").forEach {
                    emitter.next(it)
            }
            emitter.complete()

        }.subscribeOn(Schedulers.single())
            .doOnNext {
                log.info("Next 1")
            }.doOnNext {
                log.info("Next 2")
            }
            .doOnEach { log.info("do on each ${it.hasError()}${it.get()}") }
            .blockLast()

    }


    @Test
    fun unitest(){

    }
}