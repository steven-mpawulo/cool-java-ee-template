package com.example.demo.services.auth;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.filters.jwt.JwtUtility;
import com.example.demo.models.user.User;
import com.example.demo.models.user.UserPrincipal;
import com.example.demo.security.CustomSecurityContext;
import com.example.demo.services.base.BaseServiceInterface;
import com.example.demo.utils.PasswordHasher;
import com.example.demo.utils.ReturnObject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.HashMap;
import java.util.Map;

/*
    this is our sample auth service and its actions
    you can do whatever you want here

    Steven Mpawulo
    2024/12/05
 */
public class AuthService implements BaseServiceInterface {
    ReturnObject returnObject = new ReturnObject();
    JwtUtility jwtUtility = new JwtUtility();
    PasswordHasher passwordHasher = new PasswordHasher();

 //dummy
    private Response signUp(JSONObject requestBody, ContainerRequestContext requestContext) {
        String username = requestBody.getString("username");
        String password = requestBody.getString("password");

        if (username == null || username.isEmpty() || username.isBlank()) {
            return returnObject
                    .setReturnCodeAndReturnMessage(100, "Please provide username")
                    .sendResponse();
        }

        if (password == null || password.isEmpty() || password.isBlank()) {
            return returnObject
                    .setReturnCodeAndReturnMessage(100, "Please provide a password")
                    .sendResponse();
        }
        // hash the password
       String  hashedPassword = passwordHasher.hashPassword(password);

        // generate token
        Map<String,Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", hashedPassword);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        String token = jwtUtility.generateToken(user, claims);

        // update security context


        // our dummy response
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);

        return returnObject
                .setReturnCodeAndReturnMessagePlusReturnData(0, "User created successfully", response)
                .sendResponse();

    }

    //dummy
    private Response signIn(JSONObject requestBody, ContainerRequestContext requestContext) {
        String username = requestBody.getString("username");
        String password = requestBody.getString("password");

        if (username == null || username.isEmpty() || username.isBlank()) {
            return returnObject
                    .setReturnCodeAndReturnMessage(100, "Please provide username")
                    .sendResponse();
        }

        if (password == null || password.isEmpty() || password.isBlank()) {
            return returnObject
                    .setReturnCodeAndReturnMessage(100, "Please provide a password")
                    .sendResponse();
        }

        // validate user with db and do anything you want
        Map<String,Object> trialUser = new HashMap<>();
        trialUser.put("username", username);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        String token = jwtUtility.generateToken(trialUser, claims);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);



        // our dummy response
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);

        return returnObject
                .setReturnCodeAndReturnMessagePlusReturnData(0, "User login successful", response)
                .sendResponse();
    }


    @Override
    public Response processAction(String action, JSONObject requestBody, ContainerRequestContext requestContext) {
        return switch(action) {
            case "signup" -> signUp(requestBody, requestContext);
            case "login" -> signIn(requestBody, requestContext);
            default ->  returnObject
                    .setReturnCodeAndReturnMessage(100, "Action not known")
                    .sendResponse();
        };
    }
}
