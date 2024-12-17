package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ModuleDTO;
import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.exceptions.ModuleNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;


import java.util.List;

public interface ModuleService {
    List<ModuleDTO> getModuleList();
    ModuleDTO getModule(String id) throws ModuleNotFoundException;
    ModuleDTO saveModule(ModuleDTO moduleDTO);
    ModuleDTO updateModule(String id,ModuleDTO moduleDTO);
    void deleteModule(String id);
    List<ModuleDTO> getModuleByProgram(String programId) throws ProgramNotFoundException;
}
