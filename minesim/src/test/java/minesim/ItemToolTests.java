package minesim;

import static org.junit.Assert.*;

import java.util.Optional;

import minesim.entities.items.Apparel;
import minesim.entities.items.Tool;

import org.junit.Test;


public class ItemToolTests {
	
	
	@Test
	public void testToolConstructor() {
		Tool testTool = new Tool(1,1,1,1,"tool", 99, 0, "all", "equip");
		assertEquals(testTool.getDurability(), 10);
		assertEquals(testTool.getHealth(), 100);
		assertEquals(testTool.getSpeed(), 10);
		assertEquals(testTool.getCurrentLevel(), 0);
	}
	
	@Test
	public void testDurabilityMethods() {
		Tool tool = new Tool(0,0,0,0,"test", 99, 0, "all", "equip");
		tool.setDurability(20);
		assertEquals(tool.getDurability(),20);
		tool.setDurability(1020);
		assertEquals(tool.getDurability(),100);
		tool.setDurability(1);
		assertEquals(tool.getDurability(), 5);
	}
	
	@Test
	public void testSpeedMethods() {
		Tool tool = new Tool(0,0,0,0,"test", 99, 0, "all", "equip");
		tool.setSpeed(20);
		assertEquals(tool.getSpeed(), 20);
		tool.addSpeed(10);
		assertEquals(tool.getSpeed(), 30);
		tool.setSpeed(1);
		assertEquals(tool.getSpeed(), 10);
		tool.setSpeed(1001);
		assertEquals(tool.getSpeed(), 100);
		tool.addSpeed(1000);
		assertEquals(tool.getSpeed(), 100);
	}
	
	@Test
	public void testEquipMethods() {
		Tool tool = new Tool(0,0,0,0,"test", 99, 0, "all", "equip");
		tool.setItemToBeUsed();
		assertEquals(tool.getUsedStatus(), true);
		tool.removeItemBeingUsed();
		assertEquals(tool.getUsedStatus(), false);
	}
	
	@Test
	public void testToolUpgrade() {
		Tool tool = new Tool(0,0,0,0,"test", 99, 0, "all", "equip");
		tool.upgradeItem();
		assertEquals(tool.getHealth(), 100);
		assertEquals(tool.getCurrentLevel(), 1);
		assertEquals(tool.getName(), "test1");
		assertEquals(tool.getDurability(), 13);
		assertEquals(tool.getSpeed(), 13);
		tool.upgradeItem();
		assertEquals(tool.getCurrentLevel(), 2);
		assertEquals(tool.getName(), "test2");
		assertEquals(tool.getDurability(), 19);
		assertEquals(tool.getSpeed(), 19);
		tool.upgradeItem();
		assertEquals(tool.getCurrentLevel(), 3);
		assertEquals(tool.getName(), "test3");
		assertEquals(tool.getDurability(), 28);
		assertEquals(tool.getSpeed(), 28);
	}
	
	@Test
	public void testImageFileExists(){
	    Tool tool1 = new Tool(0,0,0,0,"test", 99, 0, "all", "equip");
	    assertEquals(Optional.of("ToolImages/test.png"), tool1.image);
	}
}
