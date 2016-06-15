package com.danielgek.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import models.Response;
import org.apache.struts2.interceptor.SessionAware;
import rmi.StorageServer;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by danielgek on 02/01/16.
 */
public class TumblrGetAuthAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;

    private String url;
    private StorageServerInterface storageServer;

    Response response = null;

    @Override
    public String execute() throws Exception {

        try {
            storageServer = (StorageServerInterface) Naming.lookup("//127.0.0.1:25055/storageServer");

            this.response = storageServer.getAuthUrl();
        } catch (NotBoundException |MalformedURLException |RemoteException e ) {
            e.printStackTrace();
            this.response = new Response(false,"Error on getting authorization url!");
        }

        return SUCCESS;
    }

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
