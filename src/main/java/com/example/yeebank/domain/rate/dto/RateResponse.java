package com.example.yeebank.domain.rate.dto;

import lombok.Getter;

@Getter
public class RateResponse {

    private final Long rate;

    public RateResponse(Long rate) {
        this.rate = rate;
    }
}
