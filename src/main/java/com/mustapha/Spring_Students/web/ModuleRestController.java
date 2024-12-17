package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.ModuleDTO;
import com.mustapha.Spring_Students.exceptions.ModuleNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.service.ModuleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class ModuleRestController {
    private ModuleService moduleService;
    @GetMapping("/modules")
    public List<ModuleDTO> getModules(){
        return moduleService.getModuleList();
    }
    @GetMapping("module/{id}")
    public ModuleDTO getModule(@PathVariable(name = "id") String id) throws ModuleNotFoundException {
        return moduleService.getModule(id);
    }
    @GetMapping("modules/{programId}")
    public List<ModuleDTO> getModules(@PathVariable(name = "programId") String id) throws ProgramNotFoundException {
        return moduleService.getModuleByProgram(id);
    }
}
