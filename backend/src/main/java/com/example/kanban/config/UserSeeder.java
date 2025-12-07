package com.example.kanban.config;

import com.example.kanban.domain.enums.Role;
import com.example.kanban.domain.User;
import com.example.kanban.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            log.info("Nenhum usuário encontrado. Criando usuário ADMIN inicial.");

            String adminEmail = "admin@kanban.com";
            String rawPassword = "adminpassword";

            String encodedPassword = passwordEncoder.encode(rawPassword);

            User adminUser = User.builder()
                    .email(adminEmail)
                    .password(encodedPassword)
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(adminUser);

            log.info("Usuário ADMIN criado com sucesso: {} / Senha: {}", adminEmail, rawPassword);
            log.warn("ATENÇÃO: Altere a senha do usuário ADMIN após o primeiro login!");

        } else {
            log.info("Usuários encontrados. O seeder ADMIN não foi executado.");
        }
    }
}