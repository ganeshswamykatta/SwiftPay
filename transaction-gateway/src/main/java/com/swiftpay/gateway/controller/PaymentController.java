package com.swiftpay.gateway.controller;

import com.swiftpay.gateway.dto.PaymentRequest;
import com.swiftpay.gateway.dto.PaymentResponse;
import com.swiftpay.gateway.entity.Transaction;
import com.swiftpay.gateway.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/makePayment")
    public ResponseEntity<PaymentResponse> makePayment(@Valid @RequestBody PaymentRequest request) {

        request.setTransactionId(service.generateTransactionId());
        service.processPayment(request);

        PaymentResponse response = new PaymentResponse(
                "INITIATED",
                "Payment request accepted and is being processed",
                request.getTransactionId(),
                request.getAmount()
        );

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{transactionId}")
    public PaymentResponse getTransaction(@PathVariable("transactionId") String transactionId) {

        Transaction txn = service.getTransaction(transactionId);

        return new PaymentResponse(
                txn.getStatus(),
                "Transaction fetched successfully",
                txn.getTransactionId(),
                txn.getAmount()
        );
    }

    @GetMapping("/history/{userId}")
    public List<Transaction> getTransactionHistory(@PathVariable("userId") Long userId) {
        return service.getTransactionHistory(userId);
    }
}