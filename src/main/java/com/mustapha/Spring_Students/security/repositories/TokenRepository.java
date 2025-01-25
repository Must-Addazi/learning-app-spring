package com.mustapha.Spring_Students.security.repositories;

import com.mustapha.Spring_Students.security.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);
}
