package com.sndp.agil.backend.repository;

import com.sndp.agil.backend.model.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;


public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, String> {
    boolean existsByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM BlacklistedToken b WHERE b.expirationDate < :now")
    void deleteAllExpiredSince(Date now);
}