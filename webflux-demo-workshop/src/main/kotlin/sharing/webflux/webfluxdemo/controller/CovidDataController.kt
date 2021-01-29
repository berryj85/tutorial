package sharing.webflux.webfluxdemo.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import sharing.webflux.webfluxdemo.service.CovidCaseResponse
import sharing.webflux.webfluxdemo.service.CovidDataService

@RestController
@RequestMapping("/covid")
class CovidDataController {
    @Autowired
    private lateinit var covidDataService: CovidDataService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getAllCovidCase(): Flux<CovidCaseResponse> {
        return covidDataService.getAllCase()
    }

    @GetMapping("/gender/{gender}")
    fun getCovidCaseByGender(@PathVariable("gender") gender: String): Flux<CovidCaseResponse> {
        return covidDataService.getAllByGender(gender)
    }
}