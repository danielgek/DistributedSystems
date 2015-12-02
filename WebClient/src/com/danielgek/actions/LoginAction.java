package com.danielgek.actions;

import com.danielgek.models.UserRepository;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import models.User;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.json.annotations.JSON;

import java.util.Map;
import java.util.logging.LogManager;

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
    private User user;





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
            user = userRepository.getUser();
            return SUCCESS;
        }


        return LOGIN;
    }
    @JSON(serialize=false)
    public UserRepository getUserRepository() {
        return userRepository;
    }
    @JSON(serialize=false)
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @JSON(serialize=false)
    public void setPassword(String password) {
        this.password = password;
    }

    @JSON(serialize=false)
    public String getPassword() {
        return password;
    }
    @JSON(serialize=false)
    public void setUsername(String username) {
        this.username = username;
    }
    @JSON(serialize=false)
    public String getUsername() {
        return username;
    }

    public User getUser(){
        return this.user;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
