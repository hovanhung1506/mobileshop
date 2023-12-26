package com.example.mobileshop.utils;

import com.example.mobileshop.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUtil {
    public static UserPrincipal getCurrentUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Collection<? extends GrantedAuthority> getCurrentRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails;
    }
}
