package com.example.demo.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    public String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public boolean verifyPassword(String plainTextPassword, String storedHashedPassword) {
        return BCrypt.checkpw(plainTextPassword, storedHashedPassword);
    }
}
