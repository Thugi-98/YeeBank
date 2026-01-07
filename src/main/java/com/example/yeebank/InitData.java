package com.example.yeebank;

import com.example.yeebank.domain.account.entity.Account;
import com.example.yeebank.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData implements ApplicationRunner {
// - Properties
    private final AccountRepository  accountRepository;

// - Methods
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account temp1 = new Account("383827271818", 1234L, "임시계좌1", 10000L);
        Account temp2 = new Account("383827271818", 1234L, "임시계좌2", 20000L);
        Account temp3 = new Account("383827271818", 1234L, "임시계좌3", 30000L);
        Account temp4 = new Account("383827271818", 1234L, "임시계좌4", 40000L);

        accountRepository.save(temp1);
        accountRepository.save(temp2);
        accountRepository.save(temp3);
        accountRepository.save(temp4);
    }
}
