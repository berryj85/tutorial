package berryj85.logging.webflux.interceptor.decorator

import berryj85.logging.webflux.interceptor.config.LoggingInterceptConfig
import berryj85.logging.webflux.interceptor.filter.LoggingWebFilter
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.util.StreamUtils
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels

class LoggingResponseDecorator(private val response: ServerHttpResponse, private val config: LoggingInterceptConfig) :
    ServerHttpResponseDecorator(response) {
    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return Mono.from(body).doOnNext { dataBuffer ->
            ByteArrayOutputStream().apply {
                Channels.newChannel(this).write(dataBuffer.asByteBuffer().asReadOnlyBuffer())
            }.let { outputStream ->
                this.log(StreamUtils.copyToString(outputStream, Charsets.UTF_8))
            }
        }.let {
            super.writeWith(it)
        }
    }

    private fun log(body: String) {
        StringBuilder().also { sb ->
            if (body.isNotEmpty()) {
                sb.append(" body=[").append(body).append("]")
            }
        }.let {
            LoggingWebFilter.log.log(
                config.level, "{} status=[{}]{}", config.response.prefix,
                response.rawStatusCode,
                it.toString()
            )
        }
    }
}
