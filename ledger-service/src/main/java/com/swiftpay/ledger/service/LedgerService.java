package com.swiftpay.ledger.service;

import com.swiftpay.ledger.dto.PaymentEvent;

import com.swiftpay.ledger.dto.PaymentStatusEvent;
import com.swiftpay.ledger.entity.Account;
import com.swiftpay.ledger.repository.AccountRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LedgerService {

    private final AccountRepository repository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public LedgerService(AccountRepository repository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void processPayment(PaymentEvent request) {

        try {
            Account from = repository.findById(request.getFromAccount())
                    .orElseThrow(() -> new RuntimeException("From account not found"));

            Account to = repository.findById(request.getToAccount())
                    .orElseThrow(() -> new RuntimeException("To account not found"));

            if (from.getBalance() < request.getAmount()) {
                throw new RuntimeException("Insufficient balance");
            }

            from.setBalance(from.getBalance() - request.getAmount());
            to.setBalance(to.getBalance() + request.getAmount());

            repository.save(from);
            repository.save(to);

            // ✅ SEND SUCCESS EVENT
            kafkaTemplate.send("payment-status",
                    new PaymentStatusEvent(request.getTransactionId(), "SUCCESS"));

        } catch (Exception e) {

            // ❌ SEND FAILURE EVENT
            kafkaTemplate.send("payment-status",
                    new PaymentStatusEvent(request.getTransactionId(), "FAILED"));

            throw e;
        }
    }
}