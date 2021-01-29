package sharing.webflux.webfluxdemo.aspect

import org.apache.logging.log4j.LogManager
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Aspect
@Component
class ControllerAspect {
    companion object {
        private val log = LogManager.getLogger(ControllerAspect::class.java)
    }

    @Pointcut("execution(public * sharing.webflux.webfluxdemo.controller..*Controller.*(..))")
    fun fluxController() {
    }

    @Around("fluxController()")
    fun controllerLoggin(joinPoint: ProceedingJoinPoint): Any? {

        var proceeded = joinPoint.proceed()
        if (proceeded is Flux<*>) {
            proceeded = proceeded.doFirst {
                log.info("Start ${joinPoint.target}")
            }.doFinally {
                log.info("End ${joinPoint.target}")
            }.doOnError {
                log.error("${it.message}", it)
            }
        }
        return proceeded
    }
}