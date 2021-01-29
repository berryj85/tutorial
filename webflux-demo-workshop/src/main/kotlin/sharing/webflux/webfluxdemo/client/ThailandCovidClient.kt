package sharing.webflux.webfluxdemo.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.RuntimeException

@Component
class ThailandCovidClient {
    @Autowired
    private lateinit var webClient: WebClient

    fun getAllCase(): Flux<ThailandCovidCase> {
        return webClient.get()
            .uri("https://covid19.th-stat.com/api/open/cases")
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono{
                if(it.statusCode() == HttpStatus.OK){
                    it.bodyToMono(ThailandCovidCaseWrapper::class.java)
                }else {
                    Mono.error(RuntimeException("Data not found"))
                }
            }.flatMapMany {
                Flux.fromIterable(it?.data ?: listOf())
            }
    }
}

data class ThailandCovidCaseWrapper(
    @JsonProperty("Data")
    val data: List<ThailandCovidCase>? = null
)

data class ThailandCovidCase(
    @JsonProperty("ConfirmDate")
    val confirmDate: String? = null,
    @JsonProperty("No")
    val no: String? = null,
    @JsonProperty("Age")
    val age: String? = null,
    @JsonProperty("Gender")
    val gender: String? = null,
    @JsonProperty("GenderEn")
    val genderEn: String? = null,
    @JsonProperty("NationString")
    val nationString: String? = null,
    @JsonProperty("NationEn")
    val nationEn: String? = null,
    @JsonProperty("Province")
    val province: String? = null,
    @JsonProperty("ProvinceId")
    val provinceId: String? = null,
    @JsonProperty("District")
    val district: String? = null,
    @JsonProperty("ProvinceEn")
    val provinceEn: String? = null,
    @JsonProperty("Detail")
    val detail: String? = null,
    @JsonProperty("StatQuarantine")
    val statQuarantine: Int? = null
)