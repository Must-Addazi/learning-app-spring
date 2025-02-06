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
    private CaptchaService captchaService;
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
                    imagePath = imagePath.substring(7);
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
    public StudentDTO saveStudent(MultipartFile file, MultipartFile bacFile, MultipartFile diplomaFile, MultipartFile profile, @Valid NewStudentDTO newStudentDTO)
            throws IOException, ProgramNotFoundException {
        if(!captchaService.verifyCaptcha(newStudentDTO.getCaptcha()))
            return null;
        Path baseDir = Paths.get(System.getProperty("user.home"), "students-app-files");
        Path cinDir = createDirectoryIfNotExists(baseDir.resolve("CINFiles"));
        Path bacDir = createDirectoryIfNotExists(baseDir.resolve("BacFiles"));
        Path diplomaDir = createDirectoryIfNotExists(baseDir.resolve("DiplomaFiles"));
        Path profileDir = createDirectoryIfNotExists(baseDir.resolve("profileFiles"));

        Program program = programRepository.findById(newStudentDTO.getProgramID())
                .orElseThrow(() -> new ProgramNotFoundException("Program not found with ID: " + newStudentDTO.getProgramID()));

        StudentDTO studentDTO = mapper.fromNewStudentDTO(newStudentDTO);
        studentDTO.setProgramDTO(mapper.fromProgram(program));
        studentDTO.setId(UUID.randomUUID().toString());
        studentDTO.setAmountPaid(0);

        String baseFileName = studentDTO.getFirstName() + studentDTO.getLastName() + studentDTO.getCIN();
        String cinFileName = baseFileName + ".pdf";
        String bacFileName = baseFileName + "bac.pdf";
        String diplomaFileName = baseFileName + "diploma.pdf";

        studentDTO.setPhotoCIN(saveFile(file, cinDir.resolve(cinFileName)));
        studentDTO.setBacFile(saveFile(bacFile, bacDir.resolve(bacFileName)));
        studentDTO.setDiplomaFile(saveFile(diplomaFile, diplomaDir.resolve(diplomaFileName)));
        if (profile != null) {
            String profileFileName = baseFileName + getFileExtension(profile);
            studentDTO.setPhoto(saveFile(profile, profileDir.resolve(profileFileName)));
        }

        Student student = mapper.fromStudentDTO(studentDTO);
        student.setConvene(false);
        student.setSelected(false);
        Student savedStudent = studentRepository.save(student);

        String randomPassword = generateRandomPassword();
        AppUser appUser = accountService.addNewUser(studentDTO.getEmail(), randomPassword, randomPassword);
        accountService.addRoleToUser(appUser.getUsername(), "USER");

        sendEmailToStudent(studentDTO, randomPassword);
        sendEmailToProgramResponsible(studentDTO);

        return mapper.fromStudent(savedStudent);
    }

    private Path createDirectoryIfNotExists(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        return dir;
    }

    private String saveFile(MultipartFile file, Path destinationPath) throws IOException {
        if (file != null && !file.isEmpty()) {
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            return destinationPath.toUri().toString();
        }
        return null;
    }

    private void sendEmailToStudent(StudentDTO studentDTO, String randomPassword) {
        String subject = "Subscription Validation";
        String content = String.format(
                "Dear %s %s,\n\n" +
                        "Your account has been successfully created.\n\n" +
                        "Username: %s\n" +
                        "Password: %s\n\n" +
                        "Please log in to the platform to complete your profile.\n\n" +
                        "Best regards,\nThe Team",
                studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), randomPassword
        );
        emailService.sendEmail(studentDTO.getEmail(), subject, content);
    }

    private void sendEmailToProgramResponsible(StudentDTO studentDTO) {
        String subject = "New Enrollment Notification";
        String content = String.format(
                "Dear %s,\n\n" +
                        "A new student has been enrolled in your program.\n\n" +
                        "Student Details:\n" +
                        "Name: %s %s\n" +
                        "CIN: %s\n\n" +
                        "Please log in to the platform to review the enrollment.\n\n" +
                        "Best regards,\nThe Team",
                studentDTO.getProgramDTO().getResponsibleProgramDTO().getName(),
                studentDTO.getFirstName(), studentDTO.getLastName(),
                studentDTO.getCIN()
        );
        emailService.sendEmail(studentDTO.getProgramDTO().getResponsibleProgramDTO().getEmail(), subject, content);
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

    @Override
    public List<StudentDTO> findByProgramAndConvene(String programId) throws ProgramNotFoundException {
        Program program = programRepository.findById(programId).orElseThrow(()-> new ProgramNotFoundException("program not found"));
        List<Student> studentList= studentRepository.findByProgramAndConveneTrue(program);
        return studentList.stream().map(student -> {
            StudentDTO studentDTO = mapper.fromStudent(student);
            studentDTO.setPhoto(encodeImageToBase64(student.getPhoto()));
            return studentDTO;
        }).toList();
    }
    public byte[] getFile(String studentId, String file) throws IOException, StudentNotFoundException {
        Student student = mapper.fromStudentDTO(getStudent(studentId));
        String filePath;
        if (Objects.equals(file, "bac")) {
            filePath = student.getBacFile();
        } else if (Objects.equals(file, "CIN")) {
            filePath = student.getPhotoCIN();
        } else {
            filePath = student.getDiplomaFile();
        }

        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("no file" + studentId);
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

    @Override
    public List<StudentDTO> conveneStudentList() {
        List<Student> studentList = studentRepository.findByConveneTrue();
        return studentList.stream().map(student -> {
            StudentDTO studentDTO = mapper.fromStudent(student);
            studentDTO.setPhoto(encodeImageToBase64(student.getPhoto()));
            return studentDTO;
        }).toList();
    }

    @Override
    public StudentDTO selectStudent(String studentId) {
        Student student=studentRepository.findById(studentId).get();
        if(student.getSelected()!= null)
            student.setSelected(!student.getSelected());
        else
            student.setSelected(true);
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
