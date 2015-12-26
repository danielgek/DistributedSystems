package models;

import java.io.Serializable;

/**
 * Created by danielgek on 12/10/15.
 */
public class Message implements Serializable {

    private int id = -1;
    private String message;
    private int sender;
    private int receiver;
    private int projectId;

    public Message() {
    }

    public Message(int id, String message, int sender, int receiver) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Message(String message, int sender, int receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }
    public Message(String message, int sender, int receiver, int projectId) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.projectId = projectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
