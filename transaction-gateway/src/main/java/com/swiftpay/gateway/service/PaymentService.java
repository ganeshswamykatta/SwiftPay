package com.swiftpay.gateway.service;

import com.swiftpay.gateway.dto.PaymentRequest;
import com.swiftpay.gateway.entity.Transaction;
import com.swiftpay.gateway.exception.CustomException;
import com.swiftpay.gateway.kafka.KafkaProducerService;
import com.swiftpay.gateway.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final TransactionRepository repository;
    private final KafkaProducerService kafkaProducer;
    private final IdempotencyService idempotencyService;

    public PaymentService(TransactionRepository repository,
                          KafkaProducerService kafkaProducer, IdempotencyService idempotencyService) {
        this.repository = repository;
        this.kafkaProducer = kafkaProducer;
        this.idempotencyService = idempotencyService;
    }

    public void processPayment(PaymentRequest request) {

        if (idempotencyService.isDuplicate(request.getTransactionId())) {
            throw new CustomException("Duplicate transaction");
        }

        Transaction txn = new Transaction();
        txn.setTransactionId(request.getTransactionId()); // NEW
        txn.setFromAccount(request.getFromAccount());
        txn.setToAccount(request.getToAccount());
        txn.setAmount(request.getAmount());

        txn.setStatus("INITIATED");
        repository.save(txn);

        kafkaProducer.sendPaymentEvent(request);

        txn.setStatus("PROCESSING"); // NEW
        repository.save(txn);

        idempotencyService.markProcessed(request.getTransactionId());
    }

    public Transaction getTransaction(String transactionId) {
        return repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public String generateTransactionId() {
        return "txn-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public List<Transaction> getTransactionHistory(Long userId) {
        return repository.findByFromAccountOrToAccount(userId, userId);
    }
}