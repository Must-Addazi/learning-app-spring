package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.CModule;
import com.mustapha.Spring_Students.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<CModule,String> {
    List<CModule> findByProgram(Program program);
}
