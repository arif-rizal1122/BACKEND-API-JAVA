package com.simple.api.simple_api.data;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.simple.api.simple_api.model.Category;
import com.simple.api.simple_api.model.Product;
import com.simple.api.simple_api.model.Role;
import com.simple.api.simple_api.model.User;
import com.simple.api.simple_api.repository.CategoryRepository;
import com.simple.api.simple_api.repository.ProductRepository;
import com.simple.api.simple_api.repository.RoleRepository;
import com.simple.api.simple_api.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");

        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultUsersIfNotExists();
        createDefaultProductIfNotExists();
        createDefaultAdminIfNotExists();
    }
    

    @Transactional
    private void createDefaultUsersIfNotExists() {
        Role userRole = roleRepository.findByName("ROLE_USER")
         .orElseThrow(() -> new RuntimeException("ROLE_USER not found in database"));

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
            user.setPassword(passwordEncoder.encode("buka1122")); 
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            log.info("Default user {} created successfully!", defaultEmail);
        }
    }


    @Transactional
    private void createDefaultAdminIfNotExists() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
         .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found in database"));

        for (int i = 1; i <= 2; i++) { // Perbaikan range loop (1 sampai 5)
            String defaultEmail = "admin" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                log.info("User {} already exists, skipping...", defaultEmail);
                continue;
            }

            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("buka1122")); 
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            log.info("Default user Admin {} created successfully!", defaultEmail);
        }
    }



    private void createDefaultRoleIfNotExists(Set<String> roles) {
        roles.stream()
            .filter(role -> roleRepository.findByName(role).isEmpty())
            .map(roleName -> {
                Role newRole = new Role();
                newRole.setName(roleName);
                return newRole;
            })
            .forEach(roleRepository::save);
    }
    





    private void createDefaultProductIfNotExists(){
        for (int i = 1; i <= 5; i++) {
            String productName = "product " + i;
            String productBrand ="brand " + i; 
            if (productRepository.existsByNameAndBrand(productName, productBrand)) {
                log.info("Product {} already exists, skipping...", productName, productBrand);
                continue;
            }

            Category category = categoryRepository.findByName("Default Category")
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName("Default Category");
                    return categoryRepository.save(newCategory); 
            });
                
                                                                         
            Product product = new Product(
                productName, 
                "Brand " + i,
                BigDecimal.valueOf(10000 + (i* 5000)),
                10 + i,
                "description of " + productName,
                category
            );
            productRepository.save(product);
            log.info("Default product {} created successfully!", productName);
        }
    }

}
