package com.danielgek.actions;

import com.danielgek.models.ProjectRepository;
import com.opensymphony.xwork2.ActionSupport;
import models.Response;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by danielgek on 19/12/15.
 */
public class CancelProjectAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;

    private Response response;
    private int projectId;

    @Override
    public String execute() throws Exception {
        ProjectRepository projectRepository = new ProjectRepository();
        projectRepository.removeProject(projectId);
        response = new Response(true,"removed susseccfully!");

        return SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map ;
    }
}
