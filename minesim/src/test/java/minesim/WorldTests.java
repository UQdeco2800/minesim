package minesim;

import org.junit.Test;

import minesim.entities.Peon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WorldTests {
    World w;

    private void setupNewWorld() {
        w = new World(100, 100);
    }

    @Test
    public void testWorldConstructor() {
        setupNewWorld();
        assertEquals(w.getXOffset(), 0);
    }

    @Test
    public void testScrolling() {
        setupNewWorld();
        assertEquals(w.getXOffset(), 0);

        w.moveScreenRight(100);
        assertEquals(100, w.getXOffset());

        w.moveScreenLeft(100);
        assertEquals(0, w.getXOffset());

        w.moveScreenLeft(500);
        assertEquals(0, w.getXOffset());

        w.moveScreenRight(1999);
        assertEquals(1999, w.getXOffset());

        w.moveScreenRight(100);
        assertEquals(2000, w.getXOffset());
    }

    @Test
    public void testAddNewPlayer() {
        setupNewWorld();

        int initial = w.getWorldentities().size();

        Peon p = new Peon(10, 10, 10, 10, "Tester");

        w.addEntityToWorld(p);
        assertEquals(initial + 1, w.getWorldentities().size());
    }

    @Test
    public void removePlayer() {
        setupNewWorld();

        int initial = w.getWorldentities().size();
        Peon p = new Peon(10, 10, 10, 10, "tester");

        w.addEntityToWorld(p);
        assertEquals(initial + 1, w.getWorldentities().size());

        w.removeEntityFromWorld(p);
        w.onTick(0);
        w.onTick(0);
        w.onTick(0);
        assertEquals(initial, w.getWorldentities().size());
    }

    @Test
    public void teleporterTesting() {
        setupNewWorld();
        w.addTeles();
        assertTrue(w.worldContainsTeleporterIn());
        assertTrue(w.worldContainsTeleporterOut());

    }
}
