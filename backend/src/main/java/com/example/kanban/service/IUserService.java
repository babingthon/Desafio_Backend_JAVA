package com.example.kanban.service;

import com.example.kanban.api.dto.user.PasswordChangeRequest;
import com.example.kanban.api.dto.user.RegistrationRequest;
import com.example.kanban.api.dto.user.UserProfileResponse;
import com.example.kanban.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserService {

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    User findUserByEmail(String email);
    User registerNewUser(RegistrationRequest request);
    UserProfileResponse getLoggedUserProfile(String email);
    void updatePassword(String email, PasswordChangeRequest request);
}
