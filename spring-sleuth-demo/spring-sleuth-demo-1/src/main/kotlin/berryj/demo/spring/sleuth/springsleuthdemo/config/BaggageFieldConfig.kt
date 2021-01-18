package berryj.demo.spring.sleuth.springsleuthdemo.config

import brave.baggage.BaggageField
import brave.baggage.CorrelationScopeConfig
import brave.context.slf4j.MDCScopeDecorator
import brave.propagation.CurrentTraceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BaggageFieldConfig {

    companion object {
        const val HEADER_CORRELATION_KEY = "X-Correlation-Id"
        const val HEADER_CLIENT_IP_KEY = "X-Client-Ip"
    }

    @Bean
    fun correlationIdBaggage(): BaggageField = BaggageField.create(HEADER_CORRELATION_KEY)

    @Bean
    fun clientIpBaggage(): BaggageField = BaggageField.create(HEADER_CLIENT_IP_KEY)


    @Bean
    fun mdcScopeDecorator(
        correlationIdBaggage: BaggageField,
        clientIpBaggage: BaggageField
    ): CurrentTraceContext.ScopeDecorator {
        return MDCScopeDecorator.newBuilder()
            .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(correlationIdBaggage).flushOnUpdate().build())
            .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(clientIpBaggage).flushOnUpdate().build())
            .build()
    }
}