package berryj.demo.spring.sleuth.springsleuthdemo.controller

import berryj.demo.spring.sleuth.springsleuthdemo.service.CovidService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/demo2/covid")
class CovidController {
    companion object {
        private val log = LoggerFactory.getLogger(CovidController::class.java)
    }

    @Autowired
    private lateinit var covidService: CovidService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getAllReport(): Flux<CovidService.CovidDailyReport> = covidService.getAllReport()
        .doOnSubscribe { log.info("Start getAllReport") }
        .doFinally { log.info("End getAllReport") }
        .doOnError { log.error(it.message, it) }

    @GetMapping("/latest", produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getLatestReport(): Flux<CovidService.CovidDailyReport> {
        return covidService.getLatestReport()
            .doOnSubscribe { log.info("Start getLatestReport") }
            .doFinally { log.info("End getLatestReport") }
            .doOnError { log.error(it.message, it) }
    }
}