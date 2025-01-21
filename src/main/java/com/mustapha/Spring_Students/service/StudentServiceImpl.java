package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.NewStudentDTO;
import com.mustapha.Spring_Students.dtos.PaymentDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.ProgramRepository;
import com.mustapha.Spring_Students.repositories.StudentRepository;
import com.mustapha.Spring_Students.security.entities.AppUser;
import com.mustapha.Spring_Students.security.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.util.ClassUtils.isPresent;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class StudentServiceImpl implements StudentService{
    private StudentRepository studentRepository;
    private ProgramRepository programRepository;
    private PaymentService paymentService;
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
    public StudentDTO saveStudent(MultipartFile file,MultipartFile bacFile,MultipartFile diplomaFile,MultipartFile profile, @Valid NewStudentDTO newStudentDTO) throws IOException, ProgramNotFoundException {
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
        Program program=programRepository.findById(newStudentDTO.getProgramID()).get();
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
            Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);
        Path bacPath= Paths.get(System.getProperty("user.home"),"students-app-files","BacFiles",bacFileID+".pdf");
        if(bacFile !=null && Objects.requireNonNull(bacFile.getOriginalFilename()).endsWith(".pdf"))
            Files.copy(bacFile.getInputStream(),bacPath,StandardCopyOption.REPLACE_EXISTING);

        Path diplomaPath= Paths.get(System.getProperty("user.home"),"students-app-files","DiplomaFiles",diplomaFileID+".pdf");
        if(diplomaFile !=null && Objects.requireNonNull(diplomaFile.getOriginalFilename()).endsWith(".pdf"))
            Files.copy(diplomaFile.getInputStream(),diplomaPath,StandardCopyOption.REPLACE_EXISTING);

        if (profile != null && (Objects.requireNonNull(profile.getOriginalFilename()).endsWith(".jpg") || profile.getOriginalFilename().endsWith(".png"))) {
            Path imagePath = Paths.get(System.getProperty("user.home"), "students-app-files", "profileFiles", photoID + getFileExtension(profile));
            Files.copy(profile.getInputStream(), imagePath,StandardCopyOption.REPLACE_EXISTING);
            studentDTO.setPhoto(imagePath.toUri().toString());
        }
        Student student = mapper.fromStudentDTO(studentDTO);
        student.setPhotoCIN(filePath.toUri().toString());
        student.setBacFile(bacPath.toUri().toString());
        student.setDiplomaFile(diplomaPath.toUri().toString());
        student.setConvene(false);
        student.setSelected(false);
        Student savedStudent = studentRepository.save(student);
        String randomPassword = generateRandomPassword();
        AppUser appUser= accountService.addNewUser(studentDTO.getEmail(),randomPassword,randomPassword);
        accountService.addRoleToUser(appUser.getUsername(),"USER");
        emailService.sendEmail(studentDTO.getEmail(), "Subscription Validation", "Your password is "+randomPassword+" and your username is " + studentDTO.getEmail());
        emailService.sendEmail(studentDTO.getProgramDTO().getResponsibleProgramDTO().getEmail(), "New Enrollment", "We inform you that a new student with CIN " + studentDTO.getCIN() + " has been enrolled. Please check your platform.");
        return mapper.fromStudent(savedStudent);
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
    @Override
    public Boolean deleteStudent(String id) throws StudentNotFoundException, IOException, PaymentNotFoundException {
        StudentDTO studentDTO= getStudent(id);
        Student student= mapper.fromStudentDTO(studentDTO);
        String CinPath= student.getPhotoCIN();
        String profilePath= student.getPhoto();
        String bacPath= student.getBacFile();
        String diplomaPath= student.getDiplomaFile();
        if( CinPath != null){
            Path path= Paths.get(URI.create(CinPath));
            Files.deleteIfExists(path);
        }
        if( profilePath != null){
            Path path= Paths.get(URI.create(profilePath));
            Files.deleteIfExists(path);
        }
        if( bacPath != null){
            Path path= Paths.get(URI.create(bacPath));
            Files.deleteIfExists(path);
        }
        if( diplomaPath != null){
            Path path= Paths.get(URI.create(diplomaPath));
            Files.deleteIfExists(path);
        }
        accountService.removeRoleFromUser(student.getEmail(),"USER");
        accountService.removeUser(student.getEmail());
        List<PaymentDTO> paymentDTOS= paymentService.getPaymentByEmail(student.getEmail());
        for(PaymentDTO payment : paymentDTOS){
            paymentService.deletePayment(payment.getId());
        }
        studentRepository.delete(student);
        return !studentRepository.existsById(id);
    }
    @Override
    public StudentDTO updateStudent(String id,NewStudentDTO newstudentDTO) throws StudentNotFoundException {
        Student student= mapper.fromStudentDTO(getStudent(id));
        student.setFirstName(newstudentDTO.getFirstName());
        student.setLastName(newstudentDTO.getLastName());
        student.setCIN(newstudentDTO.getCIN());
        student.setBirthDate(newstudentDTO.getBirthDate());
        accountService.updateUsername(newstudentDTO.getEmail());
        student.setEmail(newstudentDTO.getEmail());
        student.setNoteBac(newstudentDTO.getNoteBac());
        student.setPhone(newstudentDTO.getPhone());
        student.setNoteDiploma(newstudentDTO.getNoteDiploma());
        Program program=programRepository.findById(newstudentDTO.getProgramID()).get();
        student.setProgram(program);
        Student upStudent= studentRepository.save(student);
        log.info("student DTO "+newstudentDTO.toString() );
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
        Program program = programRepository.findById(programId).orElseThrow(()-> new ProgramNotFoundException("program not found"));
        List<Student> studentList= studentRepository.findByProgram(program);
        return studentList.stream().map(student -> {
            StudentDTO studentDTO = mapper.fromStudent(student);
            studentDTO.setPhoto(encodeImageToBase64(student.getPhoto()));
            return studentDTO;
        }).toList();
    }
    public byte[] getFile( String studentId, String file) throws IOException, StudentNotFoundException {
        Student student= mapper.fromStudentDTO(getStudent(studentId));
        String filePath;
        if(Objects.equals(file, "bac")) {
            filePath = student.getBacFile();
        } else if (Objects.equals(file, "CIN")) {
            filePath = student.getPhotoCIN();
        }else{
            filePath= student.getDiplomaFile();
        }
        return Files.readAllBytes(Path.of(URI.create(filePath)));
    }

    @Override
    public StudentDTO updateFile(String id, MultipartFile file, String fileType) throws IOException, StudentNotFoundException {
        Student student = mapper.fromStudentDTO(getStudent(id));

        String baseDir = System.getProperty("user.home") + File.separator + "students-app-files";
        Path filePath;

        switch (fileType) {
            case "CIN":
                filePath = handleFileUpload(file, baseDir, "CINFiles", student.getFirstName() + student.getLastName() + student.getCIN(), ".pdf");
                if (filePath != null) {
                    student.setPhotoCIN(filePath.toUri().toString());
                }
                break;

            case "Bac":
                filePath = handleFileUpload(file, baseDir, "BacFiles", student.getFirstName() + student.getLastName() + student.getCIN() + "bac", ".pdf");
                if (filePath != null) {
                    student.setBacFile(filePath.toUri().toString());
                }
                break;

            case "Diploma":
                filePath = handleFileUpload(file, baseDir, "DiplomaFiles", student.getFirstName() + student.getLastName() + student.getCIN() + "diploma", ".pdf");
                if (filePath != null) {
                    student.setDiplomaFile(filePath.toUri().toString());
                }
                break;

            case "Profile":
                filePath = handleFileUpload(file, baseDir, "profileFiles", student.getFirstName() + student.getLastName() + student.getCIN(), getFileExtension(file));
                if (filePath != null) {
                    student.setPhoto(filePath.toUri().toString());
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid fileType: " + fileType);
        }

        Student savedStudent = studentRepository.save(student);
       return mapper.fromStudent(savedStudent);
    }

    @Override
    public StudentDTO conveneStudent(String studentId) {
        Student student=studentRepository.findById(studentId).get();
        if(student.getConvene()!= null)
        student.setConvene(!student.getConvene());
        else
            student.setConvene(true);
        return mapper.fromStudent(student);
    }

    private Path handleFileUpload(MultipartFile file, String baseDir, String subFolder, String fileName, String extension) throws IOException {
        if (file == null || file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(extension)) {
            return null;
        }

        Path dirPath = Paths.get(baseDir, subFolder);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(fileName + extension);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath;
    }


    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "";
        }
        int dotIndex = originalFilename.lastIndexOf('.');
        return (dotIndex != -1) ? originalFilename.substring(dotIndex) : "";
    }
}
