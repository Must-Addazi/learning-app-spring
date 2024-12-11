package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.Payment;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.enums.PaymentType;
import com.mustapha.Spring_Students.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByStudent(Student student);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByType(PaymentType type);

}
