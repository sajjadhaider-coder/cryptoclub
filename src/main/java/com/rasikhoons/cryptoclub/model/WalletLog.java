package com.rasikhoons.cryptoclub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WalletLog")
public class WalletLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "balance", columnDefinition = "Decimal(10,2)")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "lastedUpdated")
    private LocalDateTime createdAt;

    @Column(name = "walletId")
    private Long walletId;
}
