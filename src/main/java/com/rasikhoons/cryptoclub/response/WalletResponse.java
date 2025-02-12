package com.rasikhoons.cryptoclub.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletResponse {

    private long id;
    private Long userId;
    private BigDecimal balance;
    private String transferAmount; // image
    private LocalDateTime lastedUpdated;
}
