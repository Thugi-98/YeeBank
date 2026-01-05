package com.example.yeebank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class YeeBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(YeeBankApplication.class, args);
    }

}
