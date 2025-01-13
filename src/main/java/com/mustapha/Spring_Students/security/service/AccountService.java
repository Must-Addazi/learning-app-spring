package com.mustapha.Spring_Students.security.service;

import com.mustapha.Spring_Students.security.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(String username,String password,String confirmPassword);
    void addNewRole(String role);
    void addRoleToUser(String username,String role);
    void removeRoleFromUser(String username,String role);
    Boolean removeUser(String username);
    AppUser loadUserByUsername(String username);
}
