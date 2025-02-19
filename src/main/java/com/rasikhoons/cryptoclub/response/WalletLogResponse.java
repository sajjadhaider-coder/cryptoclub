package com.rasikhoons.cryptoclub.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletLogResponse {
    private long id;
    private Long walletId;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
