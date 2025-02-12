package com.rasikhoons.cryptoclub.Repository;

import com.rasikhoons.cryptoclub.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, Long> {
}
