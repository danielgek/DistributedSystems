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
    public static final int GET_OLD_PROJECTS = 4;
    public static final int CREATE_PROJECT = 5;
    public static final int REMOVE_PROJECT = 6;
    public static final int GET_PROJECT_BY_ADMIN = 7;
    public static final int ADD_LEVEL = 8;
    public static final int GET_LEVELS_BY_PROJECT = 9;
    public static final int REMOVE_REWARD = 11;
    public static final int GET_REWARDS_BY_PROJECT = 12;
    public static final int GET_CURRENT_REWARDS = 13;
    public static final int ADD_REWARD = 14;
    public static final int ADD_POLL = 15;
    public static final int GET_POLL = 16;
    public static final int GET_POLL_BY_PROJECT = 17;
    public static final int ADD_VOTE = 18;
    public static final int GET_VOTES_BY_POLL = 19;
    public static final int PLEDGE = 20;
    public static final int SEND_MESSAGE = 21;
    public static final int GET_MESSAGES = 22;
    public static final int GET_USER = 23;



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
                "action=" + this.getClass().getDeclaredFields()[action + 1].getName() +
                ", object= " + (object == null ? "null" : object.toString()) +
                '}';
    }
}
