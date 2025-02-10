package com.rasikhoons.cryptoclub.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {

    private String transferredBy;
    private BigDecimal amountTransferred = BigDecimal.ZERO;
    private String currency;
    private Long coinId;
    private BigDecimal coinQty;
    private String userIdBy;
    private String transactionType;
    private String transactionStatus;

}

