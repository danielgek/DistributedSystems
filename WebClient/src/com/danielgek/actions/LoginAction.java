package com.danielgek.actions;

import com.danielgek.repositories.UserRepository;
import com.opensymphony.xwork2.ActionSupport;
import models.Response;
import models.User;
import org.apache.struts2.interceptor.SessionAware;

import org.apache.struts2.json.annotations.JSON;

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
    private User user;
    private Response response;



    public String execute() throws Exception {

        userRepository = new UserRepository();

        System.out.println(username);
        System.out.println(password);

        if(userRepository.authenticate(username,password)){
            user = userRepository.getUser();
            response = new Response(true,"LoggedIn!",user);
            session.put("user", user);
            return SUCCESS;
        }


        response = new Response(false,"Username or password wrong!");
        return LOGIN;
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

    /*public User getUser(){
        return this.user;
    }*/

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
