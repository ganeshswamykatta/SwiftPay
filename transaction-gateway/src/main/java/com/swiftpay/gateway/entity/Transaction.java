package com.swiftpay.gateway.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String transactionId; // NEW

    private Long fromAccount;
    private Long toAccount;
    private Double amount;
    private String status;
}