package com.danielgek.actions;

import com.danielgek.repositories.MessageRepository;
import com.danielgek.repositories.ProjectRepository;
import com.danielgek.ws.WebSocketServer;
import com.opensymphony.xwork2.ActionSupport;
import models.Message;
import models.Project;
import models.Response;
import org.apache.struts2.interceptor.SessionAware;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by danielgek on 19/12/15.
 */
public class MessageAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;

    private Message message = new Message();
    private Response response ;

    @Override
    public String execute() throws Exception {

        MessageRepository messageRepository = new MessageRepository();

        messageRepository.save(message);

        Project project = new ProjectRepository().getProject(message.getProjectId());

        HashMap<Integer, Session> users = WebSocketServer.users;

        for(int userId : users.keySet()){
            if(userId == message.getReceiver()){
                System.out.println("badum");
                users.get(userId).getBasicRemote().sendObject(new Response(true,"message", message.getProjectId()));
            }
        }

        response = new Response(true,"there are your messages");

        return SUCCESS;
    }

    public Response getResponse() {
        return response;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        session = map;
    }
}
