package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.ProgramRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class ProgramServiceImpl implements ProgramService{
    private ProgramRepository programRepository;
    private Mapper mapper;
    @Override
    public ProgramDTO getProgram(String id) throws ProgramNotFoundException {
        return mapper.fromProgram(programRepository.findById(id).orElseThrow(()-> new ProgramNotFoundException("Program not Found "))) ;
    }

    @Override
    public ProgramDTO saveProgram(ProgramDTO programDTO) {
        Program program=mapper.fromProgramDTO(programDTO);
        program.setResponsibleProgram(mapper.fromResponsibleProgramDTO(programDTO.getResponsibleProgramDTO()));
        return mapper.fromProgram(programRepository.save(program));
    }

    @Override
    public List<ProgramDTO> getPrograms() {
        List<Program> programList= programRepository.findAll();
        return programList.stream().map(program -> mapper.fromProgram(program)).toList();
    }
}
