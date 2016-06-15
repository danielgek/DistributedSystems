package com.danielgek.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import models.Response;
import models.User;
import org.apache.struts2.interceptor.SessionAware;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by danielgek on 02/01/16.
 */
public class TumblrResultAction extends ActionSupport implements SessionAware{
    private Map<String, Object> session;

    private String oauthToken;
    private String oauthVerifier;
    private StorageServerInterface storageServer;
    private Response response;
    private String tumblrUsername = null;
    private User user;

    @Override
    public String execute() throws Exception {
        if(ActionContext.getContext().getParameters().containsKey("oauth_token") && ActionContext.getContext().getParameters().containsKey("oauth_verifier")){
            this.oauthToken = ((String []) ActionContext.getContext().getParameters().get("oauth_token"))[0];
            this.oauthVerifier = ((String [])  ActionContext.getContext().getParameters().get("oauth_verifier"))[0];
        }else{
            return "index";
        }


        try {
            storageServer = (StorageServerInterface) Naming.lookup("//127.0.0.1:25055/storageServer");
            response = storageServer.tumblrLogin(oauthVerifier);
            if(response.isSuccess()){
                if(response.getMessage().equals("registered")){
                    tumblrUsername = (String) response.getObject();
                    return "joinUser";
                }else{
                    tumblrUsername = response.getMessage();
                    user = (User) response.getObject();
                    return "loggedIn";
                }


                //System.out.println(TumblrResultAction.class.getName() + " logged in tumblr username: " + tumblrUsername);

            }else{
                tumblrUsername = (String) response.getObject();
                System.out.println(TumblrResultAction.class.getName() + " join user tumblr username: " + tumblrUsername);
                return "joinUser";
            }

        } catch (NotBoundException |MalformedURLException |RemoteException e ) {
            e.printStackTrace();
            response = new Response(false,"Error on getting authorization url!");
        }
        return SUCCESS;
    }


    public Response getResponse() {
        return response;
    }

    public String getTumblrUsername() {
        return tumblrUsername;
    }

    public void setTumblrUsername(String tumblrUsername) {
        this.tumblrUsername = tumblrUsername;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
