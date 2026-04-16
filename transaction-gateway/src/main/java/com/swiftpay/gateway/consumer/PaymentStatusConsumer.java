package com.swiftpay.gateway.consumer;

import com.swiftpay.gateway.entity.Transaction;
import com.swiftpay.gateway.repository.TransactionRepository;
import com.swiftpay.gateway.dto.PaymentStatusEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentStatusConsumer {

    private final TransactionRepository repository;

    public PaymentStatusConsumer(TransactionRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "payment-status", groupId = "gateway-group")
    public void consume(PaymentStatusEvent event) {

        try {
            Transaction txn = repository.findByTransactionId(event.getTransactionId())
                    .orElseThrow();

            txn.setStatus(event.getStatus());
            repository.save(txn);

            System.out.println("✅ Updated status: " + event.getStatus());

        } catch (Exception e) {
            System.err.println("❌ Error processing message: " + e.getMessage());
        }
    }
}