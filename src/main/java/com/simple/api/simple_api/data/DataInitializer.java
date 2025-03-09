package com.simple.api.simple_api.data;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.simple.api.simple_api.model.User;
import com.simple.api.simple_api.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        createDefaultUsersIfNotExists();
    }
    

    @Transactional
    private void createDefaultUsersIfNotExists() {
        for (int i = 1; i <= 5; i++) { // Perbaikan range loop (1 sampai 5)
            String defaultEmail = "user" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                log.info("User {} already exists, skipping...", defaultEmail);
                continue;
            }

            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword("12345"); // Sebaiknya dienkripsi

            userRepository.save(user);
            log.info("Default user {} created successfully!", defaultEmail);
        }
    }

}
