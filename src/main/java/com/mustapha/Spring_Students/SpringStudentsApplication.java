package com.mustapha.Spring_Students;

import com.mustapha.Spring_Students.dtos.*;
import com.mustapha.Spring_Students.entities.Payment;
import com.mustapha.Spring_Students.enums.PaymentType;
import com.mustapha.Spring_Students.enums.PaymentStatus;
import com.mustapha.Spring_Students.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Transactional
@SpringBootApplication
public class SpringStudentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringStudentsApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(StudentService studentService,
    ModuleService moduleService,
	PaymentService paymentService,
	RespoProgramService respoProgramService,
	ProgramService programService
										){
		return args -> {
			respoProgramService.saveRespoProgram(ResponsibleProgramDTO.builder().email("prof1@gmail.com").id(UUID.randomUUID().toString()).phoneNumber("066666").firstName("Must1").build());
			respoProgramService.saveRespoProgram(ResponsibleProgramDTO.builder().email("prof2@gmail.com").id(UUID.randomUUID().toString()).phoneNumber("066666").firstName("Must2").build());
			respoProgramService.getResposProgram().forEach(respo->{
				ProgramDTO programDTO=ProgramDTO.builder().responsibleProgramDTO(respo).price(Math.random()*1000).id(UUID.randomUUID().toString()).name(respo.getFirstName()+"filiere").build();
				programDTO.setResponsibleProgramDTO(respo);
				programService.saveProgram(programDTO);
			});
			programService.getPrograms().forEach(programDTO ->{
				studentService.saveStudent(StudentDTO.builder().id(UUID.randomUUID().toString()).programDTO(programDTO).amountPaid(123).CNE(UUID.randomUUID().toString()).firstName("Mustapha").build());
				studentService.saveStudent(StudentDTO.builder().id(UUID.randomUUID().toString()).programDTO(programDTO).programDTO(programDTO).amountPaid(123).CNE(UUID.randomUUID().toString()).firstName("Mohammed").build());
				studentService.saveStudent(StudentDTO.builder().id(UUID.randomUUID().toString()).programDTO(programDTO).amountPaid(123).CNE(UUID.randomUUID().toString()).firstName("Said").build());
				studentService.saveStudent(StudentDTO.builder().id(UUID.randomUUID().toString()).programDTO(programDTO).amountPaid(123).CNE(UUID.randomUUID().toString()).firstName("Amine").build());
				moduleService.saveModule(ModuleDTO.builder().id(UUID.randomUUID().toString()).programDTO(programDTO).name("dev").teacherName("xxx").build());
				moduleService.saveModule(ModuleDTO.builder().id(UUID.randomUUID().toString()).programDTO(programDTO).name("IA").teacherName("yyy").build());
			} );

			PaymentType [] paymentTypes = PaymentType.values();
			Random random = new Random();
         studentService.getStudentList().forEach(student -> {
			 for (int i = 0; i < 10; i++) {
				 int index = random.nextInt(paymentTypes.length);
				 PaymentDTO payment = PaymentDTO.builder().
						 amount((int)(10000+Math.random()*20000)).
						 date(LocalDate.now())
						 .type(paymentTypes[index])
						 .file(UUID.randomUUID().toString())
						 .studentDTO(student)
						 .status(PaymentStatus.CREATED)
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
