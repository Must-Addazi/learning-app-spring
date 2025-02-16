package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.ResponsibleProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProgramService {
    ProgramDTO getProgram(String id) throws ProgramNotFoundException;

    ProgramDTO getProgramByRespo(String RespoEmail) throws ProgramNotFoundException, ResponsibleProgramNotFoundException;

    ProgramDTO saveProgram(MultipartFile file, ProgramDTO programDTO) throws IOException;

    List<ProgramDTO> getPrograms();

    byte[] getFile(String programId,String file) throws IOException, ProgramNotFoundException;

    Boolean deleteProgram(String programId) throws IOException, ProgramNotFoundException, StudentNotFoundException, PaymentNotFoundException, ResponsibleProgramNotFoundException;

    ProgramDTO updateProgram(ProgramDTO programDTO, MultipartFile poster, MultipartFile timing) throws  IOException, ProgramNotFoundException;


}