package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ModuleDTO;
import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.entities.CModule;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.exceptions.ModuleNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.ModuleRepository;
import com.mustapha.Spring_Students.repositories.ProgramRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@AllArgsConstructor
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService{
    private ModuleRepository moduleRepository;
    private ProgramRepository programRepository;
    private Mapper mapper;
    @Override
    public List<ModuleDTO> getModuleList() {
        List<CModule> CModuleList = moduleRepository.findAll();
        return  CModuleList.stream().map(CModule -> mapper.fromModule(CModule)).toList();
    }

    @Override
    public ModuleDTO getModule(String id) throws ModuleNotFoundException {
        CModule CModule = moduleRepository.findById(id).orElseThrow(()->new ModuleNotFoundException("module not Found"));
        return mapper.fromModule(CModule);
    }

    @Override
    public ModuleDTO saveModule(ModuleDTO moduleDTO) {
        CModule cModule=mapper.fromModuleDTO(moduleDTO);
        cModule.setProgram(mapper.fromProgramDTO(moduleDTO.getProgramDTO()));
        CModule savedModule=moduleRepository.save(cModule);
        return mapper.fromModule(savedModule);
    }

    @Override
    public ModuleDTO updateModule(String id, ModuleDTO moduleDTO) {
        CModule module= mapper.fromModuleDTO(moduleDTO);
        module.setId(id);
        CModule cModule=moduleRepository.save(module);
        return mapper.fromModule(cModule);
    }

    @Override
    public void deleteModule(String id) {
        moduleRepository.deleteById(id);
    }

    @Override
    public List<ModuleDTO> getModuleByProgram(String programId) throws ProgramNotFoundException {
        Program program= programRepository.findById(programId).orElseThrow(()-> new ProgramNotFoundException("Program not Found in module "));
    List<CModule> moduleList= moduleRepository.findByProgram(program);
        return  moduleList.stream().map(CModule -> mapper.fromModule(CModule)).toList();    }
    public List<ModuleDTO> getModuleByProgramV2(Program program) {
        List<CModule> moduleList= moduleRepository.findByProgram(program);
        return  moduleList.stream().map(CModule -> mapper.fromModule(CModule)).toList();    }

}
