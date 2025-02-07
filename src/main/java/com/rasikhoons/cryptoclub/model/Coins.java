package com.rasikhoons.cryptoclub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coins")
public class Coins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "coin_name")
    private String coinsName;

    @Column(name = "current_price", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal currentPrice = BigDecimal.ZERO;
}
