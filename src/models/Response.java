package models;

import java.io.Serializable;

/**
 * Created by danielgek on 09/10/15.
 */
public class Response implements Serializable {
    private boolean success;
    private String message;

    private Object object;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message, Object obj) {
        this.success = success;
        this.message = message;
        this.object = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", object=" + (object == null ? "null" : object) +
                '}';
    }
}
