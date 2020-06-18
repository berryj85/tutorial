package berryj.tutorial.springboot.protobuf.microservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun restTemplate(protobufHttpMessageConverter: ProtobufHttpMessageConverter): RestTemplate {
        return RestTemplate(listOf(protobufHttpMessageConverter))
    }
}