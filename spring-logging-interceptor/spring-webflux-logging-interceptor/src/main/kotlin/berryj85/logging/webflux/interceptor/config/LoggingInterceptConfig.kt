package berryj85.logging.webflux.interceptor.config

import org.apache.logging.log4j.Level
import org.springframework.context.annotation.Configuration

val DEFAULT_EXCLUDE_URL = arrayListOf("**/info", "**/prometheus")


@Configuration("logging")
open class LoggingInterceptConfig(
    var enable: Boolean = true,
    var level: Level = Level.INFO,
    var excludeUrl: List<String> = DEFAULT_EXCLUDE_URL,
    var request: LoggingRequestConfig = LoggingRequestConfig(),
    var response: LoggingResponseConfig = LoggingResponseConfig()
)

abstract class LoggingConfig {
    var enable: Boolean = true
    abstract var prefix: String
}

data class LoggingRequestConfig(
    override var prefix: String = "REQUEST",
    var enableParameter: Boolean = true,
    var enableHeader: Boolean = true
) : LoggingConfig()

data class LoggingResponseConfig(
    override var prefix: String = "RESPONSE"
) : LoggingConfig()