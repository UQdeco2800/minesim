package minesim;

import org.junit.Test;

import minesim.entities.Peon;
import minesim.entities.PeonGather;
import minesim.entities.PeonGuard;
import minesim.entities.PeonMiner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PeonTests {
    //create one of each peon class
    Peon testPeon = new Peon(0, 0, 50, 50, "TestPeon");
    //test PeonMiner
    PeonMiner testMiner = new PeonMiner(0, 0, 50, 50, "testMiner");
    //gatherer tests
    PeonGather testGather = new PeonGather(0, 0, 50, 50, "TestGather");
    PeonGuard testGuard = new PeonGuard(0, 0, 50, 50, "TestGuard");

    /**
     * Test that the Peon is created correctly
     */
    @Test
    public void testPeonInitial() {
        //check the Peon values are as they should be
        assertEquals(100, testPeon.getHappiness());
        assertEquals(0, testPeon.getAnnoyance());
        assertEquals(0, testPeon.getTiredness());
        assertTrue((testPeon.getStrength() >= 1) && (testPeon.getStrength() < 6));
        assertEquals(1, testPeon.getSpeed());
        assertEquals(100, testPeon.getHealth());
        assertEquals(100, testPeon.getRecoveryRate());
        
    }

    /**
     * Test the setter (setStat, addStat) methods for Peon
     */
    @Test
    public void testPeonSetter() {
        testPeon.setHappiness(50);
        assertEquals(50, testPeon.getHappiness());
        testPeon.addHappiness(10);
        assertEquals(60, testPeon.getHappiness());
        testPeon.setAnnoyance(50);
        assertEquals(50, testPeon.getAnnoyance());
        testPeon.addAnnoyance(10);
        assertEquals(60, testPeon.getAnnoyance());
        testPeon.setTiredness(50);
        assertEquals(50, testPeon.getTiredness());
        testPeon.addTiredness(10);
        assertEquals(60, testPeon.getTiredness());
        testPeon.setStrength(3);
        assertEquals(3, testPeon.getStrength());
        testPeon.addStrength(1);
        assertEquals(4, testPeon.getStrength());
        testPeon.setSpeed(5);
        assertEquals(5, testPeon.getSpeed());
        testPeon.addSpeed(1);
        assertEquals(6, testPeon.getSpeed());
        testPeon.setHealth(50);
        assertEquals(50, testPeon.getHealth());
        testPeon.subtractHealth(10);
        assertEquals(40, testPeon.getHealth());
        testPeon.setRecoveryRate(50);
        assertEquals(50, testPeon.getRecoveryRate());
    }

    /**
     * test setting stats to be above the maximum
     */
    @Test
    public void testSetHigh() {
        testPeon.setHappiness(110);
        assertEquals(100, testPeon.getHappiness());
        testPeon.setAnnoyance(1010);
        assertEquals(1000, testPeon.getAnnoyance());
        testPeon.setTiredness(1010);
        assertEquals(1000, testPeon.getTiredness());
        testPeon.setStrength(11);
        assertEquals(10, testPeon.getStrength());
        testPeon.setSpeed(11);
        assertEquals(10, testPeon.getSpeed());
        testPeon.setHealth(110);
        assertEquals(100, testPeon.getHealth());
    }

    /**
     * Test the jump make jump of the Peon
     */
    @Test
    public void testMakeJump() {
        try {
            testPeon.makePeonJump();
            testPeon.makePeonJump();
        } catch (Exception e) {
            throw (e);
        }
    }

    /**
     * test setting stats below the minimum
     */
    @Test
    public void testSetLow() {
        testPeon.setHappiness(-10);
        assertEquals(0, testPeon.getHappiness());
        testPeon.setAnnoyance(-10);
        assertEquals(0, testPeon.getAnnoyance());
        testPeon.setTiredness(-10);
        assertEquals(0, testPeon.getTiredness());
        testPeon.setStrength(-1);
        assertEquals(1, testPeon.getStrength());
        testPeon.setSpeed(-1);
        assertEquals(1, testPeon.getSpeed());
    }

    /**
     * test taskTiredness method
     */
    @Test
    public void testTaskTiredness() {
        testPeon.setTiredness(100);
        testPeon.setAnnoyance(100);
        testPeon.taskTiredness(10, 10);
        assertEquals(110, testPeon.getTiredness());
        assertEquals(110, testPeon.getAnnoyance());
    }

    /**
     * test mod get and set
     */
    @Test
    public void testMinerMod() {
        assertTrue(1.5 == testMiner.getMod());
        testMiner.setMod(2.0);
        assertTrue(2.0 == testMiner.getMod());
        testMiner.addMod(.5);
        assertTrue(2.5 == testMiner.getMod());
    }

    /**
     * test getStrength
     */
    @Test
    public void testMinerGet() {
        testMiner.setStrength(5);
        testMiner.setMod(1.5);
        assertEquals((int) (5 * 1.5), testMiner.getStrength());
    }

    /**
     * test mod get and set
     */
    @Test
    public void testGatherMod() {
        assertTrue(1.5 == testGather.getMod());
        testGather.setMod(2.0);
        assertTrue(2.0 == testGather.getMod());
        testGather.addMod(.5);
        assertTrue(2.5 == testGather.getMod());
    }

    /**
     * test get methods
     */
    @Test
    public void testGatherGet() {
        testGather.setStrength(4);
        testGather.setSpeed(3);
        testGather.setMod(1.5);
        assertEquals(2, testGather.getStrength());
        assertEquals((int) (3 * 1.5), testGather.getSpeed());

    }

    //test PeonGuard

    /**
     * test PeonGather taskTiredness method
     */
    @Test
    public void testGatherTaskTiredness() {
        testGather.setTiredness(150);
        testGather.setAnnoyance(150);
        testGather.taskTiredness(10, 10);
        assertEquals(190, testGather.getTiredness());
        assertEquals(160, testGather.getAnnoyance());
    }

    /**
     * test initial state
     */
    public void testGuardInit() {
        assertEquals(150, testGuard.getHealth());
    }

    /**
     * test mod get and set
     */
    @Test
    public void testGuardMod() {
        assertTrue(1.5 == testGuard.getMod());
        testGuard.setMod(2.0);
        assertTrue(2.0 == testGuard.getMod());
        testGuard.addMod(.5);
        assertTrue(2.5 == testGuard.getMod());
    }

    /**
     * test getStrength
     */
    @Test
    public void testGuardGet() {
        testGuard.setStrength(5);
        testGuard.setMod(1.5);
        assertEquals(8, testGuard.getAttack());
        testGuard.setHealth(160);
        assertEquals(150, testGuard.getHealth());
    }

    /**
     * test experience
     */
    @Test
    public void testPeonExperience() {
        testPeon.setExperience(10);
        assertEquals(10, testPeon.getExperience());
        testPeon.addExperience(50);
        assertEquals(60, testPeon.getExperience());
        testPeon.setLevel(3);
        assertEquals(3, testPeon.getLevel());
    }
    
    @Test
    public void miscPeonTests() {
    	//some extra tests :)
        testPeon.setName("poor test peon");
        assertEquals("poor test peon", testPeon.getName());
        testPeon.setLuck(20);
        assertEquals(10, testPeon.getLuck());
        testPeon.setLuck(-20);
        assertEquals(1, testPeon.getLuck()); 
    }
}
