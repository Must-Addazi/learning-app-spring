package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ModuleDTO;
import com.mustapha.Spring_Students.dtos.NewModuleDTO;
import com.mustapha.Spring_Students.entities.CModule;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.exceptions.ModuleNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.ModuleRepository;
import com.mustapha.Spring_Students.repositories.ProgramRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {
    private ModuleRepository moduleRepository;
    private ProgramRepository programRepository;
    private Mapper mapper;

    @Override
    public List<ModuleDTO> getModuleList() {
        List<CModule> CModuleList = moduleRepository.findAll();
        return CModuleList.stream().map(CModule -> mapper.fromModule(CModule)).toList();
    }

    @Override
    public ModuleDTO getModule(String id) throws ModuleNotFoundException {
        CModule CModule = moduleRepository.findById(id).orElseThrow(() -> new ModuleNotFoundException("module not Found"));
        return mapper.fromModule(CModule);
    }

    @Override
    public ModuleDTO saveModule(NewModuleDTO moduleDTO) {
        Program program= programRepository.findById(moduleDTO.getProgramId()).get();
        CModule module= CModule.builder().id(UUID.randomUUID().toString()).name(moduleDTO.getName()).teacherName(moduleDTO.getTeacherName())
                .program(program).build();
        CModule savedModule = moduleRepository.save(module);
        return mapper.fromModule(savedModule);
    }

    @Override
    public ModuleDTO updateModule(String id, ModuleDTO moduleDTO) {
        CModule module = mapper.fromModuleDTO(moduleDTO);
        module.setId(id);
        CModule cModule = moduleRepository.save(module);
        return mapper.fromModule(cModule);
    }

    @Override
    public Boolean deleteModule(String id) {
        moduleRepository.deleteById(id);
        return !moduleRepository.existsById(id);
    }

    @Override
    public List<ModuleDTO> getModuleByProgram(String programId) throws ProgramNotFoundException {
        Program program = programRepository.findById(programId).orElseThrow(() -> new ProgramNotFoundException("Program not Found in module "));
        List<CModule> moduleList = moduleRepository.findByProgram(program);
        return moduleList.stream().map(CModule -> mapper.fromModule(CModule)).toList();
    }

}