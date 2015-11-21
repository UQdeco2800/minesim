package minesim;


import org.junit.Test;

import minesim.entities.Building;
import minesim.entities.BuildingBar;
import minesim.entities.BuildingBlacksmith;
import minesim.entities.BuildingGym;
import minesim.entities.BuildingHospital;
import minesim.entities.BuildingNoodlehaus;
import minesim.entities.BuildingTeleporterIn;
import minesim.entities.BuildingTeleporterOut;
import minesim.entities.BuildingWell;
import minesim.entities.Peon;
import minesim.tasks.EnterBuilding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BuildingTests {

    /* use Gym to test Building as its an abstract class */
    Building gym = new BuildingGym(0, 0);
    /* use test peon to test interactions? */
    Peon testPeon = new Peon(0, 0, 50, 50, "TestPeon");
    /*
     * create one of each building class except Gym
     * 1 - WishingWell, 2 - bar, 3 - Blacksmith 4 - NoodleHaus
     * 5 - TeleporterIn, 6 - TeleporterOut
     */
    BuildingWell ww = new BuildingWell(0, 0);
    BuildingBar bar = new BuildingBar(0, 0);
    BuildingBlacksmith blacksmith = new BuildingBlacksmith(0, 0);
    BuildingNoodlehaus noodlehaus = new BuildingNoodlehaus(0, 0);
    BuildingTeleporterIn tpin = new BuildingTeleporterIn(0, 0);
    BuildingTeleporterOut tpout = new BuildingTeleporterOut(0, 0);
    BuildingHospital hospital = new BuildingHospital(0, 0);
    /**
     * Test that the building class with gym
     */
    @Test
    public void testBuildingInitial() {
        //check the Buildings interface values are as they should be
        assertEquals(100, gym.getHealth());
        assertFalse(gym.isDead());
        assertEquals(0, gym.getBuildingType());
        assertEquals("Strength, speed and Jim", gym.getDesc());
    }

    /**
     * Test the building class can use its interfaces
     */
    @Test
    public void testBuildingSetAdd() {
        //check the Buildings interface methods are as they should be
        gym.setHealth(100);
        assertEquals(100, gym.getHealth());
        gym.subtractHealth(10);
        assertEquals(90, gym.getHealth());
        gym.addHealth(10);
        assertEquals(100, gym.getHealth());
        gym.setName("Tim's gym");
        assertEquals("Tim's gym", gym.getName());
    }

    /**
     * Test that the building gym interacts with the peon properly
     */
    @Test
    public void testPeonInteractionWithGym() {
            /* stats before */
        int health = testPeon.getHealth();
        int strength = testPeon.getStrength();
        int speed = testPeon.getSpeed();
        int tiredness = testPeon.getTiredness();
        int buildingbonus = testPeon.getBuildingBonus();

        EnterBuilding enter = new EnterBuilding(testPeon, gym);
        enter.doTask(); // construction
        enter.doTask(); // calls peon stuff, gym.interact(testPeon);
        assertEquals(90, gym.getHealth());
        
        /* check the buff ticks on the Peon Ok */
        testPeon.onTick();
        //check peon is updated
        assertEquals(health - 20, testPeon.getHealth());
        assertEquals(strength + 2, testPeon.getStrength());
        assertEquals(speed + 2, testPeon.getSpeed());
        assertEquals(tiredness + 500, testPeon.getTiredness());
        
        /* check peon buildingbuff limits are working ok*/
        int array[] = testPeon.getBuildingBonusArray();
        assertEquals(1, array[0]);
        int array2[] = testPeon.getBuildingBonusArrayTimer();
        assertEquals(5999, array2[0]);
        assertEquals(buildingbonus - 1, testPeon.getBuildingBonus());
        
        /* check animation doesnt break here when damaged*/
        gym.setHealth(90);
        gym.onTick();
        gym.setHealth(100);
        gym.onTick();

    }

    @Test
    public void destroyBuilding() {
        gym.setHealth(0);
        gym.onTick();
        assertEquals(Boolean.TRUE, gym.isDead());
    }

    @Test
    public void sellBuilding() {
        Building gym2 = new BuildingGym(0, 0);
        gym2.sellBuilding();
        assertEquals(Boolean.TRUE, gym2.isDead());
    }

    @Test
    public void wellInitialandInteraction() {
        assertEquals(1, ww.getBuildingType());
        assertEquals("Wishing Well", ww.getName());
        
        /* stats before */
        int luck = testPeon.getLuck();
        
        EnterBuilding enter = new EnterBuilding(testPeon, ww);
        enter.doTask(); //once for construction
        enter.doTask(); // calls peon stuff, gym.interact(testPeon);
        assertEquals(90, ww.getHealth());
        
        /* check the buff ticks on the Peon Ok */
        testPeon.onTick();
        //check peon is updated
        assertEquals(luck + 2, testPeon.getLuck());

			/* test healthy and damaged animation working */
        ww.setHealth(90);
        ww.onTick();
        ww.setHealth(100);
        ww.onTick();
        
    }

    /**
     * Test the bar initially, then an interaction, then a change in animation
     */
    @Test
    public void bar() {
        assertEquals(2, bar.getBuildingType());
        assertEquals("Bar", bar.getName());

			/* initial peon stats */
        testPeon.setHealth(100);
        testPeon.setTiredness(800);
        testPeon.setHappiness(0);
        int buildingbonus = testPeon.getBuildingBonus();

        EnterBuilding enter = new EnterBuilding(testPeon, bar);
        enter.doTask(); //construction 
        enter.doTask(); // calls peon stuff, gym.interact(testPeon);

        /* check the buff ticks on the Peon Ok */
        testPeon.onTick();
        assertEquals(70, testPeon.getHealth());
        int array[] = testPeon.getBuildingBonusArray();
        assertEquals(1, array[2]);
        int array2[] = testPeon.getBuildingBonusArrayTimer();
        assertEquals(5999, array2[2]);
        assertEquals(buildingbonus - 1, testPeon.getBuildingBonus());

			/* test healthy and damaged animation working */
        bar.onTick();
        bar.setHealth(100);
        bar.onTick();

        bar.setHealth(0);
        assertTrue(bar.interact(testPeon));

    }

    @Test
    public void blacksmithInitial() {
        assertEquals(3, blacksmith.getBuildingType());
        assertEquals("Blacksmith", blacksmith.getName());
			/* test healthy and damaged animation working */
        blacksmith.setHealth(90);
        blacksmith.onTick();
        blacksmith.setHealth(100);
        blacksmith.onTick();
    }

    @Test
    public void noodleInitial() {
        assertEquals(4, noodlehaus.getBuildingType());
        assertEquals("Noodle House", noodlehaus.getName());

			/* initial peon stats */
        testPeon.setHealth(20);
        testPeon.setHappiness(0);
        int buildingbonus = testPeon.getBuildingBonus();


        EnterBuilding enter = new EnterBuilding(testPeon, noodlehaus);
        enter.doTask(); // construction 
        enter.doTask(); // calls peon stuff, gym.interact(testPeon);

//        assertEquals(100, testPeon.getHealth());
//        assertEquals(10, testPeon.getHappiness());
        int array[] = testPeon.getBuildingBonusArray();
        assertEquals(1, array[4]);
        int array2[] = testPeon.getBuildingBonusArrayTimer();
        assertEquals(6000, array2[4]);
        assertEquals(buildingbonus - 1, testPeon.getBuildingBonus());

			/* test healthy and damaged animation working */
        noodlehaus.setHealth(90);
        noodlehaus.onTick();
        noodlehaus.setHealth(100);
        noodlehaus.onTick();

        // if i enter again i should test when i have buff already
        enter.doTask();
    }

    @Test
    public void teleporterInInitial() {
        assertEquals(5, tpin.getBuildingType());
        assertEquals("Teleporter In", tpin.getName());

			/* initial peon stats */
        testPeon.setHealth(100);
        testPeon.setHappiness(50);
        int buildingbonus = testPeon.getBuildingBonus();

        tpin.setCooldown(10);
        assertFalse(tpin.interact(testPeon));
        tpin.setCooldown(0);

        EnterBuilding enter = new EnterBuilding(testPeon, tpin);
        enter.doTask(); // calls peon stuff, gym.interact(testPeon);
        
        /* check the buff ticks on the Peon Ok */
        testPeon.onTick();
        assertEquals(90, testPeon.getHealth());

        int array[] = testPeon.getBuildingBonusArray();
        assertEquals(1, array[5]);
        int array2[] = testPeon.getBuildingBonusArrayTimer();
        assertEquals(5999, array2[5]);
        assertEquals(buildingbonus - 1, testPeon.getBuildingBonus());

			/* test healthy and damaged animation working */
        tpin.setHealth(100);
        tpin.onTick();
        tpin.setHealth(80);
        tpin.onTick();
        tpin.setHealth(60);
        tpin.onTick();
        tpin.setHealth(40);
        tpin.onTick();
        tpin.setHealth(20);
        tpin.onTick();

        tpin.unsetExit();
        assertFalse(tpin.exitExists());

        tpin.setHealth(0);
        tpin.setCooldown(-10);
        tpin.onTick();
        assertEquals(tpin.getCooldown(), 0);
        assertTrue(tpin.interact(testPeon));


    }

    @Test
    public void teleporterOutInitial() {
        assertEquals(6, tpout.getBuildingType());
        assertEquals("Teleporter Out", tpout.getName());

			/* test healthy and damaged animation working */
        tpout.setHealth(100);
        tpout.onTick();
        tpout.setHealth(80);
        tpout.onTick();
        tpout.setHealth(60);
        tpout.onTick();
        tpout.setHealth(40);
        tpout.onTick();
        tpout.setHealth(20);
        tpout.onTick();


    }
    
    @Test
    public void hospital() {
        assertEquals(7, hospital.getBuildingType());
        assertEquals("Hospital", hospital.getName());

			/* test healthy and damaged animation working */
        tpout.setHealth(100);
        tpout.onTick();
        tpout.setHealth(80);
        tpout.onTick();

    }


}
