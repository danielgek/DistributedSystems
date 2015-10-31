package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danielgek on 09/10/15.
 */
public class Project implements Serializable {

    private int id = -1;
    private int adminId;
    private String name;
    private String description;
    private double objective;
    private double progress;
    private Date limit;

    public Project(int id, int adminId, String name, String description, double objective, Date limit) {
        this.id = id;
        this.adminId = adminId;
        this.name = name;
        this.description = description;
        this.objective = objective;
        this.limit = limit;
        this.progress = 0;
    }

    public Project(int id, int adminId, String name, String description, double objective, Date limit, double progress) {
        this.id = id;
        this.adminId = adminId;
        this.name = name;
        this.description = description;
        this.objective = objective;
        this.limit = limit;
        this.progress = progress;
    }

    public Project(int adminId, String name, String description, double objective, Date limit) {
        this.adminId = adminId;
        this.name = name;
        this.description = description;
        this.objective = objective;
        this.limit = limit;
        this.progress = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getObjective() {
        return objective;
    }

    public void setObjective(double objective) {
        this.objective = objective;
    }

    public Date getLimit() {
        return limit;
    }

    public void setLimit(Date limit) {
        this.limit = limit;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public boolean reatched(){
        return progress >= objective;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", adminId=" + adminId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", objective=" + objective +
                ", progress=" + progress +
                ", limit=" + limit +
                '}';
    }
}
