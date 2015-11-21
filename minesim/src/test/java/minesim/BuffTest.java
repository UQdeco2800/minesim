package minesim;

import minesim.buffs.*;
import minesim.entities.BuildingBar;
import minesim.entities.Peon;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import common.disease.Disease;

public class BuffTest {
	
	ArrayList<Buff> thebuffs;
	String image;
	/* use test peon to test interactions? */
    Peon testPeon = new Peon(0, 0, 50, 50, "TestPeon");
    
    /* used to test if adding peon effects will affect the building*/
    BuildingBar abar = new BuildingBar(0, 0);
    
    /*
     * Use beer as the big tester of initialstuff
     */
    @Test
    public void checkBuffwithBeer() {
    	Beer abeer = new Beer();
    	assertEquals("Boozed up", abeer.getName());
    	assertEquals("I can't remember how much I have had", abeer.getDesc());
    	assertFalse(testPeon.hasBuff());
    	testPeon.addBuff(abeer);
    	assertTrue(testPeon.hasBuff());
    	thebuffs = testPeon.getBuffs();
    	assertEquals(1, thebuffs.size());
    	abeer.buffTick(testPeon);
    	assertEquals(5999, abeer.getCooldown());
    	testPeon.removeBuffByName(abeer);
    	assertFalse(testPeon.hasBuff());
    	assertFalse(testPeon.removeBuffByName(abeer));
    	assertEquals("BuffImages/beer.png", abeer.getImageUrl());
    	
    	testPeon.addBuff(new Luck());
    	assertTrue(testPeon.removeBuffByType(Luck.class));
    	assertFalse(testPeon.removeBuffByType(Luck.class));
    	
    	/* restore peon */
    	testPeon.setHealth(100);
    }
    
    
    @Test
    public void checkBeerEffects() {
    	Beer beer = new Beer();

    	testPeon.addBuff(beer);
    	testPeon.onTick();
    	
    	/* check that no fails if i add to something silly */
    	abar.addBuff(new Beer());
    	abar.onTick();
    	
    	/*initial effect works? */
    	assertEquals(70, testPeon.getHealth());
    	
    	/*test continuous effects */
    	int initialtiredness = testPeon.getTiredness();
    	testPeon.setAnnoyance(10);
    	testPeon.setHappiness(90);
    	
    	/* occurs every x%500 == 0 */
    	beer.setCooldown(5501);
    	testPeon.onTick();
    	
    	assertEquals(initialtiredness + 15, testPeon.getTiredness());
    	assertEquals(10 - 2, testPeon.getAnnoyance());
    	assertEquals(90 + 2, testPeon.getHappiness());
    	
    	/*test final effects work */
    	beer.setCooldown(1);
    	testPeon.onTick();
    	assertFalse(testPeon.hasBuff());
    	
    	/* restore peon */
    	testPeon.setHealth(100);
    	
    }
    
    @Test
    public void checkGymEffects() {
    	GymStrength gyms = new GymStrength();
    	
    	int initialS = testPeon.getStrength();
    	int initials = testPeon.getSpeed();
    	int initialT = testPeon.getTiredness();
    	int initialHP = testPeon.getHealth();
    	
    	testPeon.addBuff(gyms);
    	testPeon.onTick();
    	
    	/* check initial bonus correct */
    	assertEquals(initialS + 2, testPeon.getStrength());
    	assertEquals(initials + 2, testPeon.getSpeed());
    	assertEquals(initialT + 500, testPeon.getTiredness());
    	assertEquals(initialHP - 20, testPeon.getHealth());
    	
    	/* check continual effect */
    	gyms.setCooldown(5001);
    	initialT = testPeon.getTiredness();
    	initialHP = testPeon.getHealth();
    	testPeon.onTick();
    	
    	assertEquals(initialT - 5, testPeon.getTiredness());
    	assertEquals(initialHP + 1, testPeon.getHealth());
    	
    	/* check final effects */
    	gyms.setCooldown(1);
    	initialS = testPeon.getStrength();
    	initials = testPeon.getSpeed();
    	testPeon.onTick();
    	assertEquals(initialS - 2, testPeon.getStrength());
    	assertEquals(initials - 2, testPeon.getSpeed());
    	
    	/* check that no fails if i add to something silly */
    	abar.addBuff(gyms);
    	abar.onTick();
    	
    }
    
    @Test
    public void checkDiseaseEffects() {
    	
    	Infection i = new Infection();
    	
    	int initialHP = testPeon.getHealth();
    	
    	testPeon.addBuff(i);
    	
    	i.setCooldown(3001);
    	testPeon.onTick();
    	
    	assertEquals(initialHP - 4, testPeon.getHealth());
    	
    	testPeon.removeBuffByName(i);
    	
    }
    
    @Test
    public void checkLuckEffects() {
    	
    	Luck l = new Luck();
    	
    	int initialLuck = testPeon.getLuck();
    	
    	testPeon.addBuff(l);
    	testPeon.onTick();
    	
    	/* check initial bonus correct */
    	assertEquals(initialLuck + 2, testPeon.getLuck());
    	
    	/* check continual effect */
    	l.setCooldown(5001);
    	int initialT = 100;
    	testPeon.setTiredness(100);
    	int initialA = 10;
    	testPeon.setAnnoyance(10);
    	int initialH = 80;
    	testPeon.setHappiness(80);
    	testPeon.onTick();
    	assertEquals(initialT - 5, testPeon.getTiredness());
    	assertEquals(initialA - 1, testPeon.getAnnoyance());
    	assertEquals(initialH + 1, testPeon.getHappiness());
    	
    	
    	/* check final effects */
    	l.setCooldown(1);
    	testPeon.onTick();
    	assertEquals(initialLuck, testPeon.getLuck());
    	
    	/* check that no fails if i add to something silly */
    	l.setCooldown(6000);
    	abar.addBuff(l);
    	abar.onTick();
    	l.setCooldown(1);
    	abar.onTick();
    	/* remove by class false */
    	assertFalse(abar.removeBuffByType(Luck.class));
    }
    
    @Test
    public void checkNoodleEffects() {
    	
    	Noodles n = new Noodles();
    	
    	int initialS = testPeon.getStrength();
    	int initialT = 100;
    	testPeon.setTiredness(100);
    	int initialHP = 80;
    	testPeon.setHealth(80);
    	
    	testPeon.addBuff(n);
    	testPeon.onTick();
    	
    	/* check initial bonus correct */
    	assertEquals(initialS + 1, testPeon.getStrength());
    	assertEquals(initialT + 300, testPeon.getTiredness());
    	assertEquals(initialHP + 10, testPeon.getHealth());
    	
    	/* check continual effect */
    	n.setCooldown(5001);
    	initialT = testPeon.getTiredness();
    	initialHP = testPeon.getHealth();
    	testPeon.onTick();
    	assertEquals(initialT - 5, testPeon.getTiredness());
    	assertEquals(initialHP + 1, testPeon.getHealth());
    	
    	
    	/* check final effects */
    	n.setCooldown(1);
    	testPeon.onTick();
    	assertEquals(initialS, testPeon.getStrength());
    	
    	/* check that no fails if i add to something silly */
    	n.setCooldown(6000);
    	abar.addBuff(n);
    	abar.onTick();
    }
    
    
    @Test
    public void checkTeleporterSicknessEffects() {
    	
    	TeleporterSickness ts = new TeleporterSickness();
    	

    	int initialHP = 80;
    	testPeon.setHealth(80);
    	
    	testPeon.addBuff(ts);
    	testPeon.onTick();
    	
    	/* check initial bonus correct */
    	assertEquals(initialHP - 10, testPeon.getHealth());
    	
    	/* check continual effect */
    	ts.setCooldown(3001);
    	int initialT = 100;
    	testPeon.setTiredness(100);
    	initialHP = testPeon.getHealth();
    	int initialH = 80;
    	testPeon.setHappiness(80);
    	testPeon.onTick();
    	assertEquals(initialT + 30, testPeon.getTiredness());
    	assertEquals(initialHP - 2, testPeon.getHealth());
    	assertEquals(initialH - 1, testPeon.getHappiness());
    	
    	
    	/* check final effects */
    	ts.setCooldown(1);
    	testPeon.onTick();
    	
    	/* check that no fails if i add to something silly */
    	ts.setCooldown(6000);
    	abar.addBuff(ts);
    	abar.onTick();
    	
    	
    }
    
}
