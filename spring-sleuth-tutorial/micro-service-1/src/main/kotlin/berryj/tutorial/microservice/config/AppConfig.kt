package berryj.tutorial.microservice.config

import berryj.tutorial.microservice.interceptor.RequestInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class AppConfig : WebMvcConfigurerAdapter() {
    @Autowired
    private lateinit var requestInterceptor: RequestInterceptor
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(requestInterceptor).addPathPatterns("/**")
    }
}