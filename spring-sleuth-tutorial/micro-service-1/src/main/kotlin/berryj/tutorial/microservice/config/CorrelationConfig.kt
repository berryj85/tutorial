package berryj.tutorial.microservice.config

import brave.Tracing
import brave.propagation.B3Propagation
import brave.propagation.ExtraFieldPropagation
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CorrelationConfig {
    @Bean
    @Qualifier("CorrelationIdTracing")
    fun correlationIdTracing(): Tracing.Builder = Tracing.newBuilder().propagationFactory(
            ExtraFieldPropagation.newFactoryBuilder(B3Propagation.FACTORY)
                    .addField("X-Correlation-Id")
                    .addField("X-Forwarded-For")
                    .build()
    )
}