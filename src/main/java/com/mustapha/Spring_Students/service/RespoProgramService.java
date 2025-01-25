package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ResponsibleProgramDTO;
import com.mustapha.Spring_Students.entities.ResponsibleProgram;
import com.mustapha.Spring_Students.exceptions.ResponsibleProgramNotFoundException;

import java.util.List;

public interface RespoProgramService {
    ResponsibleProgramDTO getRespoProgram(String id) throws ResponsibleProgramNotFoundException;
    ResponsibleProgram getRespoProgramByEmail(String id) throws ResponsibleProgramNotFoundException;
    ResponsibleProgramDTO saveRespoProgram(ResponsibleProgramDTO responsibleProgramDTO);
    List<ResponsibleProgramDTO> getResposProgram();
    void deleteRespoProgram(String id);
    ResponsibleProgramDTO updateRespo(String id,ResponsibleProgramDTO responsibleProgramDTO);

}
