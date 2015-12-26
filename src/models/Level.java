package models;

import java.io.Serializable;

/**
 * Created by danielgek on 12/10/15.
 */
public class Level implements Serializable {
    private int id;
    private double goal;
    private String description;
    private int projectId;

    public Level(int id, double goal, String description, int projectId) {
        this.id = id;
        this.goal = goal;
        this.description = description;
        this.projectId = projectId;
    }

    public Level(double goal, String description, int projectId) {
        this.goal = goal;
        this.description = description;
        this.projectId = projectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Level level = (Level) o;

        if (Double.compare(level.getGoal(), getGoal()) != 0) return false;
        if (getProjectId() != level.getProjectId()) return false;
        return !(getDescription() != null ? !getDescription().equals(level.getDescription()) : level.getDescription() != null);

    }



    @Override
    public String toString() {
        return "Level{" +
                "id=" + id +
                ", goal=" + goal +
                ", description='" + description + '\'' +
                ", projectId=" + projectId +
                '}';
    }
}
