package com.example.demo.configurer.base;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.services.auth.AuthService;
import com.example.demo.services.user.UserService;
import com.example.demo.utils.ReturnObject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

/*
    this is our service configurer where we will register our services
    you just have to create a service class with its actions
    and then come and add it here

    Steven Mpawulo
    2024/12/05
 */
public class BaseServiceConfigurer implements BaseServiceConfigurerInterface {
   AuthService authService = new AuthService();
   UserService userService = new UserService();

    @Override
    public Response processService(String service, String action, JSONObject requestBody, ContainerRequestContext requestContext) {
        ReturnObject returnObject = new ReturnObject();

        return switch (service) {
            case "auth" -> authService.processAction(action, requestBody, requestContext);
            case "user" -> userService.processAction(action, requestBody, requestContext);

            default -> returnObject
                       .setReturnCodeAndReturnMessage(100, "Service is unknown")
                       .sendResponse();
        };

    }


}




