package minesim;



import static org.junit.Assert.*;

import java.util.ArrayList;

import minesim.entities.Inventory;
import minesim.entities.Inventory.InventoryItem;
import minesim.entities.Peon;
import minesim.entities.items.Apparel;
import minesim.entities.items.Item;
import minesim.entities.items.Tool;
//import org.apache.logging.log4j.core.Logger;





import org.junit.Test;

public class InventoryTests {


	Item hat = new Apparel(0,0,0,0,"hat",0,0,"all","head");
	Item shirt = new Apparel(0,0,0,0,"shirt",0,0,"all","chest");
	Item boots = new Apparel(0,0,0,0,"boots",0,0,"all","feet");
	Item socks = new Apparel(0,0,0,0,"socks",0,0,"all","feet");
	Item shovel = new Tool(0,0,0,0,"shovel",0,0,"all","equip");
	ArrayList<InventoryItem> peonInventory;
	Peon peon = new Peon(0, 0, 32, 32, "Jim Steel");

	@Test
	public void testInventoryConstructor() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		assertEquals(peonTestInventory.getInventorySize(), 0);
	}

	@Test
	public void testAddItemToInventory() {

		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);

		assertEquals(peonTestInventory.toString(), "Inventory is empty");
		Item testItem1 = new Tool(0,0,0,0,"test",1,0,"all","equip");
		peonTestInventory.addItem(testItem1);
		assertTrue(peonTestInventory.doesInventoryContain(testItem1));
		assertEquals(peonTestInventory.getInventory().get(0).getAmount(),1);
		assertEquals(peonTestInventory.getInventorySize(),1);
		assertEquals(peonTestInventory.toString(), "Inventory contains: 1 test");
	}

	@Test
	public void testAddItemToInventoryWithFullStack() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem1 = new Tool(0,0,0,0,"test",2, 0, "all", "equip");
		peonTestInventory.addItem(testItem1);
		assertEquals(peonTestInventory.getInventorySize(),1);
		peonTestInventory.addItem(testItem1);
		assertEquals(peonTestInventory.getInventorySize(),1);
		peonTestInventory.addItem(testItem1);
		assertEquals(peonTestInventory.getInventorySize(),2);
		peonTestInventory.addItem(testItem1);
		assertEquals(peonTestInventory.getInventorySize(),2);
	}

	@Test
	public void testAddMultipleItemsOfOneTypeToInventory() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem3 = new Tool(0,0,0,0,"shovel",4,0,"all","equip");
		peonTestInventory.addItem(testItem3);
		peonTestInventory.addItem(testItem3);
		peonTestInventory.addItem(testItem3);
		peonTestInventory.addItem(testItem3);
		assertTrue(peonTestInventory.doesInventoryContain(testItem3));
		int testItem3Index = peonTestInventory.findIndexOfItemInInventory(testItem3);
		assertEquals(peonTestInventory.getInventory().get(testItem3Index).getAmount(),4);
		assertEquals(peonTestInventory.getInventorySize(),1);
	}

	@Test
	public void testAddMultipleItemsDifferentTypes() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem4 = new Tool(0,0,0,0,"shovel",1,0,"all","equip");
		peonTestInventory.addItem(testItem4);
		Item testItem5 = new Tool(0,0,0,0,"spade",1,0,"all","equip");
		peonTestInventory.addItem(testItem5);
		Item testItem6 = new Tool(0,0,0,0,"trowel",1,0,"all","equip");
		peonTestInventory.addItem(testItem6);
		assertEquals(peonTestInventory.getInventorySize(),3);
		assertTrue(peonTestInventory.doesInventoryContain(testItem4));
		assertTrue(peonTestInventory.doesInventoryContain(testItem5));
		assertTrue(peonTestInventory.doesInventoryContain(testItem6));

	}
	@Test
	public void testRemoveOnlyItemFromInventory() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem7 = new Tool(0,0,0,0,"shovel",0,0,"all","equip");
		peonTestInventory.addItem(testItem7);
		assertTrue(peonTestInventory.doesInventoryContain(testItem7));
		assertEquals(peonTestInventory.getInventorySize(),1);
		peonTestInventory.removeItem(testItem7);
		assertFalse(peonTestInventory.doesInventoryContain(testItem7));
		assertEquals(peonTestInventory.getInventorySize(),0);
	}

	@Test
	public void testRemoveOneOfMultipleItemsFromInventory() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem3 = new Tool(0,0,0,0,"shovel",0,0,"all","equip");
		peonTestInventory.addItem(testItem3);
		peonTestInventory.addItem(testItem3);
		peonTestInventory.addItem(testItem3);
		peonTestInventory.addItem(testItem3);
		int testItem3Index = peonTestInventory.findIndexOfItemInInventory(testItem3);
		assertEquals(peonTestInventory.getInventory().get(testItem3Index).getAmount(),4);
		peonTestInventory.removeItem(testItem3);
		assertEquals(peonTestInventory.getInventory().get(testItem3Index).getAmount(),3);
	}

	@Test
	public void testRemoveNonExistentItem() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem3 = new Tool(0,0,0,0,"shovel",0, 0, "all", "equip");
		peonTestInventory.removeItem(testItem3);
		assertEquals(peonTestInventory.getInventorySize(),0);
	}

	@Test
	public void testAddItemWithFullStack() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem3 = new Tool(0,0,0,0,"shovel",2, 0, "all", "equip");
		int testItem2Index = peonTestInventory.findIndexOfItemInInventory(testItem3);
		assertEquals(testItem2Index,-1);
		assertFalse(peonTestInventory.doesInventoryContainItemWithName("shovel"));
		peonTestInventory.addItem(testItem3);
		peonTestInventory.addItem(testItem3);
		assertTrue(peonTestInventory.doesInventoryContain(testItem3));
		assertTrue(peonTestInventory.doesInventoryContainItemWithName("shovel"));
		int testItem3Index = peonTestInventory.findIndexOfItemInInventory(testItem3);
		int testItem4Index = peonTestInventory.findIndexOfItemInInventoryByName("shovel");

		assertEquals(testItem3Index, testItem4Index);
		assertEquals(peonTestInventory.getInventory().get(testItem3Index).getAmount(),2);
		peonTestInventory.addItem(testItem3);
		assertTrue(peonTestInventory.doesInventoryContain(testItem3));
		int testItem3SecondStackIndex = peonTestInventory.findIndexOfItemInInventory(testItem3);
		assertTrue(testItem3Index != testItem3SecondStackIndex);
		assertEquals(peonTestInventory.getInventory().get(testItem3SecondStackIndex).getAmount(),1);
		assertEquals(peonTestInventory.getInventorySize(),2);

	}

	@Test
	public void testFindNonExistentItem() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem3 = new Tool(0,0,0,0,"shovel",2, 0, "all", "equip");
		Item testItem4 = new Tool(0,0,0,0,"spade",2, 0, "all", "equip");
		assertEquals(peonTestInventory.findIndexOfItemInInventoryByName("nonExistent"), -1);
		assertEquals(peonTestInventory.findIndexOfItemInInventory(testItem3), -1);
		peonTestInventory.addItem(testItem3);
		assertTrue(peonTestInventory.doesInventoryContain(testItem3));
		assertTrue(peonTestInventory.doesInventoryContainItemWithName("shovel"));
		assertFalse(peonTestInventory.doesInventoryContain(testItem4));
		assertEquals(peonTestInventory.findIndexOfItemInInventoryByName("spade"), -1);
		assertFalse(peonTestInventory.doesInventoryContainItemWithName("spade"));
	}

	@Test
	public void testClearInventory() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem4 = new Tool(0,0,0,0,"shovel",0,0,"all","equip");
		peonTestInventory.addItem(testItem4);
		Item testItem5 = new Tool(0,0,0,0,"axe",0,0,"all","equip");
		peonTestInventory.addItem(testItem5);
		Item testItem6 = new Tool(0,0,0,0,"spade",0,0,"all","equip");
		peonTestInventory.addItem(testItem6);
		assertEquals(peonTestInventory.getInventorySize(),3);
		peonTestInventory.clearInventory();
		assertEquals(peonTestInventory.getInventorySize(),0);
	}

	@Test
	public void testAddItemToFullInventory() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem1 = new Tool(0,0,0,0,"shovel",1, 0, "all", "equip");
		peonTestInventory.addItem(testItem1);
		peonTestInventory.addItem(testItem1);
		Item testItem3 = new Tool(0,0,0,0,"shovel",1, 0, "all", "equip");
		peonTestInventory.addItem(testItem3);
		Item testItem4 = new Tool(0,0,0,0,"shovel",1,0, "all", "equip");
		peonTestInventory.addItem(testItem4);
		Item testItem5 = new Tool(0,0,0,0,"shovel",1,0, "all", "equip");
		peonTestInventory.addItem(testItem5);
		Item testItem6 = new Tool(0,0,0,0,"shovel",1,0, "all", "equip");
		peonTestInventory.addItem(testItem6);
		Item testItem7 = new Tool(0,0,0,0,"shovel",1,0, "all", "equip");
		peonTestInventory.addItem(testItem7);
		peonTestInventory.addItem(testItem4);
		Item testItem8 = new Tool(0,0,0,0,"shovel",1,0, "all", "equip");
		peonTestInventory.addItem(testItem8);
		Item testItem9 = new Tool(0,0,0,0,"shovel",1,0, "all", "equip");
		peonTestInventory.addItem(testItem9);
		// inventory is now full so testItem10 should not get added
		Item testItem10 = new Tool(0,0,0,0,"axe",0,0, "all", "equip");
		peonTestInventory.addItem(testItem10);
		assertEquals(peonTestInventory.getInventorySize(),9);
	}

	@Test
	public void testEquipItemCases() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Item testItem4 = new Tool(0,0,0,0,"shovel",0,0, "all", "equip");
		peonTestInventory.addItem(testItem4);
		peonTestInventory.equipItem(testItem4);
		assertTrue(peonTestInventory.isItemEquipped(testItem4));
		Item testItemEquip = new Tool(0,0,0,0,"shovel",0,0,"all","tool");
		peonTestInventory.equipItem(testItemEquip);
		Item testItemEquip1 = new Tool(0,0,0,0,"shovel",0,0,"all","tool");
		assertFalse(peonTestInventory.equipItem(testItemEquip1));
	}

	@Test
	public void testEquipHeadItem() {
		Tool testItem101 = new Tool(0,0,0,0,"tool", 1, 0, "all", "other");
		Tool testItem102 = new Tool(0,0,0,0,"tool1", 1, 0, "all", "other");

		Inventory peonTestInventory2 = new Inventory(2, 1, null, null, null, null, shovel, peonInventory, peon);
		Item testItemEquip2 = new Tool(0,0,0,0,"shovel",0,0,"all","head");
		peonTestInventory2.equipItem(testItemEquip2);
		assertEquals(peonTestInventory2.getHeadSlot(), testItemEquip2);
		peonTestInventory2.equipItem(testItemEquip2);
		assertEquals(peonTestInventory2.getHeadSlot(), testItemEquip2);
		peonTestInventory2.dequipItem(testItemEquip2);
		assertEquals(peonTestInventory2.getHeadSlot(), null);
		assertTrue(peonTestInventory2.doesInventoryContain(testItemEquip2));
		peonTestInventory2.equipItem(testItemEquip2);
		peonTestInventory2.addItem(testItem101);
		peonTestInventory2.addItem(testItem102);
		peonTestInventory2.dequipItem(testItemEquip2);
		assertFalse(peonTestInventory2.doesInventoryContain(testItemEquip2));

	}

	@Test
	public void testEquipBodyItem() {
		Tool testItem101 = new Tool(0,0,0,0,"tool", 1, 0, "all", "other");
		Tool testItem102 = new Tool(0,0,0,0,"tool1", 1, 0, "all", "other");
		Inventory peonTestInventory3 = new Inventory(2, 1, null, null, null, null, shovel, peonInventory, peon);
		Item testItemEquip3 = new Tool(0,0,0,0,"shovel",0,0,"all","body");
		peonTestInventory3.equipItem(testItemEquip3);
		assertEquals(peonTestInventory3.getBodySlot(), testItemEquip3);
		peonTestInventory3.equipItem(testItemEquip3);
		assertEquals(peonTestInventory3.getBodySlot(), testItemEquip3);
		peonTestInventory3.dequipItem(testItemEquip3);
		assertEquals(peonTestInventory3.getBodySlot(), null);
		assertTrue(peonTestInventory3.doesInventoryContain(testItemEquip3));
		peonTestInventory3.equipItem(testItemEquip3);
		peonTestInventory3.addItem(testItem101);
		peonTestInventory3.addItem(testItem102);
		peonTestInventory3.dequipItem(testItemEquip3);
		assertFalse(peonTestInventory3.doesInventoryContain(testItemEquip3));
	}

	@Test
	public void testEquipLegsItem() {

		Tool testItem101 = new Tool(0,0,0,0,"tool", 1, 0, "all", "other");
		Tool testItem102 = new Tool(0,0,0,0,"tool1", 1, 0, "all", "other");

		Inventory peonTestInventory4 = new Inventory(2, 1, null, null, null, null, shovel, peonInventory, peon);
		Item testItemEquip4 = new Tool(0,0,0,0,"shovel",0,0,"all","legs");
		peonTestInventory4.equipItem(testItemEquip4);
		assertEquals(peonTestInventory4.getLegsSlot(), testItemEquip4);
		peonTestInventory4.equipItem(testItemEquip4);
		assertEquals(peonTestInventory4.getLegsSlot(), testItemEquip4);
		peonTestInventory4.dequipItem(testItemEquip4);
		assertEquals(peonTestInventory4.getLegsSlot(), null);
		assertTrue(peonTestInventory4.doesInventoryContain(testItemEquip4));
		peonTestInventory4.equipItem(testItemEquip4);
		peonTestInventory4.addItem(testItem101);
		peonTestInventory4.addItem(testItem102);
		peonTestInventory4.dequipItem(testItemEquip4);
		assertFalse(peonTestInventory4.doesInventoryContain(testItemEquip4));
	}

	@Test
	public void testEquipShoeItem() {

		Tool testItem101 = new Tool(0,0,0,0,"tool", 1, 0, "all", "other");
		Tool testItem102 = new Tool(0,0,0,0,"tool1", 1, 0, "all", "other");

		Inventory peonTestInventory5 = new Inventory(2, 1, null, null, null, null, shovel, peonInventory, peon);
		Item testItemEquip5 = new Tool(0,0,0,0,"shovel",0,0,"all","shoes");
		peonTestInventory5.equipItem(testItemEquip5);
		assertEquals(peonTestInventory5.getShoesSlot(), testItemEquip5);
		peonTestInventory5.equipItem(testItemEquip5);
		assertEquals(peonTestInventory5.getShoesSlot(), testItemEquip5);
		peonTestInventory5.dequipItem(testItemEquip5);
		assertEquals(peonTestInventory5.getShoesSlot(), null);
		assertTrue(peonTestInventory5.doesInventoryContain(testItemEquip5));
		peonTestInventory5.equipItem(testItemEquip5);
		peonTestInventory5.addItem(testItem101);
		peonTestInventory5.addItem(testItem102);
		peonTestInventory5.dequipItem(testItemEquip5);
		assertFalse(peonTestInventory5.doesInventoryContain(testItemEquip5));
	}

	@Test
	public void testEquipEquippedItem() {

		Tool testItem101 = new Tool(0,0,0,0,"tool", 1, 0, "all", "other");
		Tool testItem102 = new Tool(0,0,0,0,"tool1", 1, 0, "all", "other");

		Inventory peonTestInventory6 = new Inventory(2, 1, null, null, null, null, shovel, peonInventory, peon);
		Item testItemEquip6 = new Tool(0,0,0,0,"shovel",0,0,"all","equip");
		peonTestInventory6.equipItem(testItemEquip6);
		assertEquals(peonTestInventory6.getEquipSlot(), testItemEquip6);
		peonTestInventory6.equipItem(testItemEquip6);
		assertEquals(peonTestInventory6.getEquipSlot(), testItemEquip6);
		peonTestInventory6.dequipItem(testItemEquip6);
		assertEquals(peonTestInventory6.getEquipSlot(), null);
		assertTrue(peonTestInventory6.doesInventoryContain(testItemEquip6));
		peonTestInventory6.equipItem(testItemEquip6);
		peonTestInventory6.addItem(testItem101);
		peonTestInventory6.addItem(testItem102);
		peonTestInventory6.dequipItem(testItemEquip6);
		assertFalse(peonTestInventory6.doesInventoryContain(testItemEquip6));
		Item testItemOfTypeNone = new Tool(0,0,0,0,"nothing",0,0,"all", "none");
		assertFalse(peonTestInventory6.equipItem(testItemOfTypeNone));

	}

	@Test
	public void testDiggingToolMethods() {
		Inventory peonTestInventory = new Inventory(9, 99, hat, shirt, socks, boots, shovel, peonInventory, peon);
		Tool testItem4 = new Tool(0,0,0,0,"shovel",0,0, "all", "equip");
		peonTestInventory.addItem(testItem4);
		peonTestInventory.removeDiggingTool("shovel");
		assertFalse(peonTestInventory.doesInventoryContain(testItem4));
		peonTestInventory.addItem(testItem4);
		assertEquals(peonTestInventory.getDiggingTool("shovel"), testItem4);
		assertEquals(peonTestInventory.getDiggingTool("spade"), null);
		peonTestInventory.removeDiggingTool("spade");
		assertEquals(peonTestInventory.getInventorySize(), 1);

	}
}