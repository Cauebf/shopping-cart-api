package com.github.cauebf.shoppingcartapi.security.config;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.cauebf.shoppingcartapi.security.jwt.AuthTokenFilter;
import com.github.cauebf.shoppingcartapi.security.jwt.JwtAuthEntryPoint;
import com.github.cauebf.shoppingcartapi.security.user.ShopUserDetailsService;

@Configuration // define a config class
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // enable method level security (e.g. @PreAuthorize)
public class ShopConfig {

    private final AuthTokenFilter authTokenFilter;
    private final ShopUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint authEntryPoint;

    private static final List<String> SECURED_URLS = 
            List.of("/api/v1/cart/**", "/api/v1/cart/items/**", "/api/v1/orders/**");

    public ShopConfig(ShopUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint, AuthTokenFilter authTokenFilter) {
        // construtor dependency injection
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService); // create a DaoAuthenticationProvider with the custom userDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // set the password encoder
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint)) // if not authenticated, call the jwtAuthEntryPoint
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // disable sessions (every request needs authentication token)
                .authorizeHttpRequests(auth -> 
                    auth.requestMatchers(SECURED_URLS.toArray(String[]::new)) // define the URLs that need authentication
                        .authenticated()
                        .anyRequest()
                        .permitAll())
                .authenticationProvider(daoAuthenticationProvider()) // especify the authentication provider
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class) // add the AuthTokenFilter before the UsernamePasswordAuthenticationFilter
                .build();
    }
}
