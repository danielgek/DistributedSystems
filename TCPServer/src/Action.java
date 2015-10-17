import java.io.Serializable;

/**
 * Created by danielgek on 11/10/15.
 */
public class Action implements Serializable {

    private int action;
    public static final int LOGIN = 0;
    public static final int SIGUP = 1;
    public static final int GET_PROJECT = 2;
    public static final int GET_PROJECTS = 3;
    public static final int CREATE_PROJECT = 4;
    public static final int GET_PROJECT_BY_ADMIN = 5;
    public static final int ADD_LEVEL = 6;
    public static final int GET_LEVELS_BY_PROJECT = 7;
    public static final int ADD_REWARD = 8;
    public static final int GET_REWARDS_BY_PROJECT = 9;
    public static final int ADD_POLL = 10;
    public static final int GET_POLL = 11;
    public static final int GET_POLL_BY_PROJECT = 12;
    public static final int ADD_VOTE = 13;
    public static final int GET_VOTES_BY_POLL = 14;
    public static final int VOTE = 15;



    private Object object;

    public Action(int action) {
        this.action = action;
    }

    public Action(int action, Object object) {
        this.action = action;
        this.object = object;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Action{" +
                "action=" + action +
                ", object=" + (object == null ? "null" : object.toString()) +
                '}';
    }
}
