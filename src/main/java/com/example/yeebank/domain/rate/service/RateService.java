package com.example.yeebank.domain.rate.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RateService {

    private final RestTemplate restTemplate;

    @Cacheable(value = "rateCache")
    public BigDecimal getRates() {

        String key = "eed12fb49aea8aec09a8eb53396f4d6b";
        String url = String.format("https://api.exchangerate.host/live?access_key={}&currencies=USD,KRW&format=1", key);

        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode node = new ObjectMapper().readTree(response)
                    .path("quotes")
                    .path("USDKRW");

            return node.decimalValue();

        } catch (Exception e) {
            throw new IllegalArgumentException("환율 정보 파싱 실패");
        }
    }
}
