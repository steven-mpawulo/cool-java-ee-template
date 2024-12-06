package com.example.demo.models.user;

import javax.security.auth.Subject;
import java.security.Principal;

public class UserPrincipal implements Principal {
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    public User getUser() {
        return user;
    }
}
