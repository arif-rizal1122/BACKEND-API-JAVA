package com.simple.api.simple_api.security.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.simple.api.simple_api.model.User;
import com.simple.api.simple_api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(ShopUserDetailsService.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Attempting to load user by email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        logger.info("User found: {}", user.getEmail());
        return ShopUserDetails.buildUserDetails(user);
    }
}
