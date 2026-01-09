package com.example.kanban.service;

import com.example.kanban.api.dto.user.PasswordChangeRequest;
import com.example.kanban.api.dto.user.RegistrationRequest;
import com.example.kanban.api.dto.user.UserProfileResponse;
import com.example.kanban.domain.Responsible;
import com.example.kanban.domain.User;
import com.example.kanban.domain.enums.Role;
import com.example.kanban.exception.ResourceNotFoundException;
import com.example.kanban.repository.ResponsibleRepository;
import com.example.kanban.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResponsibleRepository responsibleRepository;

    @Override
    public void updatePassword(String email, PasswordChangeRequest request) {
        User user = findUserByEmail(email);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("The current password provided is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public UserProfileResponse getLoggedUserProfile(String email) {
        Responsible responsible = responsibleRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible profile not found for email: " + email));

        return new UserProfileResponse(
                responsible.getName(),
                responsible.getJobTitle(),
                responsible.getEmail()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public User registerNewUser(RegistrationRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResourceNotFoundException("User with email " + request.email() + " already exists.");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        Responsible responsible = new Responsible();
        responsible.setName(request.name());
        responsible.setEmail(request.email());
        responsible.setJobTitle(request.jobTitle());

        responsible.setUser(user);

        responsibleRepository.save(responsible);

        return user;
    }
}
