package com.danielgek.ws;

/**
 * Created by danielgek on 17/12/15.
 */

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;


@ServerEndpoint(
        value = "/ws/{userId}",
        decoders = JsonDecoder.class,
        encoders = JsonEncoder.class
)
public class WebSocketServer {
    private Session session;
    public static HashMap<Integer,Session> users = new HashMap<>() ;


    @OnOpen
    public void onOpen(@PathParam("userId") int userId, Session session){
        System.out.println("OnOpen");
        System.out.println(userId);



        this.session = session;
        users.put(userId, session);
    }

    @OnClose
    public void onClose(){
        System.out.println("OnClose");
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("OnError");
        t.printStackTrace();
    }

    @OnMessage
    public void onMessage(String s){
        System.out.println("OnMessage");


    }


}
