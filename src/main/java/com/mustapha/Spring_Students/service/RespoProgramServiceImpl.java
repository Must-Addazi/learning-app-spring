package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.ResponsibleProgramDTO;
import com.mustapha.Spring_Students.entities.ResponsibleProgram;
import com.mustapha.Spring_Students.exceptions.ResponsibleProgramNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.ResponsibleProgramRepository;
import com.mustapha.Spring_Students.security.entities.AppUser;
import com.mustapha.Spring_Students.security.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class RespoProgramServiceImpl implements RespoProgramService{
    private Mapper mapper;
    private ResponsibleProgramRepository responsibleProgramRepository;
    private AccountService accountService;
    private EmailService emailService;
    @Override
    public ResponsibleProgramDTO getRespoProgram(String id) throws ResponsibleProgramNotFoundException {

        return mapper.fromResponsibleProgram(responsibleProgramRepository.findById(id).orElseThrow(()->new ResponsibleProgramNotFoundException("responsibleProgram not found")));
    }

    @Override
    public ResponsibleProgramDTO saveRespoProgram(ResponsibleProgramDTO responsibleProgramDTO) {
        responsibleProgramDTO.setId(UUID.randomUUID().toString());
        ResponsibleProgram responsibleProgram=mapper.fromResponsibleProgramDTO(responsibleProgramDTO);
        AppUser appUser= accountService.addNewUser(responsibleProgramDTO.getEmail(),"12345","12345");
        accountService.addRoleToUser(appUser.getUsername(),"USER");
        accountService.addRoleToUser(appUser.getUsername(),"ADMIN");
        emailService.sendEmail(responsibleProgramDTO.getEmail(),"Validation subscription","your password is 12345 and your username is "+responsibleProgramDTO.getEmail());
        return mapper.fromResponsibleProgram(responsibleProgramRepository.save(responsibleProgram)) ;
    }

    @Override
    public List<ResponsibleProgramDTO> getResposProgram() {
        List<ResponsibleProgram> responsibleProgramList=responsibleProgramRepository.findAll();
        return responsibleProgramList.stream().map(responsibleProgram -> mapper.fromResponsibleProgram(responsibleProgram)).toList();
    }

    @Override
    public void deleteRespoProgram(String id) {
        responsibleProgramRepository.deleteById(id);
    }

    @Override
    public ResponsibleProgramDTO updateRespo(String id, ResponsibleProgramDTO responsibleProgramDTO) {
        ResponsibleProgram responsibleProgram=mapper.fromResponsibleProgramDTO(responsibleProgramDTO);
        responsibleProgram.setId(responsibleProgram.getId());
        return mapper.fromResponsibleProgram(responsibleProgramRepository.save(responsibleProgram));
    }
}
