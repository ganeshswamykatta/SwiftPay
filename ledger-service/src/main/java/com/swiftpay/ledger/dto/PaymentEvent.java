package com.swiftpay.ledger.dto;

import lombok.Data;

@Data
public class PaymentEvent {
    private String transactionId; // UNIQUE ID FROM CLIENT
    private Long fromAccount;
    private Long toAccount;
    private Double amount;
}