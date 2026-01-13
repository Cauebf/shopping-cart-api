package com.github.cauebf.shoppingcartapi.service.user;

import com.github.cauebf.shoppingcartapi.model.User;
import com.github.cauebf.shoppingcartapi.request.CreateUserRequest;
import com.github.cauebf.shoppingcartapi.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(Long userId, UpdateUserRequest request);
    void deleteUser(Long userId);
}
