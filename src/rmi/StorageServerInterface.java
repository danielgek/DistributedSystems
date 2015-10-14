package rmi;

import models.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by danielgek on 05/10/15.
 */
public interface StorageServerInterface extends Remote{
    Response register(User user) throws RemoteException;
    Response login(User user) throws RemoteException;
    Response createProject(Project project) throws RemoteException;
    Response getProject(int id) throws RemoteException;
    Response createReward(Reward reward) throws RemoteException;
    Response getRewards(int projectId) throws RemoteException;
    Response createLevel(Level level) throws RemoteException;
    Response getLevels(int projectId) throws RemoteException;
    Response createPoll(Poll poll) throws RemoteException;
    Response getPolls(int projectId) throws RemoteException;
    Response createVote(Vote vote) throws RemoteException;
    Response getVotes(int pollId) throws RemoteException;
}
