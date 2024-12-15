package com.mustapha.Spring_Students;

import com.mustapha.Spring_Students.dtos.*;
import com.mustapha.Spring_Students.enums.PaymentType;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
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
	//@Bean
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
				respo.setProgramDTO(programDTO);
				respoProgramService.updateRespo(respo.getId(),respo);
				programService.saveProgram(programDTO);
			});
			programService.getPrograms().forEach(programDTO ->{
                try {
                    studentService.saveStudent(null,NewStudentDTO.builder().programID(programDTO.getId()).CIN(UUID.randomUUID().toString()).firstName("Mustapha").build());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ProgramNotFoundException e) {
                    throw new RuntimeException(e);
                }
				try {
					studentService.saveStudent(null,NewStudentDTO.builder().programID(programDTO.getId()).CIN(UUID.randomUUID().toString()).firstName("Mustapha1").build());
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (ProgramNotFoundException e) {
					throw new RuntimeException(e);
				}
				try {
					studentService.saveStudent(null,NewStudentDTO.builder().programID(programDTO.getId()).CIN(UUID.randomUUID().toString()).firstName("Mustapha2").build());
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (ProgramNotFoundException e) {
					throw new RuntimeException(e);
				}
				try {
					studentService.saveStudent(null,NewStudentDTO.builder().programID(programDTO.getId()).CIN(UUID.randomUUID().toString()).firstName("Mustapha3").build());
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (ProgramNotFoundException e) {
					throw new RuntimeException(e);
				}
				moduleService.saveModule(ModuleDTO.builder().id(UUID.randomUUID().toString()).programDTO(programDTO).name("dev").teacherName("xxx").build());
				moduleService.saveModule(ModuleDTO.builder().id(UUID.randomUUID().toString()).programDTO(programDTO).name("IA").teacherName("yyy").build());
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
						 .studentCNE(student.getCIN())
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
