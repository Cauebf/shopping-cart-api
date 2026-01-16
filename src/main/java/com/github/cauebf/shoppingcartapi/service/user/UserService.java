package com.github.cauebf.shoppingcartapi.service.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.cauebf.shoppingcartapi.dto.UserDto;
import com.github.cauebf.shoppingcartapi.exceptions.AlreadyExistsException;
import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;
import com.github.cauebf.shoppingcartapi.model.User;
import com.github.cauebf.shoppingcartapi.repository.UserRepository;
import com.github.cauebf.shoppingcartapi.request.CreateUserRequest;
import com.github.cauebf.shoppingcartapi.request.UpdateUserRequest;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        // construtor dependency injection
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail())) // check if the user already exists
                .map(req -> {
                    // if not, create the user
                    User user = new User();

                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new AlreadyExistsException("Oops! " + request.getEmail() + " already exists!"));
    }

    @Override
    public User updateUser(Long userId, UpdateUserRequest request) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser); // update the user in the DB
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("User not found!");
        });
    }

    @Override
    public User getAuthenticatedUser() {
        // get the current authenticated user from the security context (set in AuthTokenFilter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // if the user is not authenticated, throw an exception
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("User is not authenticated");
        }

        // get the user's username (in this case, the email, defined in ShopUserDetails.getUsername())
        String email = authentication.getName();

        // get the user from the database
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
    
    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class); // convert the user to DTO
    }
}
