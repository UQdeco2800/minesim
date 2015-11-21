package notifications;

import minesim.DummyApplication;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NotificationManagerTests {

    @Before
    public void before() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(DummyApplication.class);
    }

    @Test
    public void clearTest() {
        NotificationManager nm = NotificationManager.getInstance();
        NotificationManager.notify("1");
        NotificationManager.notify("2");
        NotificationManager.notify("3");
        nm.clear();
        assertEquals("There should be no notifications",
                0,
                nm.getNotifications().size());
    }

    @Test
    public void notifyTest() {
        NotificationManager nm = NotificationManager.getInstance();
        nm.clear();
        String str = "Test1";
        NotificationManager.notify(str);
        Notification notification = new Notification(str);
        ArrayList<Notification> expected = new ArrayList<>();
        expected.add(notification);
        assertArrayEquals(nm.getNotifications().toArray(), expected.toArray());
    }

    @Test
    public void stringTest() {
        NotificationManager nm = NotificationManager.getInstance();
        nm.clear();
        String str = "Notification1";
        String str2 = "Notification2";
        nm.notify(str);
        nm.notify(str2);
        String nmOutput = nm.toString();

        ArrayList<String> expected = new ArrayList<>();
        expected.add(str);
        expected.add(str2);

        assertEquals("String output should be the same", nmOutput, expected.toString());
    }

}