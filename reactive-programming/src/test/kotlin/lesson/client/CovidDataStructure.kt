package lesson.client

import berryj.tutorial.reactive.nonblocking.client.DateConverter
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class CovidCase(
    @JsonProperty("ConfirmDate")
    var confirmDate: String? = null,
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
data class TimelineCovid(
    @JsonProperty("Data")
    val data: List<SummaryCovidCase>? = null
)
