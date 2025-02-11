package com.rasikhoons.cryptoclub.Repository;

import com.rasikhoons.cryptoclub.model.Coins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinsRepo extends JpaRepository<Coins, Long> {

    Coins findByCoinsName(String coinsName);
}
