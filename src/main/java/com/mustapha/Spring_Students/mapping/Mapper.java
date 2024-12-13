package com.mustapha.Spring_Students.mapping;

import com.mustapha.Spring_Students.dtos.*;
import com.mustapha.Spring_Students.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class Mapper {
    public ModuleDTO fromModule(CModule cModule){
        ModuleDTO moduleDTO = new ModuleDTO();
        BeanUtils.copyProperties(cModule,moduleDTO);
        moduleDTO.setProgramDTO(fromProgram(cModule.getProgram()));
        return moduleDTO;
    }
    public CModule fromModuleDTO(ModuleDTO moduleDTO){
        CModule cModule = new CModule();
        BeanUtils.copyProperties(moduleDTO, cModule);
        cModule.setProgram(fromProgramDTO(moduleDTO.getProgramDTO()));
        return cModule;
    }
    public PaymentDTO fromPayment(Payment payment){
        PaymentDTO paymentDTO = new PaymentDTO();
        BeanUtils.copyProperties(payment,paymentDTO);
        paymentDTO.setStudentDTO(fromStudent(payment.getStudent()));
        return paymentDTO;
    }
    public Payment fromPaymentDTO(PaymentDTO paymentDTO){
        Payment payment= new Payment();
        BeanUtils.copyProperties(paymentDTO,payment);
        payment.setStudent(fromStudentDTO(paymentDTO.getStudentDTO()));
        return payment;
    }
    public PaymentDTO fromNewpaymentDTO(NewPaymentDTO newPaymentDTO){
        PaymentDTO paymentDTO= new PaymentDTO();
        BeanUtils.copyProperties(newPaymentDTO,paymentDTO);
        return paymentDTO;
    }
    public ProgramDTO fromProgram(Program program){
        ProgramDTO programDTO= new ProgramDTO();
        BeanUtils.copyProperties(program,programDTO);
        programDTO.setResponsibleProgramDTO(fromResponsibleProgram(program.getResponsibleProgram()));
        return programDTO;
    }
    public Program fromProgramDTO(ProgramDTO programDTO){
        Program program= new Program();
        BeanUtils.copyProperties(programDTO,program);
        program.setResponsibleProgram(fromResponsibleProgramDTO(programDTO.getResponsibleProgramDTO()));
        return program;
    }
    public ResponsibleProgramDTO fromResponsibleProgram(ResponsibleProgram responsibleProgram){
        ResponsibleProgramDTO responsibleProgramDTO= new ResponsibleProgramDTO();
        BeanUtils.copyProperties(responsibleProgram,responsibleProgramDTO);
        return responsibleProgramDTO;
    }
    public ResponsibleProgram fromResponsibleProgramDTO(ResponsibleProgramDTO responsibleProgramDTO){
        ResponsibleProgram responsibleProgram= new ResponsibleProgram();
        BeanUtils.copyProperties(responsibleProgramDTO,responsibleProgram);
        return responsibleProgram;
    }
    public StudentDTO fromStudent(Student student){
        StudentDTO studentDTO= new StudentDTO();
        BeanUtils.copyProperties(student,studentDTO);
        studentDTO.setProgramDTO(fromProgram(student.getProgram()));
        return studentDTO;
    }
    public Student fromStudentDTO(StudentDTO studentDTO){
        Student student= new Student();
        BeanUtils.copyProperties(studentDTO,student);
        student.setProgram(fromProgramDTO(studentDTO.getProgramDTO()));
        return student;
    }
}
