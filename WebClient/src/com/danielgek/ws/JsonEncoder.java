package com.danielgek.ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Response;
import server.Action;

import javax.websocket.*;


/**
 * Created by danielgek on 17/12/15.
 */
public class JsonEncoder implements Encoder.Text<Response> {

    @Override
    public String encode(Response response) throws EncodeException {
        Gson gson = new Gson();

        return gson.toJson(response, Response.class);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
