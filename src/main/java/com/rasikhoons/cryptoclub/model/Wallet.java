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
@Table(name = "Wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "balance", columnDefinition = "Decimal(10,2)")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "transferAmount")
    private String transferAmount; // image

    @Column(name = "lastedUpdated")
    private LocalDateTime lastedUpdated;

    @PrePersist
    public void prePersist() {
        if (lastedUpdated == null) {
            lastedUpdated = LocalDateTime.now();  // Set the current timestamp when the entity is persisted
        }
    }
}
