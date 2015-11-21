package minesim.functions;

import org.junit.Test;

import minesim.World;
import minesim.entities.Peon;
import minesim.entities.PeonGuard;
import minesim.entities.PeonMiner;
import minesim.inputhandlers.MouseHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddEntityOnMouseClickTest {

    /**
     * Add a new entity into the world successfully
     */
    @Test
    public void testAddEntitySuccess() {
        World myWorld = genPopulatedWorld();
        AddEntityOnMouseClick.entityList(new PeonMiner(0, 0, 32, 32, "Miner"));
        AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
        AddEntityOnMouseClick.moveEntity(0, 0, myWorld);
        assertTrue(AddEntityOnMouseClick.addEntity(0, 0, myWorld));
    }

    /**
     * Add a new entity into the world but detects tile
     */
    @Test
    public void testAddEntityFail() {
        World myWorld = genPopulatedWorld();
        AddEntityOnMouseClick.entityList(new PeonMiner(0, 0, 32, 32, "Miner"));
        AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
        AddEntityOnMouseClick.moveEntity(0, 0, myWorld);
        assertFalse(AddEntityOnMouseClick.addEntity(400, 700, myWorld));
    }

    /**
     * Test that the build mode toggle is being activated and deactivated
     * correctly
     */
    @Test
    public void testSetBuildMode() {
        AddEntityOnMouseClick.setBuildMode(Boolean.FALSE);
        assertEquals(Boolean.FALSE, AddEntityOnMouseClick.getBuildMode());
        AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
        assertEquals(Boolean.TRUE, AddEntityOnMouseClick.getBuildMode());
    }

    private World genPopulatedWorld() {
        World world = new World(100, 100);
        world.addEntityToWorld(new Peon(1, 1, 1, 1, "Derek"));
        world.addEntityToWorld(new PeonGuard(4, 4, 1, 1, "Bruce"));
        world.addEntityToWorld(new PeonMiner(3, 2, 1, 1, "Alison"));
        return world;
    }

}
