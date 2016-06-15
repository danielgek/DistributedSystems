package com.danielgek.actions;

import com.danielgek.repositories.ProjectRepository;
import com.danielgek.ws.WebSocketServer;
import com.opensymphony.xwork2.ActionSupport;
import models.Pledge;
import models.Project;
import models.Response;
import org.apache.struts2.interceptor.SessionAware;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by danielgek on 16/12/15.
 */
public class PledgeAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;

    private Pledge pledge;
    private Response response;


    @Override
    public String execute() throws Exception {
        ProjectRepository projectRepository = new ProjectRepository();
        if(pledge != null){
            Response responseProject = projectRepository.pledge(pledge);
            if(responseProject.isSuccess()){
                response = new Response(true,"Pledged");
                Project project = projectRepository.getProject(pledge.getProjectId());

                int adminId = project.getAdminId();

                // TODO enviar notificação para users
                HashMap<Integer, Session> users = WebSocketServer.users;

                for(int userId : users.keySet()){
                    if(userId == adminId){
                        users.get(userId).getBasicRemote().sendObject(new Response(true,"pledged", project.getId()));
                    }
                }
            }else{
                this.response = responseProject;
            }

        }else{
            response = new Response(false,"you need to specify a pledge");
        }
        return SUCCESS;
    }

    public Pledge getPledge() {
        return pledge;
    }

    public void setPledge(Pledge pledge) {
        this.pledge = pledge;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
