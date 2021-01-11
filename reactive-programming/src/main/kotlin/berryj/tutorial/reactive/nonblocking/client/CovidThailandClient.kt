package berryj.tutorial.reactive.nonblocking.client

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection


object CovidThailandClient {

    private val objectMapper = jacksonObjectMapper()
    private const val BASE_URL = "https://covid19.th-stat.com/api/open"

    fun daily(): Mono<CovidCase> = Mono.create { emitter ->
        val httpConnect =
            (URL("$BASE_URL/today").openConnection() as HttpsURLConnection).apply {
                this.requestMethod = "GET"
                this.addRequestProperty("User-Agent", "")
                this.connect()
            }
        if (httpConnect.responseCode == HttpURLConnection.HTTP_OK)
            emitter.success(objectMapper.readValue(httpConnect.inputStream, CovidCase::class.java))
        else
            emitter.error(RuntimeException("${httpConnect.responseCode}:${httpConnect.responseMessage}"))
    }

    fun timeline(): Flux<CovidCase> = Flux.create { emitter ->
        val httpConnect = (URL("$BASE_URL/timeline").openConnection() as HttpsURLConnection).apply {
            this.requestMethod = "GET"
            this.addRequestProperty("User-Agent", "")
            this.connect()
        }
        if (httpConnect.responseCode == HttpURLConnection.HTTP_OK)
            objectMapper.readValue(httpConnect.inputStream, TimelineCovid::class.java).let {timelineData->
                timelineData.data?.forEach { covidCase -> emitter.next(covidCase) } ?: emitter.error(RuntimeException("Data not found"))
                emitter.complete()
            }
        else
            emitter.error(RuntimeException("${httpConnect.responseCode}:${httpConnect.responseMessage}"))
    }
}

class DateHandler : StdDeserializer<Date>(Date::class.java) {
    override fun deserialize(parser: JsonParser, deserializer: DeserializationContext): Date =
        SimpleDateFormat("dd/MM/yyyy").let {
            it.parse(parser.text)
        }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class TimelineCovid(
    @JsonProperty("Data")
    val data: List<CovidCase>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CovidCase(
    @JsonProperty("NewConfirmed")
    val confirmed: Long? = null,
    @JsonProperty("NewRecovered")
    val recovered: Long? = null,
    @JsonProperty("NewHospitalized")
    val hospitalized: Long? = null,
    @JsonProperty("NewDeaths")
    val deaths: Long? = null,
    @JsonDeserialize(using = DateHandler::class)
    @JsonProperty("UpdateDate")
    @JsonAlias(*["Date","UpdateDate"])
    val date: Date? = null
)