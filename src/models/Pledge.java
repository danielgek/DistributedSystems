package models;

import java.io.Serializable;

/**
 * Created by danielgek on 17/10/15.
 */
public class Pledge implements Serializable{

    private int id;
    private int projectId;
    private int userId;
    private double amount;

    public Pledge(int projectId, int userId, double amount) {
        this.projectId = projectId;
        this.userId = userId;
        this.amount = amount;
    }

    public Pledge(int id, int projectId, int userId, double amount) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Pledge{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", amount=" + amount +
                '}';
    }
}
