package com.mustapha.Spring_Students.security.service;

import com.mustapha.Spring_Students.security.entities.AppRole;
import com.mustapha.Spring_Students.security.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(String username,String password,String confirmPassword);
    AppRole addNewRole(String role);
    void addRoleToUser(String username,String role);
    void removeRoleFromUser(String username,String role);
    AppUser loadUserByUsername(String username);
}
