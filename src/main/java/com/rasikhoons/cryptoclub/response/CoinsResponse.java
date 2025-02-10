package com.rasikhoons.cryptoclub.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinsResponse {
    private long id;
    private String coinsName;
    private BigDecimal currentPrice;
}
