package com.github.cauebf.shoppingcartapi.data;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.cauebf.shoppingcartapi.model.Role;
import com.github.cauebf.shoppingcartapi.model.User;
import com.github.cauebf.shoppingcartapi.repository.RoleRepository;
import com.github.cauebf.shoppingcartapi.repository.UserRepository;

@Transactional
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        // construtor dependency injection
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // executed when the application starts
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // populate the database with default roles and users/admins
        Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultUserIfNotExists();
        createDefaultAdminIfNotExists();
    }

    private void createDefaultRoleIfNotExists(Set<String> roles) {
        for (String role : roles) {
            if (roleRepository.findByName(role) == null) {
                roleRepository.save(new Role(role));
                System.out.println("Role " + role + " created!");
            }
        }
    }

    private void createDefaultUserIfNotExists() {
        Role userRole = (Role) roleRepository.findByName("ROLE_USER");
        
        for (int i = 1; i <= 5; i++) {
            String username = "user" + i;
            String password = "123";
            String email = username + "@email.com";
            
            if (!userRepository.existsByEmail(email)) {
                User user = new User();
                user.setFirstName("User");
                user.setLastName(username);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(password));
                user.setRoles(Set.of(userRole));
                userRepository.save(user);
                System.out.println("User " + username + " created!");
            }
        }
    }

    private void createDefaultAdminIfNotExists() {
        Role adminRole = (Role) roleRepository.findByName("ROLE_ADMIN");
        
        for (int i = 1; i <= 2; i++) {
            String username = "admin" + i;
            String password = "123";
            String email = username + "@email.com";
            
            if (!userRepository.existsByEmail(email)) {
                User user = new User();
                user.setFirstName("Admin");
                user.setLastName(username);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(password));
                user.setRoles(Set.of(adminRole));
                userRepository.save(user);
                System.out.println("Admin " + username + " created!");
            }    
        }
    }
}
