package berryj.tutorial.webflux.webfluxservice

import brave.baggage.BaggageField
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import brave.baggage.CorrelationScopeConfig.SingleCorrelationField

import brave.context.slf4j.MDCScopeDecorator

import brave.propagation.CurrentTraceContext.ScopeDecorator


@Configuration
class ProcessTracingConfiguration {
    @Bean("correlationBaggageField")
    fun correlationBaggageField(): BaggageField {
        return BaggageField.create("X-Correlation-Id")
    }

    @Bean("clientIpBaggageField")
    fun clientIpBaggageField(): BaggageField {
        return BaggageField.create("X-Client-Ip")
    }

    @Bean("traceIdBaggageField")
    fun traceIdBaggageField(): BaggageField {
        return BaggageField.create("X-B3-TraceId")
    }

    @Bean("spanIdBaggageField")
    fun spanIdBaggageField(): BaggageField {
        return BaggageField.create("X-B3-SpanId")
    }

    @Bean
    fun mdcScopeDecorator(@Qualifier("correlationBaggageField") correlationField: BaggageField,
                          @Qualifier("clientIpBaggageField") clientIpBaggageField: BaggageField,
                          @Qualifier("traceIdBaggageField") traceIdBaggageField: BaggageField,
                          @Qualifier("spanIdBaggageField") spanIdBaggageField: BaggageField
    ): ScopeDecorator {
        return MDCScopeDecorator.newBuilder()
                .add(SingleCorrelationField.newBuilder(correlationField).flushOnUpdate().build())
                .add(SingleCorrelationField.newBuilder(clientIpBaggageField).flushOnUpdate().build())
                .add(SingleCorrelationField.newBuilder(traceIdBaggageField).flushOnUpdate().build())
                .add(SingleCorrelationField.newBuilder(spanIdBaggageField).flushOnUpdate().build())
                .build()
    }

}