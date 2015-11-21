package minesim;

import java.util.ArrayList;

import org.junit.Test;

import minesim.World;
import minesim.entities.Inventory;
import minesim.entities.Peon;
import minesim.entities.Inventory.InventoryItem;
import minesim.entities.items.Apparel;
import minesim.entities.items.Item;
import minesim.entities.items.Tool;
import minesim.tasks.PickUpItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PickUpItemTests {
	
	World w = new World(100, 100);


    /* use test peon to test interactions? */
    Peon testPeon = new Peon(0, 0, 50, 50, "testPeon");
    
    Item testItem = new Item(0, 0, 20, 20, "testItem", 1);
    Item testItem2 = new Item(0, 0, 20, 20, "testItem2", 1);

    /**
     * Test picking up an item
     */
    @Test
    public void testPickingUpItem() {
        PickUpItem pick = new PickUpItem(testPeon, testItem);
        pick.doTask();
        assertTrue(testPeon.getInventory().doesInventoryContain(testItem));
        PickUpItem pick2 = new PickUpItem(testPeon, testItem2);
        pick2.doTask();
        assertTrue(testPeon.getInventory().doesInventoryContain(testItem2));
    }
    
    /**
     * Test dropping an item
     */
    @Test
    public void testDroppingItem() {
    	testPeon.getInventory().addItem(testItem);
    	testPeon.getInventory().addItem(testItem2);
        assertTrue(testPeon.getInventory().doesInventoryContain(testItem));
        assertTrue(testPeon.getInventory().doesInventoryContain(testItem2));
        testPeon.dropItem(testItem, true);
        assertFalse(testPeon.getInventory().doesInventoryContain(testItem));
        testPeon.dropItem(testItem2, false);
        assertEquals(1, testPeon.getInventory().getInventorySize());
        testPeon.dropItem(testItem2, true);
        assertEquals(0, testPeon.getInventory().getInventorySize());
    }
}