package com.mustapha.Spring_Students.security.repositories;

import com.mustapha.Spring_Students.security.entities.AppUser;
import com.mustapha.Spring_Students.security.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);
    void deleteByAppUser(AppUser appUser);
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();
}
