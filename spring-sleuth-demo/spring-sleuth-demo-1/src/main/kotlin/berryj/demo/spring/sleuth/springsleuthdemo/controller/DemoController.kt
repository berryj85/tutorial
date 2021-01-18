package berryj.demo.spring.sleuth.springsleuthdemo.controller

import berryj.demo.spring.sleuth.springsleuthdemo.client.DemoClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/demo/covid")
class DemoController {
    companion object {
        private val log = LoggerFactory.getLogger(DemoController::class.java)
    }

    @Autowired
    private lateinit var demoClient: DemoClient

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getAllCovidStatistic(): Flux<DemoClient.CovidDailyReport> {
        return demoClient.getAllCovid()
            .doOnSubscribe { log.info("Start getAllReport") }
            .doFinally { log.info("End getAllReport") }
            .doOnError { log.error(it.message, it) }
    }

    @GetMapping("/latest", produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getLatestCovidStatistic(): Flux<DemoClient.CovidDailyReport> {
        return demoClient.getLatestCovid()
            .doOnSubscribe { log.info("Start getLatestReport") }
            .doFinally { log.info("End getLatestReport") }
            .doOnError { log.error(it.message, it) }
    }
}