package com.rasikhoons.cryptoclub.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {

    private String transferredBy;
    private BigDecimal amountTransferred = BigDecimal.ZERO;
    private String currency;
}

