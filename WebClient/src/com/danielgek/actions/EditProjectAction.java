package com.danielgek.actions;

import com.danielgek.models.LevelRepository;
import com.danielgek.models.PollRepository;
import com.danielgek.models.ProjectRepository;
import com.danielgek.models.RewardRepository;
import com.opensymphony.xwork2.ActionSupport;

import models.*;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by danielgek on 15/12/15.
 */
public class EditProjectAction extends ActionSupport implements SessionAware {

    Map<String, Object> session;

    private Project project = new Project();
    private ArrayList<Reward> serverRewards = new ArrayList<Reward>();
    private ArrayList<Poll> serverPolls = new ArrayList<Poll>();
    private ArrayList<Level> serverLevels = new ArrayList<Level>();
    private ArrayList<Reward> rewards = new ArrayList<Reward>();
    private ArrayList<Poll> polls = new ArrayList<Poll>();
    private ArrayList<Level> levels = new ArrayList<Level>();
    private Response response;

    @Override
    public String execute() throws Exception {
        RewardRepository rewardRepository = new RewardRepository();
        serverRewards = rewardRepository.getRewards(project.getId());
        PollRepository pollRepository = new PollRepository();
        serverPolls = pollRepository.getPolls(project.getId());
        LevelRepository levelRepository = new LevelRepository();
        serverLevels = levelRepository.getLevels(project.getId());

        System.out.println(serverRewards);
        System.out.println(rewards);

        if(rewards.isEmpty() || serverRewards.isEmpty()){
            if(rewards.isEmpty()){
                for (int i = 0; i < serverRewards.size(); i++) {
                    rewardRepository.delete(serverRewards.get(i).getId());
                }
            }
            if(serverRewards.isEmpty()) {
                for (int i = 0; i < rewards.size(); i++) {
                    rewardRepository.save(rewards.get(i));
                }
            }
        }else{
            for (int i = 0; i < rewards.size(); i++) {
                if(!serverRewards.contains(rewards.get(i))){

                    rewardRepository.save(rewards.get(i));
                }
            }

            for (int i = 0; i < serverRewards.size(); i++) {
                if(!rewards.contains(serverRewards.get(i))){
                    rewardRepository.delete(serverRewards.get(i).getId());
                }
            }
        }

        for (int i = 0; i < polls.size(); i++) {
            if(!serverPolls.contains(polls.get(i))){
                pollRepository.save(polls.get(i));
            }
        }

        for (int i = 0; i < levels.size(); i++) {
            if(!serverLevels.contains(levels.get(i))){
                levelRepository.save(levels.get(i));
            }
        }

        ProjectRepository projectRepository = new ProjectRepository();
        projectRepository.update(project);

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

    public void setLevels(ArrayList<Level> levels) {
        this.levels = levels;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
