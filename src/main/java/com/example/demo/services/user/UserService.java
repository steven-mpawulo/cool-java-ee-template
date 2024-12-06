package com.example.demo.services.user;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.services.base.BaseServiceInterface;
import com.example.demo.utils.ReturnObject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

public class UserService implements BaseServiceInterface {
    ReturnObject returnObject = new ReturnObject();

    @Override
    public Response processAction(String action, JSONObject requestBody, ContainerRequestContext requestContext) {
        return switch (action) {
            case "getUser" -> returnObject
                    .setReturnCodeAndReturnMessage(0, "Your user here")
                    .sendResponse();
            default -> returnObject
                    .setReturnCodeAndReturnMessage(100, "action not known")
                    .sendResponse();
        };
    }
}
