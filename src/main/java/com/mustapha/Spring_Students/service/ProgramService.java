package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.entities.Payment;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface ProgramService {
    ProgramDTO getProgram(String id) throws ProgramNotFoundException;

    ProgramDTO saveProgram(MultipartFile file, ProgramDTO programDTO) throws IOException;

    List<ProgramDTO> getPrograms();

    byte[] getTimingFile(String paymentId) throws IOException, ProgramNotFoundException;
}