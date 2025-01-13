package com.mustapha.Spring_Students.security.service;

import com.mustapha.Spring_Students.security.entities.AppRole;
import com.mustapha.Spring_Students.security.entities.AppUser;
import com.mustapha.Spring_Students.security.repositories.AppRoleRepository;
import com.mustapha.Spring_Students.security.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private AppRoleRepository appRoleRepository;
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public AppUser addNewUser(String username, String password, String confirmPassword) {
        AppUser appUser=appUserRepository.findByUsername(username);
        if(appUser!=null) throw new RuntimeException("this user Already exist");
        if(!password.equals(confirmPassword)) throw new RuntimeException("password not match");
        appUser= AppUser.builder()
                .username(username)
                .userId(UUID.randomUUID().toString())
                .password(passwordEncoder.encode(password))
                .build();
        return appUserRepository.save(appUser);
    }

    @Override
    public void addNewRole(String role) {
        AppRole appRole=appRoleRepository.findById(role).orElse(null);
        if(appRole!=null) throw new RuntimeException("this role already exist");
        appRole=AppRole.builder().role(role).build();
        appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser= appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).orElse(null);
        if (appUser.getRoles() == null) {
            appUser.setRoles(new ArrayList<>());
        }
        appUser.getRoles().add(appRole);
        //appUserRepository.save(appUser);
    }

    @Override
    public void removeRoleFromUser(String username, String role) {
        AppUser appUser= appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).orElse(null);
        appUser.getRoles().remove(appRole);
        //appUserRepository.save(appUser);
    }

    @Override
    public Boolean removeUser(String username) {
   return appUserRepository.deleteByUsername(username)>0;
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
