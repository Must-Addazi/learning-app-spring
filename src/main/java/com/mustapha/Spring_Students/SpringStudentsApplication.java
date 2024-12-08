package com.mustapha.Spring_Students;

import com.mustapha.Spring_Students.entities.Payment;
import com.mustapha.Spring_Students.enums.PayementStatus;
import com.mustapha.Spring_Students.enums.PayementType;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.repositories.PayementRepository;
import com.mustapha.Spring_Students.repositories.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class SpringStudentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringStudentsApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepository,
	PayementRepository payementRepository
	){
		return args -> {
          studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).code("112233").programID("INFO").firstName("Mustapha").build());
			studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).programID("IDIA").code("112244").firstName("Mohammed").build());
			studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).programID("GIIA").code("112255").firstName("Said").build());
			studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).programID("GIIA").code("112266").firstName("Amine").build());

			PayementType [] payementTypes = PayementType.values();
			Random random = new Random();
         studentRepository.findAll().forEach(student -> {
			 for (int i = 0; i < 10; i++) {
				 int index = random.nextInt(payementTypes.length);
				 Payment payment = Payment.builder().
						 amount((int)(10000+Math.random()*20000)).
						 date(LocalDate.now())
						 .type(payementTypes[index])
						 .file(UUID.randomUUID().toString())
						 .student(student)
						 .status(PayementStatus.CREATED)
						 .build();
				 payementRepository.save(payment);
			 }
		 });
		};
	}

}
