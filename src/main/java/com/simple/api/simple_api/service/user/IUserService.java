package com.simple.api.simple_api.service.user;

import com.simple.api.simple_api.dto.request.CreateUserRequest;
import com.simple.api.simple_api.dto.request.UpdateUserRequest;
import com.simple.api.simple_api.repository.model.User;

public interface IUserService {
    
    User getByUserId(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);
    
    void deleteUser(Long userId);

}
 