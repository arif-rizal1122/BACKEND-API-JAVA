package com.simple.api.simple_api.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.simple.api.simple_api.model.User;
import com.simple.api.simple_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {
    
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                   .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return ShopUserDetails.buildUserDetails(user);
    }

    

}
