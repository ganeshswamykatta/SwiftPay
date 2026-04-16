package com.swiftpay.gateway.kafka;

import com.swiftpay.gateway.dto.PaymentRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPaymentEvent(PaymentRequest request) {
        kafkaTemplate.send("payment-events", request);
    }
}