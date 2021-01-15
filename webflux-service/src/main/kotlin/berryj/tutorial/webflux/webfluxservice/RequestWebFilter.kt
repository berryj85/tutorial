package berryj.tutorial.webflux.webfluxservice

import brave.baggage.BaggageField
import brave.baggage.BaggageFields
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*
import org.springframework.stereotype.Component


@Component
class RequestWebFilter : WebFilter {
    @Autowired
    @Qualifier("correlationBaggageField")
    private lateinit var correlationBaggageField: BaggageField

    @Autowired
    @Qualifier("clientIpBaggageField")
    private lateinit var clientIpBaggageField: BaggageField

    @Autowired
    @Qualifier("traceIdBaggageField")
    private lateinit var traceIdBaggageField: BaggageField

    @Autowired
    @Qualifier("spanIdBaggageField")
    private lateinit var spanIdBaggageField: BaggageField

    companion object {
        private val correlationKey = "X-Correlation-Id"
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange).doOnSubscribe {
            traceIdBaggageField.updateValue(BaggageFields.TRACE_ID.value)
            spanIdBaggageField.updateValue(BaggageFields.SPAN_ID.value)
            val headers = exchange.request.headers
            val clientIp = headers["X-Forwarded-For"]?.toString()?.split(",").let { clientIps ->
                if (!clientIps.isNullOrEmpty())
                    clientIps[0]
                else
                    exchange.request.remoteAddress.toString()
            }
            clientIpBaggageField.updateValue(clientIp)
            UUID.randomUUID().also { uuid ->
                if (!headers[correlationKey]?.get(0).isNullOrEmpty()) {
                    correlationBaggageField
                        .updateValue("${headers[correlationKey]?.get(0).toString()}")
                } else if (correlationBaggageField.value.isNullOrEmpty()) {
                    correlationBaggageField
                        .updateValue("ECATALOG_AUTH_SERVICE-${uuid.toString().replace("-", "").toLowerCase()}")

                }
            }
        }
    }
}