package com.piyush.Ledgerflow.security;


import com.piyush.Ledgerflow.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (user.isSystemUser()) {
            return List.of(new SimpleGrantedAuthority("ROLE_SYSTEM"));
        }

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Spring Security uses this as the username.
     * We are using email as the username.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public Long getUserId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
