package minesim;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

import minesim.entities.Inventory;
import minesim.entities.Inventory.InventoryItem;
import minesim.entities.items.Apparel;
import minesim.entities.items.Item;
//import org.apache.logging.log4j.core.Logger;


import minesim.entities.items.Tool;
import minesim.entities.items.Weapon;

import org.junit.Test;


public class ItemWeaponTests {
	
	
	@Test
	public void testWeaponConstructor() {
		Weapon testWeapon = new Weapon(1,1,1,1,"tool", 99, 0, "all", "equip");
		assertEquals(testWeapon.getDurability(), 20);
		assertEquals(testWeapon.getHealth(), 100);
		assertEquals(testWeapon.getSpeed(), 10);
		assertEquals(testWeapon.getCurrentLevel(), 0);
	}
	
	@Test
	public void testDurabilityMethods() {
		Weapon weapon = new Weapon(0,0,0,0,"test", 99, 0, "all", "equip");
		weapon.setDurability(20);
		assertEquals(weapon.getDurability(),20);
		weapon.setDurability(1020);
		assertEquals(weapon.getDurability(),100);
		weapon.setDurability(1);
		assertEquals(weapon.getDurability(), 20);
	}
	
	@Test
	public void testSpeedMethods() {
		Weapon weapon = new Weapon(0,0,0,0,"test", 99, 0, "all", "equip");
		weapon.setSpeed(20);
		assertEquals(weapon.getSpeed(), 20);
		weapon.addSpeed(10);
		assertEquals(weapon.getSpeed(), 30);
		weapon.setSpeed(1);
		assertEquals(weapon.getSpeed(), 10);
		weapon.setSpeed(1001);
		assertEquals(weapon.getSpeed(), 100);
		weapon.addSpeed(1000);
		assertEquals(weapon.getSpeed(), 100);
	}
	
	@Test
	public void testEquipMethods() {
		Weapon weapon = new Weapon(0,0,0,0,"test", 99, 0, "all", "equip");
		weapon.setItemToBeUsed();
		assertEquals(weapon.getUsedStatus(), true);
		weapon.removeItemBeingUsed();
		assertEquals(weapon.getUsedStatus(), false);
	}
	
	@Test
	public void testWeaponUpgrade() {
		Weapon weapon = new Weapon(0,0,0,0,"test", 99, 0, "all", "equip");
		weapon.upgradeItem();
		assertEquals(weapon.getHealth(), 100);
		assertEquals(weapon.getCurrentLevel(), 1);
		assertEquals(weapon.getName(), "test1");
		assertEquals(weapon.getDurability(), 25);
		assertEquals(weapon.getSpeed(), 13);
		weapon.upgradeItem();
		assertEquals(weapon.getCurrentLevel(), 2);
		assertEquals(weapon.getName(), "test2");
		assertEquals(weapon.getDurability(), 35);
		assertEquals(weapon.getSpeed(), 19);
		weapon.upgradeItem();
		assertEquals(weapon.getCurrentLevel(), 3);
		assertEquals(weapon.getName(), "test3");
		assertEquals(weapon.getDurability(), 50);
		assertEquals(weapon.getSpeed(), 28);
	}
	
	@Test
	public void testImageFileExists(){
	    Weapon weapon1 = new Weapon(0,0,0,0,"test", 99, 0, "all", "equip");
	    assertEquals(Optional.of("WeaponImages/test.png"), weapon1.image);
	}
	
	@Test
	public void testGetType() {
	    Weapon weapon1 = new Weapon(0,0,0,0,"test", 99, 0, "all", "equip");
	    assertEquals("equip", weapon1.getType());
	}
}