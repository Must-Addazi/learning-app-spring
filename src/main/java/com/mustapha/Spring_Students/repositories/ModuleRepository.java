package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.CModule;
import com.mustapha.Spring_Students.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ModuleRepository extends JpaRepository<CModule,String> {

    List<CModule> findByProgram(Program program);
}
