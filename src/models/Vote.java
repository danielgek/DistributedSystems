package models;

import java.io.Serializable;

/**
 * Created by danielgek on 12/10/15.
 */
public class Vote implements Serializable {

    private int id = -1;
    private int idPoll;
    private int option;

    public Vote(int id, int idPoll, int option) {
        this.id = id;
        this.idPoll = idPoll;
        this.option = option;
    }

    public Vote(int idPoll, int option) {
        this.idPoll = idPoll;
        this.option = option;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(int idPoll) {
        this.idPoll = idPoll;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", idPoll=" + idPoll +
                ", option=" + option +
                '}';
    }
}
