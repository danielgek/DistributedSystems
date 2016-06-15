package com.danielgek.actions;

import com.danielgek.repositories.UserRepository;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import models.User;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by danielgek on 03/01/16.
 */
public class JoinTumblrAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;

    private String username;
    private String password;
    private String tumblrUsername;

    @Override
    public String execute() throws Exception {
        UserRepository userRepository = new UserRepository();
        System.out.println("username: " + username +  "password: " + password);

        System.out.println(JoinTumblrAction.class.getName() + " tumblr username: " + tumblrUsername);
        if(userRepository.authenticate(username, password)){
            if(userRepository.joinTumblr(tumblrUsername)){
                return "dashboard";
            }else{
                return "index";
            }

        }else{
            return "index";
        }

        //return SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getTumblrUsername() {
        return tumblrUsername;
    }

    public void setTumblrUsername(String tumblrUsername) {
        this.tumblrUsername = tumblrUsername;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
