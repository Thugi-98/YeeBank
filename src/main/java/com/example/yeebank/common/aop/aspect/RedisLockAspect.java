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
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
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

    private final RedisLockService lockService;

    // ✅ 포인트컷 변수 바인딩 제거: @annotation(redisLock) -> @annotation(풀패키지)
    @Around("@annotation(com.example.yeebank.common.aop.annotation.RedisLock)")
    public Object run(ProceedingJoinPoint joinPoint) throws Throwable {

        // 1) 실제 타깃 메서드에서 @RedisLock 읽기
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Method method = sig.getMethod();
        Method targetMethod = AopUtils.getMostSpecificMethod(method, joinPoint.getTarget().getClass());

        RedisLock redisLock = AnnotationUtils.findAnnotation(targetMethod, RedisLock.class);
        if (redisLock == null) {
            // 이 케이스면 포인트컷이 잘못 매칭된 것
            return joinPoint.proceed();
        }

        String keyPreFix = redisLock.key();
        String value = UUID.randomUUID().toString();
        long timeoutSeconds = redisLock.timeout();

        Object[] args = joinPoint.getArgs();

        // - Get accountId
        List<Long> accountIdsToLock = new ArrayList<>();
        accountIdsToLock.add(castToLong(args[0], "First Parameter(accountId)"));

        boolean isRemittance = targetMethod.getName().equalsIgnoreCase("remittance");
        if (isRemittance) {
            accountIdsToLock.add(castToLong(args[1], "Second Parameter(toAccountId)"));
        }

        // - Prevent DeadLock
        List<String> keys = accountIdsToLock.stream()
                .sorted(Comparator.naturalOrder())
                .map(id -> keyPreFix + id)
                .toList();

        List<String> acquiredKeys = new ArrayList<>();

        try {
            long deadlinNanos = System.nanoTime() + timeoutSeconds * 1_000_000_000L;

            for (String key : keys) {
                boolean locked = tryLockWithRetry(key, value, timeoutSeconds, deadlinNanos);
                if (!locked) {
                    throw new CustomException(ErrorCode.LOCK_EXIST_REQUEST);
                }
                acquiredKeys.add(key);
            }

            return joinPoint.proceed();
        } finally {
            for (int i = acquiredKeys.size() - 1; i >= 0; i--) {
                lockService.unlock(acquiredKeys.get(i), value);
            }
        }
    }

    private boolean tryLockWithRetry(String key, String value, long ttlSeconds, long deadlineNanos) {
        while (System.nanoTime() < deadlineNanos) {
            Boolean ok = lockService.tryLock(key, value, ttlSeconds);
            if (Boolean.TRUE.equals(ok)) return true;
            try { Thread.sleep(30); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); return false; }
        }
        return false;
    }

    private Long castToLong(Object value, String name) {
        if (!(value instanceof Long)) throw new CustomException(ErrorCode.LOCK_ID_BADREQUEST);
        return (Long) value;
    }
}
