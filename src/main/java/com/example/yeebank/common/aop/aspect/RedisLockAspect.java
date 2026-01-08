package com.example.yeebank.common.aop.aspect;

import com.example.yeebank.common.aop.annotation.RedisLock;
import com.example.yeebank.common.exception.CustomException;
import com.example.yeebank.common.exception.ErrorCode;
import com.example.yeebank.common.redis.RedisLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
public class RedisLockAspect {
// - Properties
    private final RedisLockService lockService;

// - Methods
    @Around("@annotation(redisLock)")
    public Object run(ProceedingJoinPoint joinPoint,
                      RedisLock redisLock) throws Throwable {

        String keyPreFix = redisLock.key();
        String value = UUID.randomUUID().toString();
        long timeoutSeconds = redisLock.timeout();

        Object[] args = joinPoint.getArgs();

        // - Get accountId
        List<Long> accountIdsToLock = new ArrayList<>();
        accountIdsToLock.add(castToLong(args[0], "First Parameter(accountId)"));

        boolean isRemittnace = joinPoint.getSignature().getName().equalsIgnoreCase("Remittance");
        if (isRemittnace) {
            // - Locked toAccountId
            accountIdsToLock.add(castToLong(args[1], "Second Parameter(toAccountId)"));
        }

        // - Prevent DeadLock
        List<String> keys = accountIdsToLock.stream()
                .sorted(Comparator.naturalOrder())
                .map(id -> keyPreFix + id)
                .toList();

        List<String> acquiredKeys = new ArrayList<>();

        try {
            // - tryLock
            long deadlinNanos = System.nanoTime() + timeoutSeconds * 1_000_000_000L;

            for (String key : keys) {
                boolean locked = tryLockWithRetry(key, value, timeoutSeconds, deadlinNanos);
                if (!locked) {
                    log.info("Get Lock Failed key = {} thread = {}", key, Thread.currentThread().getName());
                    throw new CustomException(ErrorCode.LOCK_EXIST_REQUEST);
                }
                acquiredKeys.add(key);
            }

            log.info("Get Lock Success keys = {} thread = {}", keys, Thread.currentThread().getName());

            // - Run Business
            return joinPoint.proceed();
        } finally {
            // - Unlock
            for (int i = acquiredKeys.size() - 1; i >= 0; i--) {
                lockService.unlock(acquiredKeys.get(i), value);
            }
            if (!acquiredKeys.isEmpty()) {
                log.info("Unlock keys = {} thread = {}", acquiredKeys, Thread.currentThread().getName());
            }
        }
    }

    private Boolean tryLockWithRetry(String key, String value, long ttlSeconds, long deadlineNanos) {
        while (System.nanoTime() < deadlineNanos) {
            Boolean ok = lockService.tryLock(key, value, ttlSeconds);
            if (Boolean.TRUE.equals(ok)) {
                return true;
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    private Long castToLong(Object value, String name) {
        if (!(value instanceof  Long)) {
            throw new CustomException(ErrorCode.LOCK_ID_BADREQUEST);
        }
        return (Long) value;
    }
}
