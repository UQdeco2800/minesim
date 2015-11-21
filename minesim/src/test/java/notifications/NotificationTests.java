package notifications;

import org.junit.Test;

import java.util.Date;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Michael on 21/09/2015.
 */
public class NotificationTests {

    @Test
    public void creationTest() {
        int bound1; int bound2;
        bound1 = (int) new Date().getTime();
        Notification n = new Notification("foo");
        bound2 = (int) new Date().getTime();

        String expected = "foo";
        int nTime = (int) n.getTime().getTime();
        assertEquals(expected, n.toString());
        assertTrue(bound1 <= nTime);
        assertTrue(bound2 >= nTime);
    }

    @Test
    public void hashTest() {
        Notification n = new Notification("foo");
        int expected = Objects.hash(n.getTime(), n.toString());
        assertEquals(n.hashCode(), expected);
    }

    @Test
    public void equalsTest() {
        Notification n = new Notification("bar");
        Notification n2 = new Notification("bar");
        assertTrue(n.equals(n));
        assertFalse(n.equals(new String("foo")));
        assertFalse(n.equals(null));
        assertTrue(n.equals(n2));
    }
}
