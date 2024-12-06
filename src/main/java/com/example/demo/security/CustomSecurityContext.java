package com.example.demo.security;

import com.example.demo.models.user.UserPrincipal;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class CustomSecurityContext implements SecurityContext {
    private final UserPrincipal userPrincipal;

    public CustomSecurityContext(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    @Override
    public Principal getUserPrincipal() {
        return this.userPrincipal;
    }

    @Override
    public boolean isUserInRole(String role) {
        // Implement role-based check logic, if necessary
        return false; // For now, returning false
    }

    @Override
    public boolean isSecure() {
        // Logic to check if the connection is secure (HTTPs)
        return false; // Default to false
    }

    @Override
    public String getAuthenticationScheme() {
        // Return the authentication scheme, e.g., "Bearer"
        return "Bearer";
    }
}

