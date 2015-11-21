package notifications;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;


public class NotificationView {

    // The one and only instance of this class
    private static NotificationView instance = new NotificationView();
    // The thing displaying the notifications
    private ListView<String> notifDisplay;

    // Make the constructor private so that this class cannot be instantiated
    private NotificationView() {
    }


    /**
     * Ensures there is only one of this object in existence.
     *
     * @return instance the singleton instance of this object
     */
    public static NotificationView getInstance() {
        return instance;
    }


    /**
     * This is used during the WindowHandler classes's setup.
     *
     * @param listView The javafx listview object
     */
    public void setListView(ListView<String> listView) {
        this.notifDisplay = listView;
    }

    /**
     * Updates the UI's notifications with the notifications passed to it.
     */
    public void update(List<Notification> notifications) {
        final ObservableList<String> list = FXCollections.observableArrayList(
                this.toStringArray(notifications));
        Platform.runLater(() -> notifDisplay.setItems(list));
    }

    /**
     * Converts a Notification list into a list of strings. This is mainly used
     * for displaying the notifications.
     */
    private List<String> toStringArray(List<Notification> notifs) {
        List<String> strings = new ArrayList<>();
        notifs.forEach((Notification notif) -> strings.add(notif.toString()));
        return strings;
    }

}