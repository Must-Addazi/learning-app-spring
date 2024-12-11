package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ResponsibleProgramDTO;
import com.mustapha.Spring_Students.entities.ResponsibleProgram;
import com.mustapha.Spring_Students.exceptions.ResponsibleProgramNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.ResponsibleProgramRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@AllArgsConstructor
@Service
@Transactional
public class RespoProgramServiceImpl implements RespoProgramService{
    private Mapper mapper;
    private ResponsibleProgramRepository responsibleProgramRepository;
    @Override
    public ResponsibleProgramDTO getRespoProgram(String id) throws ResponsibleProgramNotFoundException {

        return mapper.fromResponsibleProgram(responsibleProgramRepository.findById(id).orElseThrow(()->new ResponsibleProgramNotFoundException("responsibleProgram not found")));
    }

    @Override
    public ResponsibleProgramDTO saveRespoProgram(ResponsibleProgramDTO responsibleProgramDTO) {
        ResponsibleProgram responsibleProgram=mapper.fromResponsibleProgramDTO(responsibleProgramDTO);
        return mapper.fromResponsibleProgram(responsibleProgramRepository.save(responsibleProgram)) ;
    }

    @Override
    public List<ResponsibleProgramDTO> getResposProgram() {
        List<ResponsibleProgram> responsibleProgramList=responsibleProgramRepository.findAll();
        return responsibleProgramList.stream().map(responsibleProgram -> mapper.fromResponsibleProgram(responsibleProgram)).toList();
    }
}
