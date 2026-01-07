package com.example.yeebank.common.redis;

import com.example.yeebank.common.exception.CustomException;
import com.example.yeebank.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class LockService {
// - Properties
    private final LockRedisRepository lockRedisRepository;

// - Methods
    public <T> T withLock(String key, Duration ttl, Duration waitTime, Supplier<T> action) {
        String token = UUID.randomUUID().toString();
        long deadline = System.nanoTime() + waitTime.toNanos();

        while (System.nanoTime() < deadline) {
            if (lockRedisRepository.tryLock(key, token, ttl)) {
                try {
                    return action.get();
                } finally {
                    lockRedisRepository.unlock(key, token);
                }
            }
            sleepBackoff();
        }

        throw new CustomException(ErrorCode.REDIS_LOCK_FAIL);
    }

    public <T> T withLocks(List<String> keys, Duration ttl, Duration waitTime, Supplier<T> action) {
        List<String> sorted = keys.stream()
                .sorted(Comparator.naturalOrder())
                .toList();

        String token = UUID.randomUUID().toString();
        long deadline = System.nanoTime() + waitTime.toNanos();

        while (System.nanoTime() < deadline) {
            List<String> acquired = new ArrayList<>();
            boolean allAcquired = true;

            for (String key : sorted) {
                if (lockRedisRepository.tryLock(key, token, ttl)) {
                    acquired.add(key);
                } else {
                    allAcquired = false;
                    break;
                }
            }

            if (allAcquired) {
                try {
                    return action.get();
                } finally {
                    for (int i = acquired.size() - 1; i >= 0; i--) {
                        lockRedisRepository.unlock(acquired.get(i), token);
                    }
                }
            }

            // 부분 획득했으면 즉시 해제 후 재시도
            for (int i = acquired.size() - 1; i >= 0; i--) {
                lockRedisRepository.unlock(acquired.get(i), token);
            }

            sleepBackoff();
        }

        throw new CustomException(ErrorCode.REDIS_LOCKS_FAIL);
    }

    private void sleepBackoff() {
        try {
            long millis = ThreadLocalRandom.current().nextLong(20, 60);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
