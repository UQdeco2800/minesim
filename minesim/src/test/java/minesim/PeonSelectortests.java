package minesim;

import org.junit.Ignore;
import org.junit.Test;

import minesim.controllers.PeonSelectorController;
import minesim.World;
import minesim.entities.Peon;
import minesim.entities.PeonGuard;
import minesim.entities.PeonMiner;
import minesim.entities.WorldEntity;
import minesim.inputhandlers.MouseHandler;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;



public class PeonSelectortests {
	
	private PeonSelectorController myController = new PeonSelectorController();
	private World myWorld;
	private MouseHandler myMouseHandler;

    @BeforeClass
    public static void testInit() {
    	World myWorld = genPopulatedWorld();
    	MouseHandler myMouseHandler =  new MouseHandler(myWorld);
    }

    /**
	 * Tests that selectAllPeons is selecting and de-selecting correctly
	 */
    @Ignore @Test
	public void testSelectAll(){
    	MouseHandler myMouseHandler =  new MouseHandler(myWorld);
		myController.selectAllPeons();
		System.out.println(myWorld.getWorldentities().size());
		assertTrue(MouseHandler.registeredEntities.size() == 5); // should be 5
		myController.selectAllPeons(); //De-select
		assertTrue(MouseHandler.registeredEntities.size()  == 0); // then 0
	}
	
    /**
	 * Tests that selectIdlePeons is selecting and de-selecting correctly
	 */
	@Ignore @Test
	public void testSelectIdle(){
		myController.selectIdle();
		assertTrue(myMouseHandler.registeredEntities.size() == 5); // should be 5
		myController.selectIdle(); //De-select
		assertTrue(myMouseHandler.registeredEntities.size()  == 0); // then 0
	}

    /**
	 * Tests that selectMiners is selecting and de-selecting correctly
	 */
	@Ignore @Test
	public void testSelectMiners(){
		myController.selectMiners();
		assertTrue(myMouseHandler.registeredEntities.size() == 5); // should be 5
		myController.selectMiners(); //De-select
		assertTrue(myMouseHandler.registeredEntities.size()  == 0); // then 0
	}
	
	/**
	 * Tests that only Peons without tasks are added to the Idle group
	 */
	@Test
	public void testIdle(){
		World world = genPopulatedWorld();
		for(WorldEntity entity : world.getWorldentities()) {
			assertTrue(PeonSelectorController.isIdle(entity));
		}
	}
	
	/**
	 * Tests the moveCamera function
	 */
	@Ignore @Test
	public void testMoveCamera(){
		int x, y;
		myController.moveCamera(0, 0);
		x = World.getInstance().getXOffset();
		y = World.getInstance().getYOffset();
		assertEquals(x, 0);
		assertEquals(y, 0);
	}
	
    /**
     * Create the world for testing
     */
    private static World genPopulatedWorld() {
        World world = new World(100, 100);
        world.addEntityToWorld(new Peon(1, 1, 1, 1, "Bye"));
        world.addEntityToWorld(new PeonGuard(4, 4, 1, 1, "Tony"));
        world.addEntityToWorld(new PeonMiner(3, 2, 1, 1, "Onions"));
        return world;
    }
}