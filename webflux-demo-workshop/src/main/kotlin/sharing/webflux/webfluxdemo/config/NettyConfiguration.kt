package sharing.webflux.webfluxdemo.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
import org.springframework.boot.web.server.WebServer
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.reactive.ContextPathCompositeHandler
import org.springframework.http.server.reactive.HttpHandler

@Configuration
class NettyConfiguration: NettyReactiveWebServerFactory() {

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath:String

    override fun getWebServer(httpHandler: HttpHandler?): WebServer {
        return mapOf(
            contextPath to httpHandler,
            "/" to httpHandler
        ).let {
            super.getWebServer(ContextPathCompositeHandler(it))
        }
    }
}