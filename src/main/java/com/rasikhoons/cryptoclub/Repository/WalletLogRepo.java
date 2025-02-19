package com.rasikhoons.cryptoclub.Repository;

import com.rasikhoons.cryptoclub.model.WalletLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletLogRepo extends JpaRepository<WalletLog, Long> {
    void deleteAllByWalletId(Long id);

    List<WalletLog> findByWalletIdOrderByCreatedAtDesc(Long walletId);
}
