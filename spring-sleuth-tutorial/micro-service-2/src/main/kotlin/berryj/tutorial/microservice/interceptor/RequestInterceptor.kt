package berryj.tutorial.microservice.interceptor

import brave.ScopedSpan
import brave.Tracer
import brave.propagation.ExtraFieldPropagation
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class RequestInterceptor : HandlerInterceptorAdapter() {

    @Autowired
    private lateinit var tracer: Tracer

    companion object {
        private val log = LogManager.getLogger(RequestInterceptor::class.java)
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val correlationId = request.getHeader("X-Correlation-Id")
                ?: "MICRO2-${UUID.randomUUID().toString().replace("-", "")}"

        ExtraFieldPropagation.set("X-Correlation-Id", correlationId)
        tracer.startScopedSpan("X-Correlation-Id")
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        tracer.currentSpan().finish()
    }
}