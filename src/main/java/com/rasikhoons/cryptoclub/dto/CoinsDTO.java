package com.rasikhoons.cryptoclub.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinsDTO {

    private long id;
    private String coinsName;
    private BigDecimal currentPrice;

}
