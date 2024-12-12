package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.service.ProgramService;
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
public class ProgramRestController {
    private ProgramService programService;
    @GetMapping("/program")
    public List<ProgramDTO> programDTOList(){
        return programService.getPrograms();
    }
    @GetMapping("/program/{id}")
    public ProgramDTO programDTO( @PathVariable(name = "id") String id) throws ProgramNotFoundException {
        return programService.getProgram(id);
    }
}
