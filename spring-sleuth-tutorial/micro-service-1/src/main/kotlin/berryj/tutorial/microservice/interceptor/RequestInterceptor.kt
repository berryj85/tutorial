package berryj.tutorial.microservice.interceptor

import brave.Tracer
import brave.propagation.ExtraFieldPropagation
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class RequestInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var tracer: Tracer
    companion object {
        private val log = LogManager.getLogger(RequestInterceptor::class.java)
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val correlationId = request.getHeader("X-Correlation-Id")
                ?: "MICRO1-${UUID.randomUUID().toString().replace("-", "")}"
        ExtraFieldPropagation.set("X-Forwarded-For", "avc")
        ExtraFieldPropagation.set("X-Correlation-Id", correlationId)
        tracer.startScopedSpan("X-Correlation-Id")
        return true
    }
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        tracer.currentSpan().finish()
    }

}