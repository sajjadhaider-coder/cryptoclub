package com.rasikhoons.cryptoclub.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private long id;
    private String transferredBy;
    private BigDecimal amountTransferred = BigDecimal.ZERO;
    private LocalDateTime transferredAt;
    private String currency;
}
