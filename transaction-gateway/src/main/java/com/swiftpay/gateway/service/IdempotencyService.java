package com.swiftpay.gateway.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

    private final StringRedisTemplate redisTemplate;

    public IdempotencyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isDuplicate(String transactionId) {
        Boolean exists = redisTemplate.hasKey(transactionId);
        return exists != null && exists;
    }

    public void markProcessed(String transactionId) {
        redisTemplate.opsForValue()
                .set(transactionId, "processed", Duration.ofMinutes(10));
    }
}