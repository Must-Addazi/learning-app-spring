package com.mustapha.Spring_Students.security.repositories;

import com.mustapha.Spring_Students.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,String> {
    AppUser findByUsername(String username);
}
