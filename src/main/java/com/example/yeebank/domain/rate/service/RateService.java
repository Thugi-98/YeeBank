package com.example.yeebank.domain.rate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RateService {

    private final RestTemplate restTemplate;

    @Cacheable(value = "rateCache")
    public String getRequest() {
        String apiUrl = "https://api.exchangerate.host/live?access_key=eed12fb49aea8aec09a8eb53396f4d6b&currencies=USD,KRW&format=1";

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        String responseBody = response.getBody();

        return responseBody;

    }

}
