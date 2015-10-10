package models;

import java.io.Serializable;

/**
 * Created by danielgek on 09/10/15.
 */
public class Reward implements Serializable {

    private int id;
    private int projectId;
    private String description;
    private double value;

    public Reward(int id, int projectId, String description, double value) {
        this.id = id;
        this.projectId = projectId;
        this.description = description;
        this.value = value;
    }

    public Reward(int projectId, String description, double value) {
        this.projectId = projectId;
        this.description = description;
        this.value = value;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
