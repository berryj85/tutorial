package berryj.tutorial.microservice.controller;

import berryj.tutorial.microservice.domain.response.TransactionResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value = "/micro-service-1/v1/data", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {
    private static final Logger logger = LogManager.getLogger(TransactionController.class);
    @Autowired
    private WebClient microServiceWebClient;

    @GetMapping("/{transaction-id}")
    public Mono<TransactionResponse> getTransactionByTransactionId(@PathVariable("transaction-id") String transactionId) {
        return microServiceWebClient.get().uri("/v1/data/" + transactionId).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(TransactionResponse.class)
                .doOnSubscribe(subscription -> logger.info("Client Request transaction [{}]", transactionId))
                .doOnSuccess(response -> logger.info("Client Response transaction [{}]", response));
    }
}
