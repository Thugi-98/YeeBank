package com.example.yeebank.domain.rate.controller;

import com.example.yeebank.common.dto.CommonResponse;
import com.example.yeebank.domain.rate.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    @GetMapping
    public CommonResponse<Map<String, BigDecimal>> getRatesApi() {

        BigDecimal rate = rateService.getRates();

        Map<String, BigDecimal> data = Map.of(
                "USDKRW", rate
        );

        return new CommonResponse<>(true, "환율 조회 성공", data);
    }
}
