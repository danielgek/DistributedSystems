package models;

import java.io.Serializable;

/**
 * Created by danielgek on 12/10/15.
 */
public class Poll implements Serializable {
    private int id = -1;
    private String title;
    private String description;
    private int idProject;
    private String answer1;
    private String answer2;



    public Poll(int id, String title, String description, int idProject, String answer1, String answer2) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.idProject = idProject;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    public Poll(String title, String description, int idProject, String answer1, String answer2) {
        this.title = title;
        this.description = description;
        this.idProject = idProject;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", idProject=" + idProject +
                ", answer1='" + answer1 + '\'' +
                ", answer2='" + answer2 + '\'' +
                '}';
    }
}
