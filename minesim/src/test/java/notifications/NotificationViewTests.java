package notifications;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import minesim.DummyApplication;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Michael on 21/09/2015.
 */
public class NotificationViewTests {

    @Before
    public void before() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(DummyApplication.class);
    }

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();
    }

    @Test
    public void updateTest() {
        ListView<String> list = new ListView<String>();
        NotificationView nV = NotificationView.getInstance();
        nV.setListView(list);

        Notification notification1 = new Notification("foo");
        Notification notification2 = new Notification("bar");
        List<Notification> fooList = new ArrayList<>();
        fooList.add(notification1);
        fooList.add(notification2);

        String[] expected = new String[2];
        expected[0] = notification1.toString();
        expected[1] = notification2.toString();

        nV.update(fooList);
        try {
            waitForRunLater();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ObservableList<String> received = list.getItems();
        assertArrayEquals(received.toArray(), expected);
    }

}
