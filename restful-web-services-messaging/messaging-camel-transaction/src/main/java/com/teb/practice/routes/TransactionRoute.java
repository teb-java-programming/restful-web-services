package com.teb.practice.routes;

import static org.apache.commons.lang3.StringUtils.abbreviate;

import com.teb.practice.exception.TransactionException;
import com.teb.practice.model.TransactionEvent;
import com.teb.practice.model.TransactionRequest;
import com.teb.practice.model.TransactionResponse;
import com.teb.practice.service.AccountService;

import lombok.RequiredArgsConstructor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionRoute extends RouteBuilder {

    private static final String STATUS_SUCCESS = "SUCCESS";

    private final AccountService accountService;

    @Override
    public void configure() {

        onException(TransactionException.class)
                .handled(true)
                .log("Error: ${exception.message}")
                .setBody(
                        constant(
                                TransactionResponse.builder()
                                        .transactionId(null)
                                        .status("FAILED")
                                        .message("Transaction failed")
                                        .balance(0.0)
                                        .build()));

        from("direct:transaction")
                .routeId("transaction-route")
                .log("Received request: ${body}")
                .to("direct:enrich")
                .to("direct:validate")
                .to("direct:process")
                .multicast()
                .to("direct:audit", "direct:notify")
                .end();

        // Enrichment
        from("direct:enrich")
                .routeId("enrich-route")
                .process(
                        exchange -> {
                            TransactionRequest request =
                                    exchange.getIn().getBody(TransactionRequest.class);
                            exchange.getMessage()
                                    .setBody(
                                            TransactionEvent.builder()
                                                    .type(request.getType())
                                                    .amount(request.getAmount())
                                                    .build());
                        });

        // Camel validation
        from("direct:validate")
                .routeId("validate-route")
                .process(
                        exchange -> {
                            if (exchange.getIn().getBody(TransactionEvent.class).getAmount() <= 0) {
                                throw new TransactionException(
                                        "Deposit/Withdrawal amount must be greater than 0");
                            }
                        });

        // Business processing
        from("direct:process")
                .routeId("process-route")
                .choice()
                .when(simple("${body.type} == 'DEPOSIT'"))
                .process(
                        exchange -> {
                            TransactionEvent event =
                                    exchange.getIn().getBody(TransactionEvent.class);
                            exchange.getMessage()
                                    .setBody(
                                            TransactionResponse.builder()
                                                    .transactionId(
                                                            abbreviate(
                                                                    event.getTransactionId(), 12))
                                                    .status(STATUS_SUCCESS)
                                                    .message("Deposit successful")
                                                    .balance(
                                                            accountService.deposit(
                                                                    event.getAmount()))
                                                    .build());
                        })
                .when(simple("${body.type} == 'WITHDRAWAL'"))
                .process(
                        exchange -> {
                            TransactionEvent event =
                                    exchange.getIn().getBody(TransactionEvent.class);
                            exchange.getMessage()
                                    .setBody(
                                            TransactionResponse.builder()
                                                    .transactionId(
                                                            abbreviate(
                                                                    event.getTransactionId(), 12))
                                                    .status(STATUS_SUCCESS)
                                                    .message("Withdrawal successful")
                                                    .balance(
                                                            accountService.withdraw(
                                                                    event.getAmount()))
                                                    .build());
                        })
                .otherwise()
                .throwException(new TransactionException("Invalid transaction type"))
                .end()
                .log("Transaction processed: ${body}");

        from("direct:audit").routeId("audit-route").log("Audit result: ${body}");

        from("direct:notify").routeId("notify-route").log("Notification sent");
    }
}
