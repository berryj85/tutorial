package berryj85.logging.webflux.interceptor.filter

import berryj85.logging.webflux.interceptor.config.LoggingInterceptConfig
import berryj85.logging.webflux.interceptor.decorator.LoggingRequestDecorator
import berryj85.logging.webflux.interceptor.decorator.LoggingResponseDecorator
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class LoggingWebFilter(private val config: LoggingInterceptConfig) : WebFilter {

    companion object {
        private val antPathMatcher = AntPathMatcher()

        @JvmStatic
        val log: Logger = LogManager.getLogger(LoggingWebFilter::class.java)
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return if (config.enable && !this.isExcludeUrl(exchange.request.uri.path)) {
            val request = if (config.request.enable) {
                LoggingRequestDecorator(exchange.request, config)
            } else {
                exchange.request
            }
            val response = if (config.response.enable) {
                LoggingResponseDecorator(exchange.response, config)
            } else {
                exchange.response
            }
            chain.filter(exchange.mutate().request(request).response(response).build())
        } else {
            chain.filter(exchange)
        }
    }

    private fun isExcludeUrl(uri: String): Boolean = config.excludeUrl.any {
        antPathMatcher.match(it, uri)
    }
}