package com.example.yeebank.common.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLockService {
// - Properties
    private final StringRedisTemplate redisTemplate;

// - Methods
    public Boolean tryLock(String key, String value, Long timeoutSeconds) {
        // - If exist redis key, return false else return true
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, value, Duration.ofSeconds(timeoutSeconds));

        return Boolean.TRUE.equals(result);
    }

    public void unlock(String key, String value) {
        // - Step1. key check
        // - Step2. value check
        String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "   return redis.call('del', KEYS[1]) " +
                        "else " +
                        "   return 0 " +
                        "end";

        redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(key),
                value
        );

        log.info("Execute Return Lock Key :: {}", Thread.currentThread().getName());
    }
}
