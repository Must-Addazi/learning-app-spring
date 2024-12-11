package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;

import java.util.List;

public interface ProgramService {
    ProgramDTO getProgram(String id) throws ProgramNotFoundException;
    ProgramDTO saveProgram(ProgramDTO programDTO);
    List<ProgramDTO> getPrograms();
}
