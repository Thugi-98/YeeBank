package com.example.yeebank.domain.rate.controller;

import com.example.yeebank.domain.rate.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    @GetMapping
    public String getRatesApi() {
        String result = rateService.getRequest();

        return result;
    }
}
