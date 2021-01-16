package lesson

import org.apache.logging.log4j.LogManager
import org.junit.Test
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import reactor.core.scheduler.Schedulers
import java.lang.RuntimeException
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.function.Consumer


class MonoLesson {

    companion object {
        private val log = LogManager.getLogger(MonoLesson::class.java)
    }

    @Test
    @Tags(Tag("Mono"), Tag("Create "))
    fun `create mono`() {

        val mono1 = Mono.just("The data")
        val mono2 = Mono.create<String> {
            log.info("Returning The data")
            it.success("The data")
        }
        val mono3 = Mono.create(this.getDataConsumer())
    }

    private fun getDataConsumer(sleep: Long? = 0): Consumer<MonoSink<String>> {
        return Consumer {
            takeIf { sleep != null }.run { Thread.sleep(sleep!!) }
            log.info("Returning The data")
            it.success("The data")
        }
    }

    @Test
    @Tags(Tag("Mono"), Tag("subscriber"), Tag("subscribe"))
    fun `subscriber mono subscribe`() {
        val mono = Mono.create(this.getDataConsumer())
        mono.subscribe {
            log.info("> $it")
        }
        mono.subscribe(this.subscribeMethod())
        Mono.create(this.getDataConsumer(5000)).subscribe()
    }

    @Test
    @Tags(Tag("Mono"), Tag("subscriber"), Tag("subscribe"))
    fun `subscriber mono subscribe with handler error`() {

        val mono = Mono.create(validateDataConsumer("This is 1st January"))
        mono.doOnError {
            log.error("Mono return error : ${it.message}")
        }.doFinally {
            log.info("Mono1-Terminated")
        }.subscribe()

        val mono2 = Mono.create(validateDataConsumer("This is January"))
        mono2.doOnError {
            log.error("Mono-2Return error : ${it.message}")
        }.doFinally {
            log.info("Mono2-Terminated")
        }.subscribe()
    }

    @Test
    fun `subscriber mono with subscriber thread`() {
        val mono = Mono.create(this.getDataConsumer())
            .doOnSuccess { log.info(it) }
            .doOnSubscribe { log.info("start") }
        mono.block() // main thread
        mono.subscribeOn(Schedulers.immediate()).block() // main thread
        mono.subscribeOn(Schedulers.boundedElastic()).block() //elastic
        mono.subscribeOn(Schedulers.single()).block() //single
        mono.subscribeOn(Schedulers.newSingle("New-Single-Thread")).block() //new single

        val scheduleThread = Executors.newScheduledThreadPool(2)
        mono.subscribeOn(Schedulers.fromExecutor(scheduleThread)).block() //scheduleThread

        val threadPoolQueue = LinkedBlockingQueue<Runnable>(100)
        val threadPoolExecutor = ThreadPoolExecutor(1, 10, 5000, TimeUnit.MILLISECONDS, threadPoolQueue, Executors.defaultThreadFactory())
        mono.subscribeOn(Schedulers.fromExecutor(threadPoolExecutor)).block() // thread pool with queue
    }
    @Test
    fun `subscribe operations`(){
        Mono.create(this.getDataConsumer())
            .doOnNext { log.info(">>>$it") }
            .map { "Mapping $it" }
            .doOnSuccess { log.info(">>>>$it") }
            .block()
    }


    private fun validateDataConsumer(data: String): Consumer<MonoSink<Void>> {
        return Consumer { emitter ->
            if (data.contains(Regex("[0-9]"))) {
                emitter.error(RuntimeException("Data has numeric"))
            }
            emitter.success()
        }
    }


    private fun subscribeMethod(): Consumer<String> {
        return Consumer {
            log.info(">> $it")
        }
    }


    @Test
    @Tags(Tag("Mono"), Tag("workshop"))
    fun `create mono that get data from url`() {

    }


}
