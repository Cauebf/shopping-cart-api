package com.github.cauebf.shoppingcartapi.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.cauebf.shoppingcartapi.model.User;
import com.github.cauebf.shoppingcartapi.repository.UserRepository;

@Service
public class ShopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public ShopUserDetailsService(UserRepository userRepository) {
        // construtor dependency injection
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // this method is called when the user tries to log in (by AuthenticationManager -> AuthenticationProvider -> UserDetailsService.loadUserByUsername(email))

        // find the user in the database
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return ShopUserDetails.buildUserDetails(user); // return the User converted to UserDetails
    }
}
