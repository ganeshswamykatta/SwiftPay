package com.swiftpay.ledger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusEvent {
    private String transactionId;
    private String status; // SUCCESS / FAILED
}