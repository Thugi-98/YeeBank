package com.example.yeebank.common.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class Logging {

    @Pointcut("@annotation( com.example.yeebank.common.aop.annotation.LogStamp)")
    public void logPointCut() {

    }


    @Around("logPointCut()")
    public Object AopLog(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis(); // 시작 시간
        String status = "success"; // 메서드 성공 여부

        String methodName = joinPoint.getSignature().getName();

        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            status = "fail";
            throw e;
        } finally {
            long end = System.currentTimeMillis(); // 끝 시간
            log.info("[Log] 메서드 : {} , 결과 : {} , 소요시간 : {}ms", methodName, status, end - start); // 실행된 메서드와 소요시간
        }
    }
}