package models;

import java.io.Serializable;

/**
 * Created by danielgek on 12/10/15.
 */
public class Poll implements Serializable {
    private int id = -1;
    private String title;
    private String description;
    private int projectId;
    private String answer1;
    private String answer2;

    public Poll(){}

    public Poll(int id, String title, String description, int projectId, String answer1, String answer2) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.projectId = projectId;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    public Poll(String title, String description, int projectId, String answer1, String answer2) {
        this.title = title;
        this.description = description;
        this.projectId = projectId;
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Poll poll = (Poll) o;

        if (getProjectId() != poll.getProjectId()) return false;
        if (getTitle() != null ? !getTitle().equals(poll.getTitle()) : poll.getTitle() != null) return false;
        if (getDescription() != null ? !getDescription().equals(poll.getDescription()) : poll.getDescription() != null)
            return false;
        if (getAnswer1() != null ? !getAnswer1().equals(poll.getAnswer1()) : poll.getAnswer1() != null) return false;
        return !(getAnswer2() != null ? !getAnswer2().equals(poll.getAnswer2()) : poll.getAnswer2() != null);

    }



    @Override
    public String toString() {
        return "Poll{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", projectId=" + projectId +
                ", answer1='" + answer1 + '\'' +
                ", answer2='" + answer2 + '\'' +
                '}';
    }
}
