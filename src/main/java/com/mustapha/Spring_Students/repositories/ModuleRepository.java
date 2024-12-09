package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.CModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<CModule,String> {

}
