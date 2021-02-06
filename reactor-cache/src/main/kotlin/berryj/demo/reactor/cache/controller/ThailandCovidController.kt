package berryj.demo.reactor.cache.controller

import berryj.demo.reactor.cache.client.SummaryTimeline
import berryj.demo.reactor.cache.client.ThailandCovidClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/covid")
class ThailandCovidController {
    @Autowired
    private lateinit var thailandCovidClient: ThailandCovidClient

    @GetMapping("/summary-timeline", produces = *[MediaType.APPLICATION_JSON_VALUE])
    fun getSummaryTimeline(): Flux<SummaryTimeline> {
        return thailandCovidClient.getSummaryTimeline()
    }

}