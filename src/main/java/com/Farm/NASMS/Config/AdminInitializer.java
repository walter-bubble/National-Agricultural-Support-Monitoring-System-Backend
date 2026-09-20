package com.Farm.NASMS.Config;

import com.Farm.NASMS.Repository.UserRepository;
import com.Farm.NASMS.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {
    @Value("${nasms.admin.username}")
    private String adminUsername;

    @Value("${nasms.admin.email}")
    private String adminEmail;

    @Value("${nasms.admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initializeAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByEmailAddress(adminEmail).isEmpty()) {

                User admin = new User();

                admin.setUserName(adminUsername);
                admin.setEmailAddress(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println("NASMS ADMIN account created successfully.");

            } else {

                System.out.println("NASMS ADMIN account already exists.");
            }
        };
    }
}
