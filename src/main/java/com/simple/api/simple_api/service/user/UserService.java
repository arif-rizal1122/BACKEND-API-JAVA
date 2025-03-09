package com.simple.api.simple_api.service.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.simple.api.simple_api.dto.request.CreateUserRequest;
import com.simple.api.simple_api.dto.request.UpdateUserRequest;
import com.simple.api.simple_api.dto.response.UserDto;
import com.simple.api.simple_api.exception.AlreadyExistException;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.User;
import com.simple.api.simple_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;


    @Override
    public User getByUserId(Long userId) {
        return userRepository.findById(userId)
               .orElseThrow(() -> new ResponseNotFoundException("user not found!!"));
    }


    @Override
    public void deleteUser(Long userId){
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete, 
        () -> {
            new ResponseNotFoundException("user not found!!");
        });
    }


    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
            .filter(user -> !userRepository.existsByEmail(request.getEmail()))
            .map(req -> {
                User user = new User();
                user.setEmail(request.getEmail());
                user.setPassword(request.getPassword());
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                return userRepository.save(user);
            }).orElseThrow(() -> new AlreadyExistException("email has been already : " + request.getEmail()));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new ResponseNotFoundException("user not found"));
    }
    
    
    @Override
    public UserDto convertUserToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

}
