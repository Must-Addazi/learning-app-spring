package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ModuleDTO;
import com.mustapha.Spring_Students.exceptions.ModuleNotFoundException;


import java.util.List;

public interface ModuleService {
    List<ModuleDTO> getModuleList();
    ModuleDTO getModule(String id) throws ModuleNotFoundException;
    ModuleDTO saveModule(ModuleDTO moduleDTO);
    ModuleDTO updateModule(String id,ModuleDTO moduleDTO);
    void deleteModule(String id);
}
