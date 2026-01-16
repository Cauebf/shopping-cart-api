package com.github.cauebf.shoppingcartapi.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.cauebf.shoppingcartapi.security.user.ShopUserDetailsService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// custom filter to validate token
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    // OncePerRequestFilter guarantees that the filter is only executed once per request

    private JwtUtils jwtUtils;
    private ShopUserDetailsService userDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils, ShopUserDetailsService userDetailsService) {
        // construtor dependency injection
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    ) throws ServletException, IOException {
        // this method is called for each HTTP request

        try {
            String jwt = parseJwt(request); // extract the token
            
            // checks if the token is not null, the signature is valid, and it has not expired
            if (jwt != null && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt); // extract username (email)

                // get the user from the database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); 
                
                // set the authentication object
                Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails, // principal
                    null, // credentials
                    userDetails.getAuthorities() // authorities (roles)
                );
                // set the authentication object in the security context to be used in other parts of the application
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage() + " : Invalid or expired token, please log in and try again!");
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            return;
        }
        
        filterChain.doFilter(request, response); // pass the request to the next filter
    }
    
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization"); // get the Authorization header
        
        // check if the header is not null and starts with "Bearer "
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            // remove "Bearer " and return only the token
            return headerAuth.substring(7, headerAuth.length()); 
        }
        return null;
    }
}
