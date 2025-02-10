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
@Table(name = "Transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "transferredBy")
    private String transferredBy;

    @Column(name = "amountTransferred", columnDefinition = "Decimal(10, 2)")
    private BigDecimal amountTransferred = BigDecimal.ZERO;

    @Column(name = "transferredAt")
    private LocalDateTime transferredAt;

    @Column(name = "currency")
    private String currency;

    @Column(name = "coinId")
    private Long coinId;

    @Column(name = "coinQty", columnDefinition = "Decimal(10, 2)")
    private BigDecimal coinQty = BigDecimal.ZERO;

    @Column(name = "userIdBy")
    private String userIdBy;

    @Column(name = "transactionType")
    private String transactionType;

    @Column(name = "transactionStatus")
    private String transactionStatus;

    @PrePersist
    public void prePersist() {
        if (transferredAt == null) {
            transferredAt = LocalDateTime.now();  // Set the current timestamp when the entity is persisted
        }
    }
}
