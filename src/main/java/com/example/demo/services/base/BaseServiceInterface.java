package com.example.demo.services.base;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.utils.ReturnObject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

public interface BaseServiceInterface {
    public Response processAction(String action, JSONObject requestBody, ContainerRequestContext requestContext);
}
