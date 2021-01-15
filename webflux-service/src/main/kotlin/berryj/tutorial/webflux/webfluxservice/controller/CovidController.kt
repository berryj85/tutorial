package berryj.tutorial.webflux.webfluxservice.controller

import berryj.tutorial.webflux.webfluxservice.client.CovidCase
import berryj.tutorial.webflux.webfluxservice.service.CovidCaseService
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/webflux-service/covid")
class CovidController {

    companion object {
        private val log = LogManager.getLogger(CovidController::class.java)
    }

    @Autowired
    private lateinit var covidCaseService: CovidCaseService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun getAllCovidCase(): Flux<CovidCase> {
        return covidCaseService.getAllCovidCase()
            .doOnSubscribe { log.info("Start CovidController.getAllCovidCase") }
            .doFinally { log.info("End CovidController.getAllCovidCase") }
    }
}