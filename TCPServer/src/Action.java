import java.io.Serializable;

/**
 * Created by danielgek on 11/10/15.
 */
public class Action implements Serializable {
    private int action;
    public final static int LOGIN = 0;
    public final static int SIGUP = 1;
    public final static int GET_PROJECT = 2;
    public final static int CREATE_PROJECT = 3;
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
}