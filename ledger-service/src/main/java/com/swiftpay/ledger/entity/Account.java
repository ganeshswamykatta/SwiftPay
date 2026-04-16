package com.swiftpay.ledger.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Account {

    @Id
    private Long id;

    private Double balance;
}