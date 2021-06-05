package berryj85.logging.webflux.interceptor.decorator

import berryj85.logging.webflux.interceptor.config.LoggingInterceptConfig
import berryj85.logging.webflux.interceptor.filter.LoggingWebFilter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.util.StreamUtils
import reactor.core.publisher.Flux
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.channels.Channels

class LoggingRequestDecorator(private val request: ServerHttpRequest, private val config: LoggingInterceptConfig) :
    ServerHttpRequestDecorator(request) {
    @Throws(IOException::class)

    override fun getBody(): Flux<DataBuffer> {
        return super.getBody().doOnNext { dataBuffer ->
            ByteArrayOutputStream().apply {
                Channels.newChannel(this).write(dataBuffer.asByteBuffer().asReadOnlyBuffer())
            }.let { outputStream ->
                this.log(StreamUtils.copyToString(outputStream, Charsets.UTF_8))
            }
        }
    }

    private fun log(body: String) {
        StringBuilder().also { sb ->
            if (config.request.enableHeader && request.headers.isNotEmpty()) {
                sb.append(" headers=[")
                    .append(request.headers.toSingleValueMap().let { jacksonObjectMapper().writeValueAsString(it) })
                    .append("]")
            }
            if (config.request.enableParameter && request.queryParams.isNotEmpty()) {
                sb.append(" params=[")
                    .append(request.queryParams.toSingleValueMap().let { jacksonObjectMapper().writeValueAsString(it) })
                    .append("]")
            }
            if (body.isNotEmpty()) {
                sb.append(" body=[").append(body).append("]")
            }
        }.let {
            LoggingWebFilter.log.log(
                config.level, "{} method=[{}] uri=[{}]{}", config.request.prefix,
                request.methodValue,
                request.uri.path,
                it.toString()
            )
        }
    }
}
