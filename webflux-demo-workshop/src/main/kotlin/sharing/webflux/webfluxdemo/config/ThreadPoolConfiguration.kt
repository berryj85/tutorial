package sharing.webflux.webfluxdemo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class ThreadPoolConfiguration {
    @Bean
    fun executorService(): ExecutorService {
        return Executors.newFixedThreadPool(10)
    }
}