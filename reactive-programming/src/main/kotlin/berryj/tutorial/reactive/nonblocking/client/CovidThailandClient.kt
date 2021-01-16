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
    private const val BASE_URL = "https://scovid19.th-stat.com/api/open"


    fun daily(): Mono<SummaryCovidCase> = Mono.create { emitter ->
        this.createGetHttpsConnection("$BASE_URL/today").let { httpConnect ->
            if (httpConnect.responseCode == HttpURLConnection.HTTP_OK)
                emitter.success(objectMapper.readValue(httpConnect.inputStream, SummaryCovidCase::class.java))
            else
                emitter.error(RuntimeException("${httpConnect.responseCode}:${httpConnect.responseMessage}"))
        }

    }

    fun timeline(): Flux<SummaryCovidCase> = Flux.create { emitter ->
        this.createGetHttpsConnection("$BASE_URL/timeline").let { httpConnect ->
            if (httpConnect.responseCode == HttpURLConnection.HTTP_OK)
                objectMapper.readValue(httpConnect.inputStream, TimelineCovid::class.java).let { timelineData ->
                    timelineData.data?.forEach { covidCase -> emitter.next(covidCase) }
                        ?: emitter.error(RuntimeException("Data not found"))
                    emitter.complete()
                }
            else
                emitter.error(RuntimeException("${httpConnect.responseCode}:${httpConnect.responseMessage}"))
        }

    }

    fun cases() = Flux.create<CovidCase> { emitter ->
        this.createGetHttpsConnection("$BASE_URL/cases").let { httpConnect ->
            if (httpConnect.responseCode == HttpURLConnection.HTTP_OK)
                objectMapper.readValue(httpConnect.inputStream, CovidCaseWrapper::class.java).let { wrapper ->
                    wrapper.data?.forEach { covidCase -> emitter.next(covidCase) }
                        ?: emitter.error(RuntimeException("Data not found"))
                    emitter.complete()
                }
            else
                emitter.error(RuntimeException("${httpConnect.responseCode}:${httpConnect.responseMessage}"))
        }
    }

    private fun createGetHttpsConnection(urlStirng: String): HttpsURLConnection =
        (URL(urlStirng).openConnection() as HttpsURLConnection).apply {
            this.requestMethod = "GET"
            this.addRequestProperty("User-Agent", "")
            this.connect()
        }
}

class DateConverter : StdDeserializer<Date>(Date::class.java) {
    override fun deserialize(parser: JsonParser, deserializer: DeserializationContext): Date =
        SimpleDateFormat("dd/MM/yyyy").let {
            it.parse(parser.text)
        }
}

class DateTimeConverter : StdDeserializer<Date>(Date::class.java) {
    override fun deserialize(parser: JsonParser, deserializer: DeserializationContext): Date =
        SimpleDateFormat("yyyy-MM-dd hh:mm:ss").let {
            it.parse(parser.text)
        }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class CovidCase(
    @JsonProperty("ConfirmDate")
    val confirmDate: String? = null,
    @JsonProperty("No")
    val no: String? = null,
    @JsonProperty("Age")
    val age: Int? = null,
    @JsonProperty("Gender")
    val gender: String? = null,
    @JsonProperty("GenderEn")
    val genderEn: String? = null,
    @JsonProperty("Nation")
    val nation: String? = null,
    @JsonProperty("NationEn")
    val nationEn: String? = null,
    @JsonProperty("Province")
    val province: String? = null,
    @JsonProperty("ProvinceId")
    val provinceId: Int? = null,
    @JsonProperty("District")
    val district: String? = null,
    @JsonProperty("ProvinceEn")
    val provinceEn: String? = null,
    @JsonProperty("detail")
    val detail: String? = null,
    @JsonProperty("StatQuarantine")
    val statQuarantine: Boolean = false
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SummaryCovidCase(
    @JsonProperty("NewConfirmed")
    val confirmed: Long? = null,
    @JsonProperty("NewRecovered")
    val recovered: Long? = null,
    @JsonProperty("NewHospitalized")
    val hospitalized: Long? = null,
    @JsonProperty("NewDeaths")
    val deaths: Long? = null,
    @JsonDeserialize(using = DateConverter::class)
    @JsonProperty("UpdateDate")
    @JsonAlias(*["Date", "UpdateDate"])
    val date: Date? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
 data class CovidCaseWrapper(
    @JsonProperty("Data")
    val data: List<CovidCase>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
private data class TimelineCovid(
    @JsonProperty("Data")
    val data: List<SummaryCovidCase>? = null
)

