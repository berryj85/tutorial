package sharing.webflux.webfluxdemo.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import sharing.webflux.webfluxdemo.client.ThailandCovidClient
import sharing.webflux.webfluxdemo.exception.WebfluxDemoException
import java.lang.RuntimeException
import java.util.concurrent.ExecutorService

@Service
class CovidDataService {

    @Autowired
    private lateinit var thailandCovidClient: ThailandCovidClient

    @Autowired
    private lateinit var executorService: ExecutorService

    fun getAllCase(): Flux<CovidCaseResponse> {

        return thailandCovidClient.getAllCase().map {
            CovidCaseResponse(
                it.confirmDate, it.age, it.nationEn, it.genderEn, it.statQuarantine == 1
            )
        }.subscribeOn(Schedulers.fromExecutor(executorService))

    }

    fun getAllByGender(gender: String): Flux<CovidCaseResponse> {
        return this.getAllCase().filter { it.gender == gender }
            .switchIfEmpty {
                it.onError(WebfluxDemoException())
            }
    }
}

data class CovidCaseResponse(
    val date: String? = null,
    val age: String? = null,
    val nation: String? = null,
    val gender: String? = null,
    val statQuarantine: Boolean = false
)