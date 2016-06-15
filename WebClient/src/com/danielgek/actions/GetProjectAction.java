package com.danielgek.actions;

import com.danielgek.repositories.*;
import com.opensymphony.xwork2.ActionSupport;
import models.*;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by danielgek on 14/12/15.
 */
public class GetProjectAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private User user;

    private Project project = new Project();
    private ArrayList<Reward> rewards = new ArrayList<Reward>();
    private ArrayList<Poll> polls = new ArrayList<Poll>();
    private ArrayList<Level> levels = new ArrayList<Level>();
    private ArrayList<Message> messages = new ArrayList<Message>();


    private Response response;

    @Override
    public String execute() throws Exception {
        this.user = (User) session.get("user");

        System.out.println("getProject");
        System.out.println(project.toString());
        ProjectRepository projectRepository = new ProjectRepository();
        project = projectRepository.getProject(project.getId());
        System.out.println(project.toString());
        RewardRepository rewardRepository = new RewardRepository();
        rewards = rewardRepository.getRewards(project.getId());
        PollRepository pollRepository = new PollRepository();
        polls = pollRepository.getPolls(project.getId());
        LevelRepository levelRepository = new LevelRepository();
        levels = levelRepository.getLevels(project.getId());
        MessageRepository messageRepository = new MessageRepository();
        messages = messageRepository.getMessages(project.getId());
        System.out.println(messages);
        response = new Response(true, "there is your project!");

        return SUCCESS;
    }



    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ArrayList<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }

    public ArrayList<Poll> getPolls() {
        return polls;
    }

    public void setPolls(ArrayList<Poll> polls) {
        this.polls = polls;
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setLevels(ArrayList<Level> levels) {
        this.levels = levels;
    }
}
