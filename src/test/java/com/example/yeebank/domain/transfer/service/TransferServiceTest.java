package com.example.yeebank.domain.transfer.service;

import com.example.yeebank.domain.account.entity.Account;
import com.example.yeebank.domain.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class TransferServiceTest {
// - Properties
    private final AccountRepository accountRepository;
    private final TransferService transferService;

// - Methods
    // - Constructor
    TransferServiceTest(AccountRepository accountRepository,
                    TransferService transferService) {
        this.accountRepository = accountRepository;
        this.transferService = transferService;
    }

    @Test
    @DisplayName("락 없이 동시 출금 시 비정상적인 출금 발생")
    void withdrawal() throws Exception {
        // - Given
        Account account = new Account();
        account.setBalance(100_000L);
        Long accountId = accountRepository.save(account).getId();

        Integer threadCount = 200;
        Long amount = 1_000L;

        ExecutorService executor = Executors.newFixedThreadPool(32);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        CountDownLatch done = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        // - When
        for (int i = 0; i < threadCount; i++ ) {
            executor.submit(() -> {
                try {
                    barrier.await(); // - 동시 실행
                    transferService.Withdrawal(accountId, amount);
                    success.incrementAndGet();
                } catch (BrokenBarrierException | InterruptedException e) {
                    Thread.currentThread().interrupt();
                    fail.incrementAndGet();
                } catch (Exception e) {
                    fail.incrementAndGet();
                } finally {
                    done.countDown();
                }
            });
        }

        done.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        Account after = accountRepository.findById(account.getId()).orElseThrow();

        // - Then
        assertThat(success.get()).isEqualTo(100);
        assertThat(fail.get()).isEqualTo(100);
        assertThat(after.getBalance()).isEqualTo(0L);
    }
}