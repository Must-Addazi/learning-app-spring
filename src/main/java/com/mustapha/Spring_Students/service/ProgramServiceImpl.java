package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.ProgramRepository;
import com.mustapha.Spring_Students.security.entities.AppUser;
import com.mustapha.Spring_Students.security.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class ProgramServiceImpl implements ProgramService{
    private ProgramRepository programRepository;
    private RespoProgramService respoProgramService;
    private EmailService emailService;
    private AccountService accountService;
    private Mapper mapper;
    @Override
    public ProgramDTO getProgram(String id) throws ProgramNotFoundException {
        return mapper.fromProgram(programRepository.findById(id).orElseThrow(()-> new ProgramNotFoundException("Program not Found "))) ;
    }
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
    @Override
    public ProgramDTO saveProgram(MultipartFile file,ProgramDTO programDTO) throws IOException {
        Path path= Paths.get(System.getProperty("user.home"),"students-app-files","posterFiles");
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        programDTO.setId(UUID.randomUUID().toString());
        String FileID;
        FileID = programDTO.getName()+UUID.randomUUID();
        Path filePath= Paths.get(System.getProperty("user.home"),"students-app-files","posterFiles",FileID+".pdf");
        if(file !=null)
            Files.copy(file.getInputStream(),filePath);
        programDTO.setPosterFile(filePath.toUri().toString());
        Program program=mapper.fromProgramDTO(programDTO);
        program.setResponsibleProgram(mapper.fromResponsibleProgramDTO(programDTO.getResponsibleProgramDTO()));
        String randomPassword = generateRandomPassword(10);
        AppUser appUser= accountService.addNewUser(programDTO.getResponsibleProgramDTO().getEmail(),randomPassword,randomPassword);
        accountService.addRoleToUser(appUser.getUsername(),"USER");
        accountService.addRoleToUser(appUser.getUsername(),"ADMIN");
        emailService.sendEmail(
                programDTO.getResponsibleProgramDTO().getEmail(),
                "ENSAS congratulates you on registering your continuing education program",
                "Dear " + programDTO.getResponsibleProgramDTO().getName() + ",\n\n" +
                        "ENSAS congratulates you on successfully registering your continuing education program. " +
                        "You have been designated as the program's responsible person.\n\n" +
                        "Your login credentials are as follows:\n" +
                        "- Username: " + programDTO.getResponsibleProgramDTO().getEmail()+ "\n" +
                        "- Password: "+randomPassword +"\n\n" +
                        "Thank you for your commitment to excellence in education.\n\n" +
                        "Best regards,\n" +
                        "ENSAS Team"
        );

        return mapper.fromProgram(programRepository.save(program));
    }

    @Override
    public List<ProgramDTO> getPrograms() {
        List<Program> programList= programRepository.findAll();
        return programList.stream().map(program -> mapper.fromProgram(program)).toList();
    }
    public byte[] getPosterFile( String programId) throws IOException, ProgramNotFoundException {
        Program program = mapper.fromProgramDTO(getProgram(programId));
        String filePath=program.getPosterFile();
        return Files.readAllBytes(Path.of(URI.create(filePath)));
    }

    @Override
    public Boolean deleteProgram(String programId) throws IOException, ProgramNotFoundException, StudentNotFoundException, PaymentNotFoundException {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException("Program not found with ID: " + programId));

        if (program.getResponsibleProgram() != null) {
            respoProgramService.deleteRespoProgram(program.getResponsibleProgram().getId());
            log.info("delete respo");
        }

        String posterFile = program.getPosterFile();
        if (posterFile != null) {
            log.info("delete file");
            Path path = Paths.get(URI.create(posterFile));
            Files.deleteIfExists(path);
        }
       programRepository.deleteById(programId);
        return !programRepository.existsById(programId);
    }

}
