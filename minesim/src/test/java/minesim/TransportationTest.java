package minesim;

import org.junit.Test;

import minesim.DummyApplication;

import org.junit.Before;
import org.testfx.api.FxToolkit;

import minesim.entities.ElevatorShaft;
import minesim.entities.Peon;
import minesim.entities.items.Elevator;
import minesim.entities.items.Ladder;
import minesim.entities.items.Rope;
import minesim.entities.items.Transportation;
import minesim.tasks.ClimbTransportation;
import minesim.tasks.DigTile;
import minesim.tasks.Task;
import minesim.tiles.RollingHillsGenerator;
import minesim.tiles.Tile;
import minesim.tiles.TileChunk;
import minesim.tiles.TileGridManager;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

public class TransportationTest {
	
	@Before
    public void before() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(DummyApplication.class);
    }
	
    Transportation testTransportationRope = new Rope(0, 0, 32, 4, "Rope", 0);
    Transportation testTransportationLadder = new Ladder(0, 0, 32, 32, "Ladder", 0);
    Transportation testTransportationElevator = new Elevator(0, 0, 32, 32, "Elevator", 0);
    Transportation testTransportationElevatorShaft = new ElevatorShaft(0,0,32,32,"ElevatorShaft",0);
    
    Peon testPeon = new Peon(0, 0, 32, 32, "Peon");

    @Test
    public void testInitialisedValues() {
        assertEquals(1, testTransportationRope.getSpeed());
        assertEquals(2, testTransportationLadder.getSpeed());
        assertEquals(4, testTransportationElevator.getSpeed());
        assertEquals(4,testTransportationElevatorShaft.getSpeed());
        
        assertEquals("Rope", testTransportationRope.getName());
        assertEquals("Ladder", testTransportationLadder.getName());
        assertEquals("Elevator", testTransportationElevator.getName());
        assertEquals("ElevatorShaft",testTransportationElevatorShaft.getName());
        
        assertEquals(Optional.of("TransportationImages/rope.png"), testTransportationRope.image);
        assertEquals(Optional.of("TransportationImages/ladder.png"), testTransportationLadder.image);
        assertEquals(Optional.of("TransportationImages/elevator.png"), testTransportationElevator.image);
        assertEquals(Optional.of("TransportationImages/elevatorshaft.png"), testTransportationElevatorShaft.image);
    } 
    
    @Test
    public void testSetGetAddSpeed() {
        testTransportationRope.setSpeed(1);
        assertEquals(1, testTransportationRope.getSpeed());
        testTransportationRope.addSpeed(1);
        assertEquals(2, testTransportationRope.getSpeed());
        
        testTransportationLadder.setSpeed(2);
        assertEquals(2, testTransportationLadder.getSpeed());
        testTransportationLadder.addSpeed(2);
        assertEquals(4, testTransportationLadder.getSpeed());
        
        testTransportationElevator.setSpeed(4);
        assertEquals(4, testTransportationElevator.getSpeed());
        testTransportationElevator.addSpeed(4);
        assertEquals(8, testTransportationElevator.getSpeed());
        
        testTransportationElevatorShaft.setSpeed(4);
        assertEquals(4, testTransportationElevatorShaft.getSpeed());
        testTransportationElevatorShaft.addSpeed(4);
        assertEquals(8, testTransportationElevatorShaft.getSpeed());
    }

    @Test
    public void testSetGetName() {
        testTransportationRope.setName("RopeTest");
        assertEquals("RopeTest", testTransportationRope.getName());
        testTransportationRope.setName("LadderTest");
        assertEquals("LadderTest", testTransportationRope.getName());
        testTransportationRope.setName("ElevatorTest");
        assertEquals("ElevatorTest", testTransportationRope.getName());
        testTransportationRope.setName("ElevatorShaftTest");
        assertEquals("ElevatorShaftTest", testTransportationRope.getName());
    }

    @Test
    public void testClimbTransportation() {
    	Task previousTask = new DigTile(testPeon, 0, 1, testPeon.getParentWorld().getTileManager(), 1);
    	ClimbTransportation climbLadderTask = new ClimbTransportation(testPeon, testTransportationLadder, testTransportationLadder.getXpos(), 
    			testTransportationLadder.getYpos(), previousTask);
    	ClimbTransportation climbRopeTask = new ClimbTransportation(testPeon, testTransportationRope, testTransportationRope.getXpos(), 
    			testTransportationRope.getYpos(), previousTask);
    	ClimbTransportation climbElevatorTask = new ClimbTransportation(testPeon, testTransportationElevator, testTransportationElevator.getXpos(), 
    			testTransportationElevator.getYpos(), previousTask);
    	
    	//Set initial tiredness (which increases by 1 each time you climb)  
        testPeon.setTiredness(100);
        assertEquals(100,testPeon.getTiredness());
    	
    	//Check ladder climbing
        testPeon.updateTask(Optional.of(climbLadderTask));
        assertEquals(0, testPeon.getYpos());
        climbLadderTask.climbDown();
        assertEquals(2, testPeon.getYpos());
        climbLadderTask.climbUp();
        assertEquals(0, testPeon.getYpos());
        
        //Tiredness should have incremented twice
        assertEquals(102,testPeon.getTiredness());
        
        //Check rope climbing
        testPeon.updateTask(Optional.of(climbRopeTask));
        assertEquals(0, testPeon.getYpos());
        climbRopeTask.climbDown();
        assertEquals(1, testPeon.getYpos());
        climbRopeTask.climbUp();
        assertEquals(0, testPeon.getYpos());
        
        //Check elevator climbing
        testPeon.updateTask(Optional.of(climbElevatorTask));
        assertEquals(0, testPeon.getYpos());
        climbElevatorTask.climbDown();
        assertEquals(4, testPeon.getYpos());
        climbElevatorTask.climbUp();
        assertEquals(0, testPeon.getYpos());
    }
    
    @Test
    public void testClearTilePath() {
        TileGridManager manager;
        TileChunk chunk_origin;
        manager = new TileGridManager(new RollingHillsGenerator());
        chunk_origin = new TileChunk();
        manager.addChunk(0, 0, chunk_origin);
        manager.generateWorld();
        TileChunk chunk = new TileChunk();
        
        chunk.setTileType(0, 0, Tile.GRASS);
        manager.addChunk(0, 0, chunk);
        Tile checktile = manager.getTileAtLocation(0,0);
        assertEquals(Tile.GRASS, checktile);
        Transportation.clearTilePath(0,0,1, manager);
        Tile checktile2 = manager.getTileAtLocation(0, 0);
        assertEquals(Tile.AIR, checktile2);
        
        chunk.setTileType(2, 2, Tile.GRASS);
        manager.addChunk(2, 2, chunk);
        Tile checktile3 = manager.getTileAtLocation(2,2);
        assertEquals(Tile.GRASS, checktile3);
        Transportation.clearTilePath(32,32,16, manager);
        Tile checktile4 = manager.getTileAtLocation(2, 2);
        assertEquals(Tile.AIR, checktile4);
    }
    
    @Test
    public void testBuildTransportEntity() {

    	Transportation.transportMode = 1;
    	Transportation.primaryClick = Boolean.FALSE;
    	Transportation.buildTransportEntity(240, 50);
    	assertEquals(1, Transportation.transportMode);
    	
    	Transportation.transportMode = 2;
    	Transportation.primaryClick = Boolean.FALSE;
    	Transportation.buildTransportEntity(240, 50);
    	assertEquals(2, Transportation.transportMode);

    	//Tests for when adjustedEndY > adjustedY
    	World world = World.getInstance();
    	int initial = world.getWorldentities().size();
    	
    	//test for ladder 
    	Transportation.transportMode = 1;
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.buildTransportEntity(240, 50);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(240, Transportation.getStartX());
    	assertEquals(50, Transportation.getStartY());
    	
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.transportType = "ladder";
    	Transportation.buildTransportEntity(240, 100);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(240, Transportation.getEndX());
    	assertEquals(100, Transportation.getEndY());
    	
    	Transportation.buildTransportEntity(240, 100);
    	assertEquals(initial + 2, world.getWorldentities().size());
    	
    	//test for rope
    	Transportation.transportMode = 1;
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.buildTransportEntity(340, 50);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(340, Transportation.getStartX());
    	assertEquals(50, Transportation.getStartY());
    	
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.transportType = "rope";
    	Transportation.buildTransportEntity(340, 100);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(340, Transportation.getEndX());
    	assertEquals(100, Transportation.getEndY());
    	
    	Transportation.buildTransportEntity(340, 100);
    	assertEquals(initial + 4, world.getWorldentities().size());
    	
    	//test for elevator
    	Transportation.transportMode = 1;
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.buildTransportEntity(440, 50);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(440, Transportation.getStartX());
    	assertEquals(50, Transportation.getStartY());
    	
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.transportType = "elevator";
    	Transportation.buildTransportEntity(440, 100);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(440, Transportation.getEndX());
    	assertEquals(100, Transportation.getEndY());
    	
    	Transportation.buildTransportEntity(440, 100);
    	assertEquals(initial + 5, world.getWorldentities().size());
    	
    	//Tests for when adjustedEndY > adjustedY
    	World world2 = World.getInstance();
    	int initial2 = world2.getWorldentities().size();
    	
    	//test for ladder 
    	Transportation.transportMode = 1;
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.buildTransportEntity(240, 100);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(240, Transportation.getStartX());
    	assertEquals(100, Transportation.getStartY());
    	
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.transportType = "ladder";
    	Transportation.buildTransportEntity(240, 50);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(240, Transportation.getEndX());
    	assertEquals(50, Transportation.getEndY());
    	
    	Transportation.buildTransportEntity(240, 50);
    	assertEquals(initial2 + 2, world2.getWorldentities().size());
    	
    	//test for rope
    	Transportation.transportMode = 1;
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.buildTransportEntity(340, 100);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(340, Transportation.getStartX());
    	assertEquals(100, Transportation.getStartY());
    	
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.transportType = "rope";
    	Transportation.buildTransportEntity(340, 50);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(340, Transportation.getEndX());
    	assertEquals(50, Transportation.getEndY());
    	
    	Transportation.buildTransportEntity(340, 50);
    	assertEquals(initial2 + 4, world2.getWorldentities().size());
    	
    	//test for elevator
    	Transportation.transportMode = 1;
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.buildTransportEntity(440, 100);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(440, Transportation.getStartX());
    	assertEquals(100, Transportation.getStartY());
    	
    	Transportation.primaryClick = Boolean.TRUE;
    	Transportation.transportType = "elevator";
    	Transportation.buildTransportEntity(440, 50);
    	assertEquals(Boolean.FALSE, Transportation.primaryClick);
    	assertEquals(440, Transportation.getEndX());
    	assertEquals(50, Transportation.getEndY());
    	
    	Transportation.buildTransportEntity(440, 50);
    	assertEquals(initial2 + 5, world2.getWorldentities().size());
    	
    }
    
    @Test
    public void testSetStart() {
    	Transportation testTransportationRope = new Rope(0, 0, 32, 4, "Rope", 0);
    	testTransportationRope.setStart(100, 100);
    	assertEquals(100,Transportation.getStartX());
    	assertEquals(100,Transportation.getStartY());
    	
    }
}
