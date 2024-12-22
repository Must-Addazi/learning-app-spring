package com.mustapha.Spring_Students.security.service;

import com.mustapha.Spring_Students.security.entities.AppRole;
import com.mustapha.Spring_Students.security.entities.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      AppUser appUser= accountService.loadUserByUsername(username);
      if(appUser==null) throw  new UsernameNotFoundException(String.format("user %s not found",username));
     String[] roles= appUser.getRoles().stream().map(AppRole::getRole).toArray(String[]::new);
        return User.withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(roles)
                .build();
    }
}
