package com.swiftpay.ledger.repository;

import com.swiftpay.ledger.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}