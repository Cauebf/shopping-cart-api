package com.github.cauebf.shoppingcartapi.security.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.cauebf.shoppingcartapi.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUserDetails implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> authorities; // roles

    // convert User to ShopUserDetails
    public static ShopUserDetails buildUserDetails(User user) {
        // get the user's roles and map them to GrantedAuthority 
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new ShopUserDetails(
            user.getId(), 
            user.getEmail(), 
            user.getPassword(), 
            authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    } 
}
