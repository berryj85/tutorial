package berryj.demo.reactor.cache.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.logging.log4j.LogManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.cache.CacheFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Signal

@Component
class ThailandCovidClient(val redisTemplate: RedisTemplate<String, Any>) {

    companion object {
        private const val KEY_SUMMARY_TIMELINE = "COVID:SUMMARY_TIMELINE"
    }

    private val log = LogManager.getLogger(ThailandCovidClient::javaClass)


    fun getSummaryTimeline(): Flux<SummaryTimeline> =
        CacheFlux.lookup(this::summaryTimelineCacheReader, KEY_SUMMARY_TIMELINE)
            .onCacheMissResume(this::downloadSummaryTimeline)
            .andWriteWith(this::summaryTimelineCacheWriter)


    private fun summaryTimelineCacheWriter(key: String, mutableList: List<Signal<SummaryTimeline>>): Mono<Void> {
        return Mono.create<Void> { sink ->
            mutableList.map {
                it.get()
            }.toList().let {
                redisTemplate.opsForValue().set(key, it)
            }
            sink.success()
        }.doFirst { log.info("cache summary timeline to redis $key") }
    }

    private fun summaryTimelineCacheReader(key: String): Mono<List<Signal<SummaryTimeline>>?> {
        return Mono.create<List<Signal<SummaryTimeline>>?> { sink ->
            (redisTemplate.opsForValue().get(key) as List<SummaryTimeline>?)?.map { data -> Signal.next(data) }.also {
                sink.success(it)
            }
        }.doFirst {
            log.info("get summary timeline from redis $key")
        }
    }

    private fun downloadSummaryTimeline(): Flux<SummaryTimeline> {
        return WebClient.create().get().uri("https://covid19.th-stat.com/api/open/timeline")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(SummaryTimelineWrapper::class.java)
            .flatMapMany {
                Flux.fromIterable(it.data ?: listOf())
            }
            .switchIfEmpty { Flux.error<RuntimeException>(RuntimeException("Data not found")) }
            .doFirst { log.info("Start get summary timeline") }
            .doFinally { log.info("End get summary timeline") }
    }
}

data class SummaryTimelineWrapper(
    @JsonProperty("UpdateDate")
    val updateDate: String? = null,
    @JsonProperty("Source")
    val source: String? = null,
    @JsonProperty("Data")
    val data: List<SummaryTimeline>? = null
)

data class SummaryTimeline(
    @JsonProperty("Date")
    val date: String? = null,
    @JsonProperty("NewConfirmed")
    val newConfirmed: Int? = null,
    @JsonProperty("NewRecovered")
    val newRecovered: Int? = null,
    @JsonProperty("NewHospitalized")
    val newHospitalized: Int? = null,
    @JsonProperty("NewDeaths")
    val newDeaths: Int? = null,
    @JsonProperty("Confirmed")
    val confirmed: Int? = null,
    @JsonProperty("Recovered")
    val recovered: Int? = null,
    @JsonProperty("Hospitalized")
    val hospitalized: Int? = null,
    @JsonProperty("Deaths")
    val deaths: Int? = null
)

