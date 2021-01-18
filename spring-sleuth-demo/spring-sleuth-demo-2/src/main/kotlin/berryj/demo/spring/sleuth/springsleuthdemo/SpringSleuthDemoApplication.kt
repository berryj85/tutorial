package berryj.demo.spring.sleuth.springsleuthdemo

import brave.baggage.BaggageField
import brave.baggage.CorrelationScopeConfig
import brave.context.slf4j.MDCScopeDecorator
import brave.propagation.CurrentTraceContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class SpringSleuthDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringSleuthDemoApplication>(*args)
}
