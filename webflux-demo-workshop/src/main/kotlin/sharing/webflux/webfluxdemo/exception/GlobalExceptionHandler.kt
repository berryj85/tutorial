package sharing.webflux.webfluxdemo.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import reactor.core.publisher.Mono
import java.util.*

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(WebfluxDemoException::class)
    fun webfluxDemoExceptionHandle(webfluxDemoException: WebfluxDemoException): ResponseEntity<CommonExceptionResponse> {
        val commonResponse = CommonExceptionResponse("404-001", webfluxDemoException.message)
        val responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(commonResponse)
        return responseEntity
    }
}

data class CommonExceptionResponse(val responseCode: String, val responseMessage: String?)