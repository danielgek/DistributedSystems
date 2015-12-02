package com.danielgek.actions;

import com.danielgek.models.UserRepository;
import com.opensymphony.xwork2.ActionSupport;
import models.User;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by danielgek on 28/11/15.
 */
public class LoginAction extends ActionSupport implements SessionAware{
    /**
     * The model class that stores the message
     * to display in the view.
     */
    private UserRepository userRepository;
    private String password;
    private String username;

    private Map<String,Object> session;

    /*
     * Creates the MessageStore model object and
     * returns success.  The MessageStore model
     * object will be available to the view.
     * (non-Javadoc)
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    public String execute() throws Exception {

        userRepository = new UserRepository();

        if(userRepository.authenticate(username,password)){
            session.put("user",userRepository.getUser());
            return SUCCESS;
        }


        return LOGIN;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
