package com.example.yeebank.common.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Repository
public class LockRedisRepository {
// - Properties
    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> unlockScript;

// - Methods
    // - Constructor
    public LockRedisRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;

        this.unlockScript = new DefaultRedisScript<>();
        this.unlockScript.setScriptText(
                "if redis.call('get', KEY[1]) == ARGV[1] then " +
                " return redis.call('del', KEYS[1]) " +
                "else " +
                " return 0 " +
                "end"
        );
        this.unlockScript.setResultType(Long.class);
    }

    public boolean tryLock(String key, String token, Duration ttl) {
        Boolean ok = redisTemplate.opsForValue().setIfAbsent(key, token, ttl);
        return Boolean.TRUE.equals(ok);
    }

    public boolean unlock(String key, String token) {
        Long result = redisTemplate.execute(unlockScript, List.of(key), token);
        return Objects.equals(result, 1L);
    }
}
