package berryj.tutorial.microservice.filter;

import berryj.tutorial.microservice.constant.TracingContant;
import brave.baggage.BaggageField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TracingFilter implements WebFilter {

    @Autowired
    @Qualifier("correlation-baggage")
    private BaggageField correlationBaggageField;

    @Autowired
    @Qualifier("clientIp-baggage")
    private BaggageField clientIpBaggageField;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .doOnSubscribe(subscription -> {
                    correlationBaggageField.updateValue(this.getCorrelationId(exchange.getRequest()));
                    clientIpBaggageField.updateValue(this.getClientIp(exchange.getRequest()));
                });
    }

    private String getClientIp(ServerHttpRequest request) {
        if (request.getHeaders().containsKey(TracingContant.HEADER_FORWARD_FOR) && !request.getHeaders().get(TracingContant.HEADER_FORWARD_FOR).isEmpty()) {
            return request.getHeaders().get(TracingContant.HEADER_FORWARD_FOR).get(0);
        } else {
            return request.getRemoteAddress().getAddress().getHostAddress();
        }
    }

    private String getCorrelationId(ServerHttpRequest request) {
        Optional<String> correlationId = Optional.empty();
        if (request.getHeaders().containsKey(TracingContant.HEADER_CORRELATION_ID) && !request.getHeaders().get(TracingContant.HEADER_CORRELATION_ID).isEmpty()) {
            correlationId = Optional.ofNullable(request.getHeaders().get(TracingContant.HEADER_CORRELATION_ID).get(0));
        }
        return correlationId.orElse("MICRO-1-" + UUID.randomUUID().toString().replace("-", ""));
    }
}
