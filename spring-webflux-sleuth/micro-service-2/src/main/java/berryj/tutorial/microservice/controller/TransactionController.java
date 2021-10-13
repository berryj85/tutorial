package berryj.tutorial.microservice.controller;

import berryj.tutorial.microservice.domain.response.TransactionResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;


@RestController
@RequestMapping(value = "/micro-service-2/v1/data", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {
    private static final Logger logger = LogManager.getLogger(TransactionController.class);

    @GetMapping("/{transaction-id}")
    public Mono<TransactionResponse> getTransactionByTransactionId(@PathVariable("transaction-id") String transactionId) {
        return Mono.<TransactionResponse>create(monoSink -> monoSink.success(TransactionResponse.builder().transactionId(transactionId).transactionDate(new Date()).build()))
                .doOnSubscribe(subscription -> logger.info("Request transaction [{}]", transactionId))
                .doOnSuccess(response -> logger.info("Response transaction [{}]", response));
    }
}
