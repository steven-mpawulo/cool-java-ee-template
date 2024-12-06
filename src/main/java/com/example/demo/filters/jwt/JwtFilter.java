package com.example.demo.filters.jwt;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.models.user.User;
import com.example.demo.models.user.UserPrincipal;
import com.example.demo.security.CustomSecurityContext;
import com.example.demo.utils.ReturnObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.*;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Provider
public class JwtFilter implements ContainerRequestFilter {
    ReturnObject returnObject = new ReturnObject();
    JwtUtility jwtUtility = new JwtUtility();

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        try {
            String httpMethod = containerRequestContext.getMethod();
            System.out.println(httpMethod);

            // we want to skip checking for auth token on one and only GET method
            if (httpMethod.equals("GET")) {
                return;
            }

            String requestBody = getRequestBody(containerRequestContext);

            if (requestBody == null) {
                containerRequestContext.abortWith(
                        returnObject
                                .setReturnCodeAndReturnMessage(100, "Please provide some data")
                                .sendResponse()
                );
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            try {
                json = objectMapper.writeValueAsString(requestBody);
            } catch(Exception e) {
                e.printStackTrace();
            }

            System.out.println(json);
            JSONObject jsonObject = JSONObject.parseObject(json);
            String service = jsonObject.getString("service");
            System.out.println(service);

            if (service == null || service.isEmpty() || service.isBlank()) {
                containerRequestContext.abortWith(
                        returnObject
                                .setReturnCodeAndReturnMessage(100, "Please provide a service")
                                .sendResponse()
                );

                return;
            }

            // we want to skip checking for auth token on auth service
            if ("auth".equals(service)) {
                return;
            }

            String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length());
                try {
                    // let's validate the token
                    String username = jwtUtility.extractUserName(token);
                    System.out.println("token user name");
                    System.out.println(username);

                    /*  you can decide to fetch user from database and validate the token
                        this is just an example of a user
                     */

                    User userfromDb = new User();
                    userfromDb.setUsername("steven");
                    userfromDb.setPassword("123456");

                    Map<String, Object> user = new HashMap<String, Object>();
                    user.put("username", userfromDb.getUsername());
                    user.put("password", userfromDb.getPassword());


                    boolean isTokenValid = jwtUtility.isTokenValid(token, user);
                    if (!isTokenValid) {
                        containerRequestContext.abortWith(
                                returnObject.setReturnCodeAndReturnMessage(100, "Invalid access token")
                                .sendResponse()
                        );
                    }

                    // now we can set our security context
                    UserPrincipal userPrincipal = new UserPrincipal(userfromDb);
                    SecurityContext securityContext = new CustomSecurityContext(userPrincipal);
                    containerRequestContext.setSecurityContext(securityContext);


                } catch (Exception e) {
                    containerRequestContext.abortWith(
                            returnObject.setReturnCodeAndReturnMessage(100, "invalid access token")
                                    .sendResponse()
                    );
                }
            } else {
                containerRequestContext.abortWith(
                        returnObject.setReturnCodeAndReturnMessage(100, "Please provide authorization token")
                                .sendResponse()
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
            containerRequestContext.abortWith(
                    returnObject
                            .setReturnCodeAndReturnMessage(400, e.getMessage())
                            .sendResponse()
            );
        }

    }


    private String getRequestBody(ContainerRequestContext requestContext) {
        try {
            InputStream originalInputStream = requestContext.getEntityStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(originalInputStream));
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }


            // Set the buffered request body back to the request context
            ByteArrayInputStream newInputStream = new ByteArrayInputStream(requestBody.toString().getBytes());
            requestContext.setEntityStream(newInputStream);

            return requestBody.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }

}


