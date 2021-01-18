package berryj.demo.spring.sleuth.springsleuthdemo.filter

import berryj.demo.spring.sleuth.springsleuthdemo.config.BaggageFieldConfig.Companion.HEADER_CORRELATION_KEY
import brave.baggage.BaggageField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*

@Component
class RequestHeaderWebFilter : WebFilter, Ordered {

    @Autowired
    private lateinit var correlationIdBaggage: BaggageField

    @Autowired
    private lateinit var clientIpBaggage: BaggageField
    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange).doOnSubscribe {
            val headers = exchange.request.headers
            val clientIp = headers["X-Forwarded-For"]?.toString()?.split(",").let { clientIps ->
                if (!clientIps.isNullOrEmpty())
                    clientIps[0]
                else
                    exchange.request.remoteAddress.toString()
            }
            clientIpBaggage.updateValue(clientIp)
            UUID.randomUUID().also { uuid ->

                if (!headers[HEADER_CORRELATION_KEY]?.get(0).isNullOrEmpty()) {
                    correlationIdBaggage
                        .updateValue("${headers[HEADER_CORRELATION_KEY]?.get(0).toString()}")
                } else if (correlationIdBaggage.value.isNullOrEmpty()) {
                    correlationIdBaggage
                        .updateValue("DEMO1-${uuid.toString().replace("-", "").toLowerCase()}")

                }
            }
        }
    }


}