package berryj.demo.spring.sleuth.springsleuthdemo.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.util.*

@Component
class DemoClient {
    companion object {
        private val log = LoggerFactory.getLogger(DemoClient::class.java)
    }

    @Value("\${app.demo2.host}")
    private lateinit var host: String

    @Value("\${app.demo2.endpoints.all-covid-statistic}")
    private lateinit var covidAllEndpoint: String

    @Value("\${app.demo2.endpoints.latest-covid-statistic}")
    private lateinit var covidLatestEndpoint: String

    @Autowired
    private lateinit var webClient: WebClient

    fun getAllCovid(): Flux<CovidDailyReport> = webClient.get().uri("$host$covidAllEndpoint")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToFlux(CovidDailyReport::class.java)

    fun getLatestCovid(): Flux<CovidDailyReport> = webClient.get().uri("$host$covidLatestEndpoint")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToFlux(CovidDailyReport::class.java)

    data class CovidDailyReport(
        val region: String,
        val territory: Boolean,
        val name: String,
        val daysSinceLastCase: Int,
        val transmissionType: Int,
        val newDeaths: Int,
        val deaths: Int,
        val cases: Int,
        val newCases: Int,
        val reportDate: Date,
        val reportNumber: Int
    )
}