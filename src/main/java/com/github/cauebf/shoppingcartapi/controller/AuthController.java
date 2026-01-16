package com.github.cauebf.shoppingcartapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.cauebf.shoppingcartapi.request.LoginRequest;
import com.github.cauebf.shoppingcartapi.response.ApiResponse;
import com.github.cauebf.shoppingcartapi.response.JwtResponse;
import com.github.cauebf.shoppingcartapi.security.jwt.JwtUtils;
import com.github.cauebf.shoppingcartapi.security.user.ShopUserDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        // construtor dependency injection
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // AuthenticationManager -> DaoAuthenticationProvider -> ShopUserDetailsService.loadUserByUsername(email) -> UserRepository.findByEmail(email) -> PasswordEncoder.matches(request.getPassword(), user.getPassword())
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(), 
                    request.getPassword()
                )
            );
            // if the authentication is successful, set the authentication object in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // generate the token for the user
            String jwt = jwtUtils.generateTokenForUser(authentication);
            // get the authenticated user (principal is the UserDetails returned by ShopUserDetailsService.loadUserByUsername(email))
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            // create the custom response
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
            // return the response with the token for the client
            return ResponseEntity.ok(new ApiResponse("Login Successful", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
