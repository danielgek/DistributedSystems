package com.danielgek.actions;

import com.opensymphony.xwork2.ActionSupport;
import models.User;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by danielgek on 29/11/15.
 */
public class CreateProjectAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    private User user;

    @Override
    public String execute() throws Exception {
        this.user = (User) session.get("user");

        return super.execute();
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
}
