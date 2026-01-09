package com.example.yeebank.common.aop.aspect;

import com.example.yeebank.common.aop.annotation.RedisLock;
import com.example.yeebank.common.exception.CustomException;
import com.example.yeebank.common.exception.ErrorCode;
import com.example.yeebank.common.redis.RedisLockService;
import com.example.yeebank.domain.transfer.enums.TransferStatus;
import com.example.yeebank.domain.transfer.service.TransferService;
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
    private final TransferService transferService;

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

        for (String key : keys) {
            Boolean ok = lockService.tryLock(key, value, timeoutSeconds);
            if (!Boolean.TRUE.equals(ok)) {
                recordLockFail(targetMethod.getName(), joinPoint.getArgs());
                throw new CustomException(ErrorCode.LOCK_EXIST_REQUEST);
            }
        }

        return joinPoint.proceed();

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

    private void recordLockFail(String methodName, Object[] args) {
        String reason = "요청이 이미 실행중 입니다.";

        if (methodName.equalsIgnoreCase("withdrawal")) {
            Long fromAccountid = (Long) args[0];
            Long amount = (Long) args[1];
            transferService.withdrawal(fromAccountid, amount, TransferStatus.FAIL, reason);
        }

        if (methodName.equalsIgnoreCase("deposit")) {
            Long toAccountId = (Long) args[0];
            Long amount = (Long) args[1];
            transferService.deposit(toAccountId, amount, TransferStatus.FAIL, reason);
        }
    }

    private Long castToLong(Object value, String name) {
        if (!(value instanceof Long)) throw new CustomException(ErrorCode.LOCK_ID_BADREQUEST);
        return (Long) value;
    }
}
