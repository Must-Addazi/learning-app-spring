package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program,String> {
}
