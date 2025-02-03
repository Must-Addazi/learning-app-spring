package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.ProgramPoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProgramPosterRepository extends JpaRepository<ProgramPoster,Long> {

}
