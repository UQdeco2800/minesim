package minesim;

import static org.junit.Assert.*;
import minesim.entities.Peon;
import minesim.entities.items.Item;
import minesim.entities.items.Tool;

import org.junit.Test;

public class ItemTests {
    World w;

    private void setupNewWorld() {
        w = new World(100, 100);
    }
    
	@Test
	public void testItemConstructor() {
		Item testItem = new Item (0,0,0,0,"testItem", 99);
		assertTrue(testItem.getDurability() > 0);
		assertTrue(testItem.getDurability() <= 100);
		assertEquals(testItem.getHealth(), 100);
		assertEquals(testItem.getStackLimit(), 99);
		assertEquals(testItem.getID(), testItem.getName().hashCode());
	}
	
	@Test
	public void testStackLimitMethods() {
		Item testItem = new Item (0,0,0,0,"testItem", 99);
		assertEquals(testItem.getStackLimit(), 99);
		testItem.setStackLimit(80);
		assertEquals(testItem.getStackLimit(), 80);
		testItem.setStackLimit(100);
		assertEquals(testItem.getStackLimit(), 99);
		testItem.setStackLimit(-10);
		assertEquals(testItem.getStackLimit(), 0);
	}
	
	@Test
	public void testOtherMethods() {
		Item testItem = new Item (0,0,0,0,"testItem", 99);
		assertEquals(testItem.toString(), "testItem");
		assertEquals(testItem.getItem(), testItem);
	}
	
	@Test
	public void testDurabilityMethods() {
		Item testItem = new Item(0,0,0,0,"testItem", 99);
		testItem.setDurability(20);
		assertEquals(testItem.getDurability(), 20);
		testItem.setDurability(1020);
		assertEquals(testItem.getDurability(),100);
		testItem.setDurability(-21);
		assertEquals(testItem.getDurability(), 0);
	}
	
	@Test
	public void testHealthMethods() {
		Item testItem = new Tool(0,0,0,0,"test", 99, 0, "all", "equip");
		testItem.setHealth(20);
		assertEquals(testItem.getHealth(), 20);
		testItem.subtractHealth(10);
		assertEquals(testItem.getHealth(),10);
		testItem.setHealth(-1);
		assertEquals(testItem.getHealth(), 0);
		testItem.setHealth(10001);
		assertEquals(testItem.getHealth(), 100);
		testItem.subtractHealth(1000);
		assertEquals(testItem.getHealth(), 0);
	}
	
	@Test
	public void testIsDeadMethod() {
		Item testItem = new Item (0,0,0,0,"testItem", 99);
		assertFalse(testItem.isDead());
		testItem.setHealth(0);
		assertTrue(testItem.isDead());
	}
	
	@Test
	public void testGetItemType() {
		Item testItem = new Item (0,0,0,0,"testItem", 99);
		assertEquals(testItem.getType(), "none");
	}
	@Test
    public void testDeleteItem() {
        setupNewWorld();

        int initial = w.getWorldentities().size();
        Item testItem = new Item (0,0,0,0,"testItem", 99);

        w.addEntityToWorld(testItem);
        assertEquals(initial + 1, w.getWorldentities().size());

        w.removeEntityFromWorld(testItem);
        w.onTick(0);
        w.onTick(0);
        w.onTick(0);
        assertEquals(initial, w.getWorldentities().size());
    }
	
	@Test
	public void testGetPeonClass() {
	    Item testItem = new Item (0,0,0,0,"testItem", 99);
	    assertEquals("all", testItem.getPeonClass());
	}

}
