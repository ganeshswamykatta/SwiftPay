package com.swiftpay.ledger.kafka;

import com.swiftpay.ledger.dto.PaymentStatusEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPaymentStatus(String transactionId, String status) {

        PaymentStatusEvent event = new PaymentStatusEvent(transactionId, status);

        kafkaTemplate.send("payment-status", event);

        System.out.println("📤 Sent payment status: " + event);
    }
}