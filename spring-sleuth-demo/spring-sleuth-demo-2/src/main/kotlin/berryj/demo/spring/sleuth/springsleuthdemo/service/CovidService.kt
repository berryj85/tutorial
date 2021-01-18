package berryj.demo.spring.sleuth.springsleuthdemo.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.scheduler.Schedulers
import reactor.kotlin.extra.math.max
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

@Service
class CovidService {
    companion object {
        private val log = LoggerFactory.getLogger(CovidService::class.java)
    }

    @Value("\${app.report.filename}")
    private lateinit var reportFilename: String
    fun getAllReport(): Flux<CovidDailyReport> = Flux.create(this.readDataFromFile())
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSubscribe { log.info("Start getAllReport") }
        .doFinally { log.info("End getAllReport") }

    fun getLatestReport(): Flux<CovidDailyReport> {
        val allDailyReport = this.getAllReport().cache()
        return allDailyReport.max { covidDailyReport, covidDailyReport2 ->
            covidDailyReport.reportDate.compareTo(covidDailyReport2.reportDate)
        }.map { it.reportDate }.flatMapMany { latestDate ->
            allDailyReport.filter { it.reportDate == latestDate }
        }.subscribeOn(Schedulers.newSingle("single-2"))
            .doOnSubscribe { log.info("Start getLatestReport") }
            .doFinally { log.info("End getLatestReport") }

    }

    private fun readDataFromFile(): Consumer<FluxSink<CovidDailyReport>> {
        return Consumer { sink ->
            try {
                BufferedReader(InputStreamReader(CovidService::class.java.getResourceAsStream(reportFilename)))
                    .apply {
                        this.lines().collect(Collectors.joining()).let { stgData ->
                            if (stgData.isNullOrBlank())
                                sink.error(RuntimeException("Data Not found"))
                            jacksonObjectMapper().readValue(
                                stgData,
                                object : TypeReference<List<CovidDailyReport>>() {})
                        }.forEach { covidDailyReport ->
                            sink.next(covidDailyReport)
                        }
                        sink.complete()
                    }
            } catch (e: Exception) {
                sink.error(RuntimeException("Unexpected", e))
            }
        }
    }


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