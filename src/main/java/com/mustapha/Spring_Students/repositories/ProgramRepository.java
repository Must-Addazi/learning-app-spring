package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.entities.ResponsibleProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program,String> {
    Program findByResponsibleProgram(ResponsibleProgram responsibleProgram);
}
