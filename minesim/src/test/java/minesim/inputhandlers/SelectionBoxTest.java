package minesim.inputhandlers;

import org.junit.Test;

import java.awt.geom.Rectangle2D;

import minesim.entities.Peon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sean on 3/09/2015.
 */
public class SelectionBoxTest {

    SelectionBox testBox;

    @Test
    public void testSelectionBoxConstructor() throws Exception {
        SelectionBox box = new SelectionBox(0, 1, 0, 1);
    }

    @Test
    public void testBounds() throws Exception {
        testBox = new SelectionBox(0, 0, 1, 0);
        testBox = new SelectionBox(0, 1, 0, 0);
        testBox = new SelectionBox(-1, 0, 0, 0);
        testBox = new SelectionBox(0, -1, -1, -3);
        testBox = new SelectionBox(1, 0, 0, 1);


    }

    @Test
    public void testEquals() throws Exception {
        SelectionBox box = new SelectionBox(0, 0, 10, 10);
        SelectionBox otherBox = new SelectionBox(0, 0, 10, 10);
        assertTrue(box.equals(otherBox));
    }

    @Test
    public void testRectangle() throws Exception {
        testBox = new SelectionBox(0, 0, 10, 10);
        testBox.setSelectedArea(0, 0, 10, 10);
        Rectangle2D rectangle = new Rectangle2D.Double(0, 0, 10, 10);
        assertTrue(testBox.getRect().equals(rectangle));

        testBox = new SelectionBox(0, 0, 5, 5);
        rectangle = new Rectangle2D.Double(0, 0, 5, 5);
        assertEquals(rectangle, testBox.getRect());

    }

    @Test
    public void testContains() throws Exception {
        Peon someone = new Peon(0, 5, 1, 1, "Someone");
        Peon outcast = new Peon(999, 999, 1, 1, "Outcast");
        testBox = new SelectionBox(0, 0, 10, 10);

        assertTrue(testBox.contains(someone));
        assertFalse(testBox.contains(outcast));

        testBox.clearArea();

        assertFalse(testBox.contains(someone));
        assertFalse(testBox.contains(outcast));

        testBox.setSelectedArea(0, 0, 1000, 1000);

        assertTrue(testBox.contains(someone));
        assertTrue(testBox.contains(outcast));
    }

    @Test
    public void testClear() throws Exception {
        testBox = new SelectionBox(10, 10, 20, 30);
        testBox.clearArea();

        assertEquals(SelectionBox.NOTHING, testBox.getRect());

    }

    @Test
    public void testHashCode() throws Exception {
        testBox = new SelectionBox(0, 1, 3, 4);
        testBox.hashCode();
    }


}
