package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.ResponsibleProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsibleProgramRepository extends JpaRepository<ResponsibleProgram,String> {
    ResponsibleProgram findByEmail(String email);
}
