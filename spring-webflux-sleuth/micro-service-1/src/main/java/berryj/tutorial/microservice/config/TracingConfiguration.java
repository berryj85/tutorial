package berryj.tutorial.microservice.config;

import berryj.tutorial.microservice.constant.TracingContant;
import brave.baggage.BaggageField;
import brave.baggage.CorrelationScopeConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfiguration {

    @Bean("correlation-baggage")
    public BaggageField correlationBaggageField() {
        return BaggageField.create(TracingContant.MDC_CORRELATION_ID);
    }

    @Bean("clientIp-baggage")
    public BaggageField clientIpBaggageField() {
        return BaggageField.create(TracingContant.HEADER_FORWARD_FOR);
    }


    @Bean
    public CurrentTraceContext.ScopeDecorator mdcScopeDecorator(@Qualifier("correlation-baggage") BaggageField correlationBaggageField
            , @Qualifier("clientIp-baggage") BaggageField clientIpBaggageField) {
        return MDCScopeDecorator.newBuilder()
                .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(correlationBaggageField).flushOnUpdate().build())
                .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(clientIpBaggageField).flushOnUpdate().build())
                .build();
    }

}
