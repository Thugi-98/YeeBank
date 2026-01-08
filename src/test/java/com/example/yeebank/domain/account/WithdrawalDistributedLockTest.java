package com.example.yeebank.domain.account;

import com.example.yeebank.domain.account.entity.Account;
import com.example.yeebank.domain.account.repository.AccountRepository;
import com.example.yeebank.domain.account.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WithdrawalDistributedLockTest {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @Autowired
    WithdrawalDistributedLockTest(AccountService accountService,
                                  AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @AfterEach
    void cleanup() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Redis 분산락 적용 시 동시 출금에서도 초과 출금(정합성 깨짐)이 없어야 한다")
    void withdrawal_concurrency_with_redis_lock() throws Exception {
        // given
        Account account = new Account(1L, "1234567812345687", "1234", "테스트", 10_000L);
        Long accountId = accountRepository.save(account).getId();

        final int threadCount = 200;
        final long amount = 100L;

        final int expectedSuccess = (int) (10_000L / amount); // 100
        final int expectedFail = threadCount - expectedSuccess; // 100
        final long expectedFinalBalance = 0L;

        // 핵심: barrier(threadCount)를 쓸 거면 pool도 threadCount로 맞춰서 절대 멈추지 않게 한다.
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        CountDownLatch done = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
        Queue<Throwable> errors = new ConcurrentLinkedQueue<>();

        // 락이 "즉시 실패" 구현이어도 테스트가 흔들리지 않도록, 테스트에서 짧게 재시도한다.
        Duration maxTry = Duration.ofSeconds(5);

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    // 모두 모였다가 거의 동시에 출발
                    barrier.await(10, TimeUnit.SECONDS);

                    long deadline = System.nanoTime() + maxTry.toNanos();

                    while (true) {
                        try {
                            accountService.withdrawal(accountId, amount);
                            success.incrementAndGet();
                            break;
                        } catch (IllegalStateException lockFail) {
                            // 락 획득 실패(예: "Exist Request...")는 잠깐 쉬고 재시도
                            if (System.nanoTime() >= deadline) {
                                fail.incrementAndGet();
                                break;
                            }
                            Thread.sleep(10);
                        } catch (RuntimeException businessFail) {
                            // 잔액 부족 등 비즈니스 실패는 재시도하지 않고 실패로 처리
                            fail.incrementAndGet();
                            break;
                        }
                    }

                } catch (Throwable t) {
                    errors.add(t);
                    fail.incrementAndGet();
                } finally {
                    done.countDown();
                }
            });
        }

        boolean finished = done.await(60, TimeUnit.SECONDS);

        executor.shutdownNow();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // then
        // 1) 테스트 자체가 멈추지 않았는지(멈췄으면 barrier/스레드 문제)
        assertThat(finished)
                .as("스레드가 끝까지 종료되지 않았습니다. 첫 에러: %s",
                        errors.peek() == null ? "none" : errors.peek().toString())
                .isTrue();

        // 2) barrier 타임아웃/기타 예외가 없었는지
        assertThat(errors)
                .as("실행 중 예외가 발생했습니다. 예: %s",
                        errors.peek() == null ? "none" : errors.peek().toString())
                .isEmpty();

        // 3) 정합성 검증(핵심)
        Account after = accountRepository.findById(accountId).orElseThrow();

        assertThat(success.get()).isEqualTo(expectedSuccess);
        assertThat(fail.get()).isEqualTo(expectedFail);
        assertThat(after.getBalance()).isEqualTo(expectedFinalBalance);
    }
}
