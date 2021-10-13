package berryj.tutorial.microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Value("${application.micro-service-2.url}")
    private String microService2Url;

    @Bean
    public WebClient microServiceWebClient() {
        return WebClient.builder().baseUrl(microService2Url).build();
    }
}
