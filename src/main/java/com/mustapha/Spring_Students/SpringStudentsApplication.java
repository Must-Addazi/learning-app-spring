package com.mustapha.Spring_Students;

import com.mustapha.Spring_Students.dtos.*;
import com.mustapha.Spring_Students.enums.PaymentType;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.security.service.AccountService;
import com.mustapha.Spring_Students.service.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
@EnableAsync
@Configuration
public class SpringStudentsApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); // charge des variables d'envirenement
		System.out.println("DATABASE_URL: " + dotenv.get("DATABASE_URL"));
		System.out.println("DATABASE_USERNAME: " + dotenv.get("DATABASE_USERNAME"));
		System.out.println("DATABASE_PASSWORD: " + dotenv.get("DATABASE_PASSWORD"));
		SpringApplication.run(SpringStudentsApplication.class, args);
	}


		@Bean
		public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
			PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
			configurer.setLocation(new FileSystemResource(".env"));
			return configurer;
		}

	@Bean
	CommandLineRunner commandLineRunner(AccountService accountService){
return args -> {
 accountService.addNewRole("USER");
 accountService.addNewRole("ADMIN");
 accountService.addNewRole("SUPER_ADMIN");
 accountService.addNewUser("mustapha@gmail.com","12345","12345");
	accountService.addRoleToUser("mustapha@gmail.com","USER");
	accountService.addRoleToUser("mustapha@gmail.com","ADMIN");
	accountService.addRoleToUser("mustapha@gmail.com","SUPER_ADMIN");
};
	}
	//@Bean
	CommandLineRunner commandLineRunner(StudentService studentService, AccountService accountService,
    ModuleService moduleService,
	PaymentService paymentService,
	RespoProgramService respoProgramService,
	ProgramService programService
										){
		return args -> {
			respoProgramService.saveRespoProgram(ResponsibleProgramDTO.builder().email("prof1@gmail.com").id(UUID.randomUUID().toString()).phoneNumber("066666").name("Must1").build());
			respoProgramService.saveRespoProgram(ResponsibleProgramDTO.builder().email("prof2@gmail.com").id(UUID.randomUUID().toString()).phoneNumber("066666").name("Must2").build());
			respoProgramService.getResposProgram().forEach(respo->{
				ProgramDTO programDTO=ProgramDTO.builder().responsibleProgramDTO(respo).price(Math.random()*1000).id(UUID.randomUUID().toString()).name(respo.getName()+"filiere").build();
				programDTO.setResponsibleProgramDTO(respo);
			//	respo.setProgramDTO(programDTO);
				respoProgramService.updateRespo(respo.getId(),respo);
                try {
                    programService.saveProgram(null,programDTO);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
		programService.getPrograms().forEach(programDTO ->{

                try {
                    studentService.saveStudent(null,null,null,null,NewStudentDTO.builder().programID(programDTO.getId()).CIN(UUID.randomUUID().toString()).email("user").firstName("Mustapha").build());
                } catch (IOException | ProgramNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
					studentService.saveStudent(null,null,null,null,NewStudentDTO.builder().programID(programDTO.getId()).CIN(UUID.randomUUID().toString()).email("admin").firstName("Mustapha1").build());
				} catch (IOException | ProgramNotFoundException e) {
					throw new RuntimeException(e);
				}
                try {
					studentService.saveStudent(null,null,null,null,NewStudentDTO.builder().programID(programDTO.getId()).CIN(UUID.randomUUID().toString()).firstName("Mustapha3").build());
				} catch (IOException | ProgramNotFoundException e) {
					throw new RuntimeException(e);
				}
                moduleService.saveModule(NewModuleDTO.builder().programId(programDTO.getId()).name("dev").teacherName("xxx").build());
				moduleService.saveModule(NewModuleDTO.builder().programId(programDTO.getId()).name("IA").teacherName("yyy").build());
			} );

			PaymentType [] paymentTypes = PaymentType.values();
			Random random = new Random();
         studentService.getStudentList().forEach(student -> {
			 for (int i = 0; i < 5; i++) {
				 int index = random.nextInt(paymentTypes.length);
				 NewPaymentDTO payment = NewPaymentDTO.builder().
						 amount((10000+Math.random()*20000)).
						 date(LocalDate.now())
						 .type(paymentTypes[index])
						 .email(student.getEmail())
						 .build();
                 try {
                     paymentService.savePayment(null,payment);
                 } catch (IOException e) {
                     throw new RuntimeException(e);
                 }
             }
		 });

		};
	}

}
