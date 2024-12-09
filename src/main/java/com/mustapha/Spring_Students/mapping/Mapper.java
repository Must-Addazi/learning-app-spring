package com.mustapha.Spring_Students.mapping;

import com.mustapha.Spring_Students.dtos.*;
import com.mustapha.Spring_Students.entities.*;
import com.mustapha.Spring_Students.entities.CModule;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class Mapper {
    public ModuleDTO fromModule(CModule CModule){
        ModuleDTO moduleDTO = new ModuleDTO();
        BeanUtils.copyProperties(CModule,moduleDTO);
        return moduleDTO;
    }
    public CModule fromModuleDTO(ModuleDTO moduleDTO){
        CModule CModule = new CModule();
        BeanUtils.copyProperties(moduleDTO, CModule);
        return CModule;
    }
    public PaymentDTO fromPayment(Payment payment){
        PaymentDTO paymentDTO = new PaymentDTO();
        BeanUtils.copyProperties(payment,paymentDTO);
        return paymentDTO;
    }
    public Payment fromPaymentDTO(PaymentDTO paymentDTO){
        Payment payment= new Payment();
        BeanUtils.copyProperties(paymentDTO,payment);
        return payment;
    }
    public ProgramDTO fromProgram(Program program){
        ProgramDTO programDTO= new ProgramDTO();
        BeanUtils.copyProperties(program,programDTO);
        return programDTO;
    }
    public Program fromProgramDTO(ProgramDTO programDTO){
        Program program= new Program();
        BeanUtils.copyProperties(programDTO,program);
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
        return studentDTO;
    }
    public Student fromStudentDTO(StudentDTO studentDTO){
        Student student= new Student();
        BeanUtils.copyProperties(studentDTO,student);
        return student;
    }
}
