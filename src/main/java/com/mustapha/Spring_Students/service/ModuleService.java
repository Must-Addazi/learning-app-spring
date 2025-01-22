package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ModuleDTO;
import com.mustapha.Spring_Students.dtos.NewModuleDTO;
import com.mustapha.Spring_Students.dtos.NewProgram;
import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.exceptions.ModuleNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;


import java.util.List;

public interface ModuleService {
    List<ModuleDTO> getModuleList();
    ModuleDTO getModule(String id) throws ModuleNotFoundException;
    ModuleDTO saveModule(NewModuleDTO moduleDTO);
    ModuleDTO updateModule(ModuleDTO moduleDTO);
    Boolean deleteModule(String id);
    List<ModuleDTO> getModuleByProgram(String programId) throws ProgramNotFoundException;

}
