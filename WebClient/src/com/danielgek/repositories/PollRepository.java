package com.danielgek.repositories;

import models.Poll;
import models.Response;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by danielgek on 08/12/15.
 */
public class PollRepository {
    private StorageServerInterface storageServer;
    private ArrayList<Poll> polls;
    private Poll poll;


    public PollRepository() {
        try {
            storageServer = (StorageServerInterface) Naming.lookup("//127.0.0.1:25055/storageServer");
        } catch (NotBoundException |MalformedURLException |RemoteException e ) {
            e.printStackTrace();
        }
    }

    public boolean save(Poll poll){
        try {
            Response response = storageServer.addPoll(poll);
            if (response.isSuccess()){
                this.poll = (Poll) response.getObject();
                return true;
            }else{
                return false;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }


    }
    public boolean save(ArrayList<Poll> polls){
        try {
            for (int i = 0; i < polls.size(); i++) {
                Response response = storageServer.addPoll(polls.get(i));
                if (response.isSuccess()){
                    this.polls = (ArrayList<Poll>) response.getObject();
                    return true;
                }else{
                    return false;
                }
            }
            return false;


        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }



    public ArrayList<Poll> getPolls(int id) {
        try {
            Response response = storageServer.getPolls(id);
            return (ArrayList<Poll>) response.getObject();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPolls(ArrayList<Poll> polls) {
        this.polls = polls;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
