package minesim;

import org.junit.Test;

import minesim.entities.Peon;
import minesim.entities.PeonMiner;
import minesim.tasks.DigTile;
import minesim.tasks.Task;

import static org.junit.Assert.assertTrue;

public class AutoTaskTests {
    World testWorld;
    Task testTask;
    Peon testPeon = new Peon(0, 0, 50, 50, "TestPeon");
    Peon testMiner = new PeonMiner(0, 0, 50, 50, "TestMiner");

    private void setupNewWorld() {
        testWorld = new World(1000, 1000);
        testTask = new DigTile(new Peon(0, 0, 50, 50, "tempPeon"), 1, 1, testWorld.getTileManager(), 0);
    }


    @Test
    public void testAddRemoveTasks() {
        setupNewWorld();
        assertTrue(testWorld.getWorldTasks(testMiner).isEmpty());
        testWorld.addTask(testTask);
        assertTrue(!testWorld.getWorldTasks(testMiner).isEmpty());
        testWorld.removeTask(testTask);
        assertTrue(testWorld.getWorldTasks(testMiner).isEmpty());
    }
}
