package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
}
