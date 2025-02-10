package com.rasikhoons.cryptoclub.Repository;


import com.rasikhoons.cryptoclub.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transactions, Long> {
}
