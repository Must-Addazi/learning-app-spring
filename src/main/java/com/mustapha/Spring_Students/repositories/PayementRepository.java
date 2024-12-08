package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.Payment;
import com.mustapha.Spring_Students.enums.PayementStatus;
import com.mustapha.Spring_Students.enums.PayementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayementRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByStudentCode(String code);
    List<Payment> findByStatus(PayementStatus status);
    List<Payment> findByType(PayementType type);

}
