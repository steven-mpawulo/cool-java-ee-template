package com.example.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

public class ReturnObject {
    public int returnCode = 0;
    public String returnMessage = "";
    public  Map<String, Object> returnData = new HashMap<String, Object>();

    public ReturnObject setReturnCodeAndReturnMessage(int returnCode, String returnMessage) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;

        return this;

    }

    public ReturnObject setReturnCodeAndReturnMessagePlusReturnData(int returnCode, String returnMessage, Map<String, Object> returnData) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
        this.returnData = returnData;

        return this;
    }

    private String toJson()  {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public Response sendResponse()  {
        String ourReturnObject = this.toJson();
        return Response.status(200).entity(ourReturnObject).build();
    }
}
