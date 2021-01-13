package lesson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import lesson.client.CovidCase
import lesson.client.CovidCaseWrapper
import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import javax.net.ssl.HttpsURLConnection

class FluxWorkshop1 {

    private val BASE_URL = "https://covid19.th-stat.com/api/open"
    private val log = LogManager.getLogger(FluxWorkshop1::class.java)

    //TODO revise this function to Flux
    @Tags(*[Tag("Workshop"), Tag("Flux")])
    private fun getCovidCases(): List<CovidCase> {
        this.createGetHttpsConnection("$BASE_URL/cases").let { httpConnect ->
            if (httpConnect.responseCode == HttpURLConnection.HTTP_OK)
                return jacksonObjectMapper().readValue(httpConnect.inputStream, CovidCaseWrapper::class.java).data
                    ?: throw RuntimeException("Data not found")
            else
                throw RuntimeException("${httpConnect.responseCode}:${httpConnect.responseMessage}")
        }
    }

    //TODO revise this function to Consumer
    @Tags(*[Tag("Workshop"), Tag("Flow"), Tag("Consumer")])
    private fun writeJsonToFile(filename: String, jsonString: String) {
        File("./data").takeIf { !it.exists() }?.apply { this.mkdirs() }
        File("./data/$filename").takeIf { !it.exists() }?.apply {
            BufferedWriter(FileWriter(this)).apply {
                this.write(jsonString)
                this.flush()
                this.close()
            }
        }
    }

    //TODO create function for save covid cases to file
    // - separate file with confirmed date
    @Test
    @Tags(*[Tag("Workshop"), Tag("Flux")])
    fun workshop() {

    }


    private fun convertDateToFilename(dateString: String?): String {
        if (dateString.isNullOrBlank())
            return "other"
        return SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateString)
            .let { date ->
                SimpleDateFormat("yyyyMMdd").format(date)
            }
    }


    private fun createGetHttpsConnection(urlString: String): HttpsURLConnection =
        (URL(urlString).openConnection() as HttpsURLConnection).apply {
            this.requestMethod = "GET"
            this.addRequestProperty("User-Agent", "")
            this.connect()
        }
}