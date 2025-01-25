package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.dtos.ResponsibleProgramDTO;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.ResponsibleProgramNotFoundException;
import com.mustapha.Spring_Students.service.ProgramService;
import com.mustapha.Spring_Students.service.RespoProgramService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class RespoProgramRestController {
    private RespoProgramService respoProgramService;
    private ProgramService programService;
    @GetMapping("/respo")
    public List<ResponsibleProgramDTO> responsibleProgramDTOs(){
        return respoProgramService.getResposProgram();
    }
    @GetMapping("/getProgramByRespo/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ProgramDTO programByRespo(@PathVariable(name = "id") String id) throws ProgramNotFoundException, ResponsibleProgramNotFoundException {
        return programService.getProgramByRespo(id);
    }
    @GetMapping("/respo/{id}")
    public ResponsibleProgramDTO responsibleProgramDTO(@PathVariable String id) throws ResponsibleProgramNotFoundException {
        return respoProgramService.getRespoProgram(id);
    }
    @PostMapping("/saveRespo")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_SUPER_ADMIN')")
    public String saveResponsibleProgram(ResponsibleProgramDTO responsibleProgramDTO){
        return respoProgramService.saveRespoProgram(responsibleProgramDTO).getId();
    }
}
