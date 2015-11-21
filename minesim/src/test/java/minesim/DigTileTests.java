package minesim;

import org.junit.Test;

import minesim.entities.Peon;
import minesim.entities.PeonMiner;
import minesim.tasks.DigTile;
import minesim.tasks.Task;
import minesim.tiles.Tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DigTileTests {
    World testWorld;
    Task testTask;
    Peon testMiner = new PeonMiner(0, 0, 50, 50, "TestMiner");

    private void setupNewWorld() {
        testWorld = new World(1000, 1000);
        testWorld.getTileManager().setTileAtLocation(0, 1, Tile.DIRT);
        testTask = new DigTile(testMiner, 0, 1, testWorld.getTileManager(), 1);
        testTask.setPeon(testMiner);
    }

    /**
     * Test the tiredness and annoyance of the peon before and after digging a
     * tile
     */

    //@Ignore @Test
    public void testTiredness() {
        setupNewWorld();
        assertEquals(testMiner.getAnnoyance(), 0);
        assertEquals(testMiner.getTiredness(), 0);
        testWorld.addTask(testTask);
        testMiner.getCurrentTask().get().doTask();
        assertEquals(testMiner.getAnnoyance(), 1);
        assertEquals(testMiner.getTiredness(), 2);
    }

    /**
     * Test the tile type changes before and after digging a tile
     */

    //@Ignore @Test
    public void testTileType() {
        setupNewWorld();
        assertTrue(testWorld.getTileManager().getTileAtLocation(0, 1) != Tile.AIR);
        testWorld.addTask(testTask);
        testMiner.getCurrentTask().get().doTask();
        assertTrue(testWorld.getTileManager().getTileAtLocation(0, 1) == Tile.AIR);
    }

    /**
     * Test exception when out of boundary
     */
    @Test
    public void testBoundary() throws IndexOutOfBoundsException {
        setupNewWorld();
        testWorld.addTask(new DigTile(new Peon(0, 0, 50, 50, "tempPeon"), 1100, 1100, testWorld.getTileManager(), 10));
    }

}
