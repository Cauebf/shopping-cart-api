package com.github.cauebf.shoppingcartapi.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    // AuthenticationEntryPoint is an interface that is used to handle unauthenticated requests
    // JwtAuthEntryPoint is responsible for returning a 401 response message to the client when the user tries to access a protected resource without a valid token

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        
        // set the content type and status code of the response
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>(); // create a map to store the error message and status code
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED); // add the status code to the map
        body.put("error", "Unauthorized"); // add the error message to the map
        body.put("message", authException.getMessage()); // add the exception message to the map
        body.put("path", request.getServletPath()); // add the request path to the map

        // convert the map to JSON and write it to the response
        final ObjectMapper mapper = new ObjectMapper(); 
        mapper.writeValue(response.getOutputStream(), body);
    }
}
