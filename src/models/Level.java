package models;

import java.io.Serializable;

/**
 * Created by danielgek on 12/10/15.
 */
public class Level implements Serializable {
    private int id;
    private float goal;
    private String description;
    private int idProject;

    public Level(int id, float goal, String description, int idProject) {
        this.id = id;
        this.goal = goal;
        this.description = description;
        this.idProject = idProject;
    }

    public Level(float goal, String description, int idProject) {
        this.goal = goal;
        this.description = description;
        this.idProject = idProject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getGoal() {
        return goal;
    }

    public void setGoal(float goal) {
        this.goal = goal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    @Override
    public String toString() {
        return "Level{" +
                "id=" + id +
                ", goal=" + goal +
                ", description='" + description + '\'' +
                ", idProject=" + idProject +
                '}';
    }
}
