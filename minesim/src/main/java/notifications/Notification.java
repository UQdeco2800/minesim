package notifications;

import java.util.Date;
import java.util.Objects;

public class Notification {

    private Date receiveTime;
    private String message;


    /**
     * Creates a notification with the message message
     */
    public Notification(String message) {
        this.message = new String(message);
        this.receiveTime = new Date();
    }

    @Override
    public String toString() {
        return message;
    }

    public Date getTime() {
        return receiveTime;
    }

    /**
     * This is a realllly bad equals method that just works on the text/message
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Notification that = (Notification) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiveTime, message);
    }
}