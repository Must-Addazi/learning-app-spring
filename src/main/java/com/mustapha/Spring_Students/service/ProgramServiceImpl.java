package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.entities.Payment;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.ProgramRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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
    public ProgramDTO saveProgram(MultipartFile file,ProgramDTO programDTO) throws IOException {
        Path path= Paths.get(System.getProperty("user.home"),"students-app-files","timing");
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        programDTO.setId(UUID.randomUUID().toString());
        String FileID;
        FileID = programDTO.getName()+UUID.randomUUID();
        Path filePath= Paths.get(System.getProperty("user.home"),"students-app-files","timing",FileID+".pdf");
        if(file !=null)
            Files.copy(file.getInputStream(),filePath);
        programDTO.setTiming(filePath.toUri().toString());
        Program program=mapper.fromProgramDTO(programDTO);
        program.setResponsibleProgram(mapper.fromResponsibleProgramDTO(programDTO.getResponsibleProgramDTO()));
        return mapper.fromProgram(programRepository.save(program));
    }

    @Override
    public List<ProgramDTO> getPrograms() {
        List<Program> programList= programRepository.findAll();
        return programList.stream().map(program -> mapper.fromProgram(program)).toList();
    }
    public byte[] getTimingFile( String programId) throws IOException, ProgramNotFoundException {
        Program program = mapper.fromProgramDTO(getProgram(programId));
        String filePath=program.getTiming();
        return Files.readAllBytes(Path.of(URI.create(filePath)));
    }
}
