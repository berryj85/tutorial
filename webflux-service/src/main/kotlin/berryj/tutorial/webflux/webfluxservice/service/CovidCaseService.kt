package berryj.tutorial.webflux.webfluxservice.service

import berryj.tutorial.webflux.webfluxservice.client.CovidCase
import berryj.tutorial.webflux.webfluxservice.client.ThailandCovidDataClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import reactor.cache.CacheFlux
import reactor.core.publisher.Flux


@Service
class CovidCaseService {

    @Autowired
    private lateinit var thailandCovidDataClient: ThailandCovidDataClient

    fun getAllCovidCase(): Flux<CovidCase> {
        return thailandCovidDataClient.getCovidCase()
    }
}