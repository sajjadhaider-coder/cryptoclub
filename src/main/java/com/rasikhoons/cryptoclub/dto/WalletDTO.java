package com.rasikhoons.cryptoclub.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class WalletDTO {

    private long id;
    private Long userId;
    private BigDecimal balance;
    private MultipartFile file; // image
}
