package com.github.cauebf.shoppingcartapi.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.github.cauebf.shoppingcartapi.security.user.ShopUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {

    private String jwtSecret = "nZ9LZJ1G3m9g0F0YHhR4mQvZbZzY9Z8nJq5kZP5dXyM=";
    private int expirationTime = 3600000;

    public String generateTokenForUser(Authentication authentication) {
        // this method is called after a successful authentication

        // get the authenticated user
        ShopUserDetails userPrincial = (ShopUserDetails) authentication.getPrincipal();

        // get the user's roles
        List<String> roles = userPrincial.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        
        // return the token with the user's information
        return Jwts.builder()
                .setSubject(userPrincial.getEmail()) // sets the token subject (user identifier)
                .claim("id", userPrincial.getId()) // adds a custom claim with the user ID
                .claim("roles", roles) // adds user roles/authorities
                .setIssuedAt(new Date()) // sets the token creation date
                .setExpiration(new Date((new Date()).getTime() + expirationTime)) // sets the token expiration date
                .signWith(key(), SignatureAlgorithm.HS256) // signs the token with the secret
                .compact(); // builds the token
    }

    private Key key() {
        // return a key based on the secret
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) {
        // check if the token is valid and return the user's email
        return Jwts.parserBuilder()
                .setSigningKey(key()) // sets the secret key used to verify the signature
                .build()
                .parseClaimsJws(token) // parses and validates the token (signature and expiration)
                .getBody() // gets the token claims (payload)
                .getSubject(); // returns the subject (email)
    }

    public boolean validateToken(String token) {
        // check if the token is valid and not expired
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key()) // sets the secret key for validation
                    .build()
                    .parseClaimsJws(token); // parses the token and validates signature & expiration
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage()); // Throws exception if token is invalid
        }
    }
}
