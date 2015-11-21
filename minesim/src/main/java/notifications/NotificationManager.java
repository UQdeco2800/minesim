package notifications;

// Imports

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the notifications created by the simulator.
 */
public class NotificationManager {
    private static final Logger LOGGER =
            Logger.getLogger(NotificationManager.class);
    private static NotificationManager instance = new NotificationManager();
    private NotificationView nV = NotificationView.getInstance();

    // The list of notifications
    private ArrayList<Notification> notifications = new ArrayList<>();

    // Make the constructor private so that this class cannot be instantiated
    private NotificationManager() {
    }

    /**
     * Ensures there is only one of this object in existence.
     *
     * @return instance the singleton instance of this object
     */
    public static NotificationManager getInstance() {
        return instance;
    }

    /**
     * Called at some point during the game, registers the notification with the
     * manager.
     *
     * @param message This is the specific text of the notification
     */
    public static void notify(String message) {
        NotificationManager nm = NotificationManager.getInstance();
        nm.notifications.add(new Notification(message));
        LOGGER.debug(nm.notifications.get(
                nm.notifications.size() - 1).toString());
        nm.out();
    }

    /**
     * Sends out the current notifications to the viewing system.
     */
    private void out() {
        nV.update(notifications);
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public void clear() {
        notifications.clear();
        nV.update(notifications); // push the empty list to view
    }

    @Override
    public String toString() {
        return notifications.toString();
    }
}