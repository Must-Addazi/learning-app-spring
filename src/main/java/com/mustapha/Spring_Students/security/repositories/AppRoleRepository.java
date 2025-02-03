package com.mustapha.Spring_Students.security.repositories;

import com.mustapha.Spring_Students.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole,String> {
}
