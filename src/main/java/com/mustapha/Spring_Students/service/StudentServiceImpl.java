package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.NewStudentDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.StudentRepository;
import com.mustapha.Spring_Students.security.entities.AppUser;
import com.mustapha.Spring_Students.security.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class StudentServiceImpl implements StudentService{
    private StudentRepository studentRepository;
    private ProgramService programService;
    private AccountService accountService;
    private EmailService emailService;
    private Mapper mapper;
    @Override
    public List<StudentDTO> getStudentList() {
        List<Student> studentList = studentRepository.findAll();
        return studentList.stream().map(student -> {
            StudentDTO studentDTO = mapper.fromStudent(student);
            studentDTO.setPhoto(encodeImageToBase64(student.getPhoto()));
            return studentDTO;
        }).toList();
    }
    private String encodeImageToBase64(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                if (imagePath.startsWith("file:///")) {
                    imagePath = imagePath.substring(8);
                }
                byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));
                return Base64.getEncoder().encodeToString(imageBytes);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture de l'image: " + e.getMessage());
            return null;
        }
    }

    @Override
    public StudentDTO getStudent(String id) throws StudentNotFoundException {
        Student student = studentRepository.findById(id).orElseThrow(() ->new StudentNotFoundException("Student not found"));
        return mapper.fromStudent(student);
    }

    @Override
    public StudentDTO saveStudent(MultipartFile file,MultipartFile bacFile,MultipartFile diplomaFile,MultipartFile profile, NewStudentDTO newStudentDTO) throws IOException, ProgramNotFoundException {
        Path path= Paths.get(System.getProperty("user.home"),"students-app-files","CINFiles");
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        Path pathBac= Paths.get(System.getProperty("user.home"),"students-app-files","BacFiles");
        if(!Files.exists(pathBac)){
            Files.createDirectories(pathBac);
        }
        Path pathDiploma = Paths.get(System.getProperty("user.home"),"students-app-files","DiplomaFiles");
        if(!Files.exists(pathDiploma)){
            Files.createDirectories(pathDiploma);
        }
        Path pathProfile= Paths.get(System.getProperty("user.home"),"students-app-files","profileFiles");
        if(!Files.exists(pathProfile)){
            Files.createDirectories(pathProfile);
        }
        Program program=mapper.fromProgramDTO(programService.getProgram(newStudentDTO.getProgramID()));
        StudentDTO studentDTO = mapper.fromNewStudentDTO(newStudentDTO);
        studentDTO.setProgramDTO(mapper.fromProgram(program));
        studentDTO.setId(UUID.randomUUID().toString());
        studentDTO.setAmountPaid(0);
        String CinFileID;
        String bacFileID;
        String diplomaFileID;
        String photoID;
        CinFileID = studentDTO.getFirstName() + studentDTO.getLastName() + studentDTO.getCIN();
        bacFileID = studentDTO.getFirstName() + studentDTO.getLastName() + studentDTO.getCIN()+"bac";
        diplomaFileID= studentDTO.getFirstName() + studentDTO.getLastName() + studentDTO.getCIN()+"diploma";
        photoID= studentDTO.getFirstName() + studentDTO.getLastName()+studentDTO.getCIN();
        Path filePath= Paths.get(System.getProperty("user.home"),"students-app-files","CINFiles",CinFileID+".pdf");
        if(file !=null && Objects.requireNonNull(file.getOriginalFilename()).endsWith(".pdf"))
            Files.copy(file.getInputStream(),filePath);
        Path bacPath= Paths.get(System.getProperty("user.home"),"students-app-files","BacFiles",bacFileID+".pdf");
        if(bacFile !=null && Objects.requireNonNull(bacFile.getOriginalFilename()).endsWith(".pdf"))
            Files.copy(bacFile.getInputStream(),bacPath);

        Path diplomaPath= Paths.get(System.getProperty("user.home"),"students-app-files","DiplomaFiles",diplomaFileID+".pdf");
        if(diplomaFile !=null && Objects.requireNonNull(diplomaFile.getOriginalFilename()).endsWith(".pdf"))
            Files.copy(diplomaFile.getInputStream(),diplomaPath);

        if (profile != null && (Objects.requireNonNull(profile.getOriginalFilename()).endsWith(".jpg") || profile.getOriginalFilename().endsWith(".png"))) {
            Path imagePath = Paths.get(System.getProperty("user.home"), "students-app-files", "profileFiles", photoID + getFileExtension(profile));
            Files.copy(profile.getInputStream(), imagePath);
            studentDTO.setPhoto(imagePath.toUri().toString());
        }
        studentDTO.setPhotoCIN(filePath.toUri().toString());
       AppUser appUser= accountService.addNewUser(studentDTO.getEmail(),"12345","12345");
        accountService.addRoleToUser(appUser.getUsername(),"USER");
        Student student = mapper.fromStudentDTO(studentDTO);
        Student savedStudent = studentRepository.save(student);
        emailService.sendEmail(studentDTO.getEmail(),"Validation subscription","your password is 12345 and your username is "+studentDTO.getEmail());
        emailService.sendEmail(studentDTO.getProgramDTO().getResponsibleProgramDTO().getEmail(),"Nouveau inscription","Nous vous informaons qu'un nouveau etudiant avec CIN "+studentDTO.getCIN()+" a été inscrit merci de consulter votre platforme");
        return mapper.fromStudent(savedStudent);
    }

    private String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

    @Override
    public void deleteStudent(String id) throws StudentNotFoundException {
        StudentDTO studentDTO= getStudent(id);
        Student student= mapper.fromStudentDTO(studentDTO);
    studentRepository.delete(student);
    }

    @Override
    public StudentDTO updateStudent(String id,StudentDTO studentDTO) {
        Student student= mapper.fromStudentDTO(studentDTO);
        student.setId(id);
        Student upStudent= studentRepository.save(student);
        return mapper.fromStudent(upStudent);
    }

    @Override
    public List<StudentDTO> searchStudentByName(String name) {
        List<Student> studentList = studentRepository.searchByName(name);
        return studentList.stream().map(student -> mapper.fromStudent(student)).toList();
    }


    @Override
    public StudentDTO findByCIN(String code) {
        return mapper.fromStudent(studentRepository.findByCIN(code));
    }

    @Override
    public StudentDTO findByEmail(String email) {
        StudentDTO studentDTO= mapper.fromStudent(studentRepository.findByEmail(email));
        studentDTO.setPhoto(encodeImageToBase64(studentDTO.getPhoto()));
        return studentDTO;
    }

    @Override
    public List<StudentDTO> findByProgram(String programId) throws ProgramNotFoundException {
        Program program = mapper.fromProgramDTO(programService.getProgram(programId));
        List<Student> studentList= studentRepository.findByProgram(program);
        return studentList.stream().map(student -> {
            StudentDTO studentDTO = mapper.fromStudent(student);
            studentDTO.setPhoto(encodeImageToBase64(student.getPhoto()));
            return studentDTO;
        }).toList();
    }
}
