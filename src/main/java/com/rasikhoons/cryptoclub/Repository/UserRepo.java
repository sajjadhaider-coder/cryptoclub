package com.rasikhoons.cryptoclub.Repository;

import com.rasikhoons.cryptoclub.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserInfo, Long> {
    UserInfo findByUserName(String username);
}
