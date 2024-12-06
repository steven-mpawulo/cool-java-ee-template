package com.example.demo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.demo.configurer.base.BaseServiceConfigurer;
import com.example.demo.utils.ReturnObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/* this will be our only controller and it houses two methods
    the get method for checking on server availability
    the post method which handles all of our server requests
    all our request are authenticated with jwt tokens except for the auth service request
    and our one and only GET request

    Steven Mpawulo
    2024/12/05
 */


@Path("/v1")
public class DemoController{
    @Context ContainerRequestContext requestContext;
    BaseServiceConfigurer baseServiceConfigurer = new BaseServiceConfigurer();
    ReturnObject returnOBject = new ReturnObject();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response serverUpAndRunning() {
        return returnOBject
                .setReturnCodeAndReturnMessage(0, "Pong")
                .sendResponse();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response demo(String json)  {

        //handle errors globally
        try {
            // confirm that you have the request body
            if (json.isBlank()) {
                return returnOBject
                        .setReturnCodeAndReturnMessage(100, "Please provide some data")
                        .sendResponse();
            }

            // we shall parse our request body here once
            JSONObject requestBody = JSON.parseObject(json);
            String service = requestBody.getString("service");
            String action = requestBody.getString("action");


            if (service == null || service.isEmpty() || service.isBlank()) {
                return returnOBject.
                        setReturnCodeAndReturnMessage(0, "Please provide a service")
                        .sendResponse();
            }

            if (action == null || action.isEmpty() || action.isBlank()) {
                return returnOBject
                        .setReturnCodeAndReturnMessage(100, "Please provide an action")
                        .sendResponse();
            }

            // let our service configurer do some magic

            return baseServiceConfigurer.processService(service, action, requestBody, requestContext);
        } catch(Exception e) {
            return returnOBject
                    .setReturnCodeAndReturnMessage(400, e.getMessage())
                    .sendResponse();
        }

    }

}
