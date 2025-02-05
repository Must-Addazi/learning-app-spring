package com.mustapha.Spring_Students.security.service;

import com.mustapha.Spring_Students.security.entities.AppRole;
import com.mustapha.Spring_Students.security.entities.AppUser;
import com.mustapha.Spring_Students.security.entities.PasswordResetToken;
import com.mustapha.Spring_Students.security.repositories.AppRoleRepository;
import com.mustapha.Spring_Students.security.repositories.AppUserRepository;
import com.mustapha.Spring_Students.security.repositories.TokenRepository;
import com.mustapha.Spring_Students.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public AppUser addNewUser(String username, String password, String confirmPassword) {
        AppUser appUser=appUserRepository.findByUsername(username);
      if(appUser!=null) {
          return appUser;
      }else {
          if (!password.equals(confirmPassword)) throw new RuntimeException("password not match");
          appUser = AppUser.builder()
                  .username(username)
                  .userId(UUID.randomUUID().toString())
                  .password(passwordEncoder.encode(password))
                  .build();
          return appUserRepository.save(appUser);
      }
    }

    @Override
    public void addNewRole(String role) {
        AppRole appRole=appRoleRepository.findById(role).orElse(null);
        if(appRole==null) {
            appRole = AppRole.builder().role(role).build();
            appRoleRepository.save(appRole);
        }
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

    @Override
    public AppUser updateUsername(String username) {
        AppUser appUser= loadUserByUsername(username);
        appUser.setUsername(username);
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser upadatePassword(String username,String password, String confirmPassword) {
        AppUser appUser=appUserRepository.findByUsername(username);
        if (!password.equals(confirmPassword)) throw new RuntimeException("password not match");
        appUser.setPassword(passwordEncoder.encode(password));
            return appUserRepository.save(appUser);
        }

    @Override
    public Boolean generatePasswordResetToken(String email) {
            AppUser appUser = loadUserByUsername(email);
            if(appUser == null)
                return false;
            tokenRepository.deleteByAppUser(appUser);
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = PasswordResetToken.builder().token(token).appUser(appUser).expiryDate(LocalDateTime.now().plusMinutes(5)).build();
            tokenRepository.save(resetToken);

            String resetUrl = "https://fcensas-addazi.vercel.app/reset-password?token=" + token;
          //String resetUrl = "http://localhost:4200/reset-password?token=" + token;

        emailService.sendEmail(email, "Reset your password", "Click the link to reset your password: " + resetUrl);
       return true;
    }

    @Override
    public Boolean resetPassword(String token, String newPassword) {
        System.out.println("token is " +token);
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
                if(resetToken==null)
                    throw new IllegalArgumentException("Invalid token");

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        AppUser appUser = resetToken.getAppUser();
        appUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        appUserRepository.save(appUser);
        tokenRepository.delete(resetToken);
        return true;
    }

    @Scheduled(fixedRate = 3600000 )
    public void cleanUpExpiredTokens(){
        tokenRepository.deleteExpiredTokens();
    }
}
