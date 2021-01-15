package lesson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import lesson.client.CovidCase
import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import reactor.core.scheduler.Schedulers
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.lang.RuntimeException
import java.util.stream.Collectors

class FluxWorkshop2 {

    private val log = LogManager.getLogger(FluxWorkshop2::class.java)

    //TODO convert to publisher
    private fun readJsonFromFile(file: File): String {
        if (!file.exists() || file.isDirectory) {
            throw RuntimeException("${file.name} is not exist or it is directory")
        }
        return BufferedReader(FileReader(file)).lines().collect(Collectors.joining())
    }

    //TODO convert this to Mono
    fun listAllFile(): List<File> {
        return File("./data")?.listFiles()?.toList()
            ?: throw FileNotFoundException("Directory is not found.")
    }

    //TODO create summary function
    @Test
    fun workshop() {
    }


}