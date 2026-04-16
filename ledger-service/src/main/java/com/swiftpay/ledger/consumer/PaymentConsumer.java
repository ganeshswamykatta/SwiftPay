package com.swiftpay.ledger.consumer;

import com.swiftpay.ledger.dto.PaymentEvent;
import com.swiftpay.ledger.service.LedgerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    private final LedgerService ledgerService;

    public PaymentConsumer(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }


    @KafkaListener(topics = "payment-events", groupId = "ledger-group")
    public void consume(PaymentEvent event) {

        try {
            ledgerService.processPayment(event);
        } catch (Exception e) {
            System.err.println("❌ Failed to process message: " + e.getMessage());
            // do NOT throw → avoids infinite retry
        }
    }
}