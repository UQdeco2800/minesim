package minesim;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

import minesim.entities.items.Apparel;
import minesim.entities.items.MinedEntity;


public class ItemMinedEntityTests {

	@Test
	public void testMinedEntityConstructor() {
		MinedEntity testMinedEntity = new MinedEntity(1,1,1,1,"test", 99);
		assertTrue(testMinedEntity.getDurability() > 0);
		assertTrue(testMinedEntity.getDurability() <= 100);
		assertEquals(testMinedEntity.getHealth(), 100);
	}
	
	@Test
	public void testDurabilityMethod() {
		MinedEntity testMinedEntity = new MinedEntity(1,1,1,1,"test", 99);
		testMinedEntity.setDurability(30);
		assertEquals(testMinedEntity.getDurability(), 30);
		testMinedEntity.setDurability(-10);
		assertEquals(testMinedEntity.getDurability(), 10);
		testMinedEntity.setDurability(3030);
		assertEquals(testMinedEntity.getDurability(), 100);
	}

	@Test
	public void testImageFileExists(){
	    MinedEntity testMinedEntity1 = new MinedEntity(1,1,1,1,"test", 99);
	    assertEquals(Optional.of("MinedEntityImages/test.png"), testMinedEntity1.image);
	}
}