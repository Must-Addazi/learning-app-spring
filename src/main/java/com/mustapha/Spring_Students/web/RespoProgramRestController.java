package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.ResponsibleProgramDTO;
import com.mustapha.Spring_Students.exceptions.ResponsibleProgramNotFoundException;
import com.mustapha.Spring_Students.service.RespoProgramService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class RespoProgramRestController {
    private RespoProgramService respoProgramService;
    @GetMapping("/respo")
    public List<ResponsibleProgramDTO> responsibleProgramDTOs(){
        return respoProgramService.getResposProgram();
    }
    @GetMapping("/respo/{id}")
    public ResponsibleProgramDTO responsibleProgramDTO(@PathVariable String id) throws ResponsibleProgramNotFoundException {
        return respoProgramService.getRespoProgram(id);
    }
}
