package berryj.tutorial.microservice.config

import io.netty.handler.ssl.SslContextBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient

@Configuration
class WebClientConfig {
    @Bean
    fun httpClient(): HttpClient = HttpClient.from(TcpClient.create().secure { spec -> spec.sslContext(SslContextBuilder.forClient().build()).build() })

    @Bean
    fun webClient(httpClient: HttpClient): WebClient = WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient)).build()
}