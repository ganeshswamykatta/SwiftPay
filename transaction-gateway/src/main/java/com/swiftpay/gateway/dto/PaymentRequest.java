package com.swiftpay.gateway.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class PaymentRequest {
//    @NotNull
    private String transactionId;

    @NotNull
    private Long fromAccount;

    @NotNull
    private Long toAccount;

    @NotNull
    @Positive
    private Double amount;
}