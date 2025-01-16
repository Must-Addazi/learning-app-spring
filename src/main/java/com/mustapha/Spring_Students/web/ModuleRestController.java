package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.ModuleDTO;
import com.mustapha.Spring_Students.dtos.NewModuleDTO;
import com.mustapha.Spring_Students.exceptions.ModuleNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.service.ModuleService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/saveModule")
    public ModuleDTO saveModule(@ModelAttribute NewModuleDTO newModuleDTO){
        return moduleService.saveModule(newModuleDTO);
    }
    @GetMapping("module/{id}")
    public ModuleDTO getModule(@PathVariable(name = "id") String id) throws ModuleNotFoundException {
        return moduleService.getModule(id);
    }
    @GetMapping("modules/{programId}")
    public List<ModuleDTO> getModules(@PathVariable(name = "programId") String id) throws ProgramNotFoundException {
        return moduleService.getModuleByProgram(id);
    }
    @DeleteMapping("/deleteModule/{id}")
    public boolean deletePayment(@PathVariable String id) {
        return moduleService.deleteModule(id);
    }
}
