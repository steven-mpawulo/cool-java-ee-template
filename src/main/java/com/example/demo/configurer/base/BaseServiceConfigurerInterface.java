package com.example.demo.configurer.base;

import com.alibaba.fastjson2.JSONObject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

public interface BaseServiceConfigurerInterface {
    public Response processService(String service, String action, JSONObject requestBody, ContainerRequestContext requestContext);

}
