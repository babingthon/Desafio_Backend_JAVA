package com.example.kanban.service;

import com.example.kanban.api.dto.user.RegistrationRequest;
import com.example.kanban.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserService {

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    User findUserByEmail(String email);
    User registerNewUser(RegistrationRequest request);
}
