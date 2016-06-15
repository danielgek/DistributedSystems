package com.danielgek.repositories;

import models.Response;
import models.Reward;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by danielgek on 08/12/15.
 */
public class RewardRepository {
    private StorageServerInterface storageServer;
    private Reward reward;
    private ArrayList<Reward> rewards;

    public RewardRepository() {
        try {
            storageServer = (StorageServerInterface) Naming.lookup("//127.0.0.1:25055/storageServer");
        } catch (NotBoundException |MalformedURLException |RemoteException e ) {
            e.printStackTrace();
        }
    }

    public boolean save(Reward reward){

        try {
            Response response = storageServer.addReward(reward);
            if (response.isSuccess()){
                this.reward = (Reward) response.getObject();
                return true;
            }else{
                return false;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean save(ArrayList<Reward> rewards){

        try {
            for (int i = 0; i < rewards.size(); i++) {
                Response response = storageServer.addReward(rewards.get(i));
                if (response.isSuccess()){
                    //this.reward = (Reward) response.getObject();
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

    public boolean delete(int id){

        try {
            Response response = storageServer.removeReward(id);
            if (response.isSuccess()){
                return true;
            }else{
                return false;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }


    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public ArrayList<Reward> getRewards() {
        return rewards;
    }

    public ArrayList<Reward> getRewards(int id) {
        try {
            Response response = storageServer.getRewards(id);
            if (response.isSuccess()){
                return (ArrayList<Reward>) response.getObject();
            }else{
                return null;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }
}
