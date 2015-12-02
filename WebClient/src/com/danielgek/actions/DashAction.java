package com.danielgek.actions;

import com.danielgek.models.ProjectRepository;
import com.opensymphony.xwork2.ActionSupport;
import models.Project;
import models.User;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by danielgek on 28/11/15.
 */
public class DashAction extends ActionSupport implements SessionAware {

    private ArrayList<Project> projects;
    private ArrayList<Project> currentProjects;
    private ArrayList<Project> oldProjects;
    private Map<String, Object> session;
    private User user;


    public String execute() throws Exception {
        //check for login
        if(session.size() == 0)
            return LOGIN;

        user = (User) session.get("user");

        ProjectRepository projectRepository = new ProjectRepository();

        projects = projectRepository.getAllProjects();
        currentProjects = projectRepository.getMyProjects(user.getId());
        oldProjects = projectRepository.getOldProjects();


        return SUCCESS;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public ArrayList<Project> getCurrentProjects() {
        return currentProjects;
    }

    public void setCurrentProjects(ArrayList<Project> currentProjects) {
        this.currentProjects = currentProjects;
    }

    public ArrayList<Project> getOldProjects() {
        return oldProjects;
    }

    public void setOldProjects(ArrayList<Project> oldProjects) {
        this.oldProjects = oldProjects;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
