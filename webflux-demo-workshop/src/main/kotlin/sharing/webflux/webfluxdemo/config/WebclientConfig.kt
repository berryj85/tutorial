package sharing.webflux.webfluxdemo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebclientConfig {

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder().codecs {
            it.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
        }.build()
    }
}