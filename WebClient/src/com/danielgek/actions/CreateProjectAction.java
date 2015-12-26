package com.danielgek.actions;

import com.danielgek.models.ProjectRepository;
import com.opensymphony.xwork2.ActionSupport;
import models.*;
import org.apache.struts2.interceptor.SessionAware;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by danielgek on 29/11/15.
 */
public class CreateProjectAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    private User user;
    private Project project = new Project();
    private ArrayList<Reward> rewards = new ArrayList<Reward>();
    private ArrayList<Poll> polls = new ArrayList<Poll>();
    private ArrayList<Level> levels = new ArrayList<Level>();
    private Response response;

    @Override
    public String execute() throws Exception {
        this.user = (User) session.get("user");
        ProjectRepository projectRepository = new ProjectRepository();
        this.project = projectRepository.newProject(this.project);
        if(this.project != null ){
            response = new Response(true, "Created!");
        }else{
            response = new Response(false, "Fail!");
        }


        return SUCCESS;

    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(ArrayList<Poll> polls) {
        this.polls = polls;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<Level> levels) {
        this.levels = levels;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "CreateProjectAction{" +
                "session=" + session +
                ", user=" + user +
                ", project=" + project +
                ", rewards=" + rewards +
                ", polls=" + polls +
                ", levels=" + levels +
                '}';
    }
}
