package com.swiftpay.gateway.repository;

import com.swiftpay.gateway.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(String transactionId);

    List<Transaction> findByFromAccountOrToAccount(Long senderId, Long receiverId);
}