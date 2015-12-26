package com.danielgek.ws;

import com.google.gson.Gson;
import models.Response;
import server.Action;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Created by danielgek on 17/12/15.
 */
public class JsonDecoder implements Decoder.Text<Action> {
    @Override
    public Action decode(String string) throws DecodeException {
        Gson gson = new Gson();
        return gson.fromJson(string, Action.class);
    }

    @Override
    public boolean willDecode(String s) {
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
