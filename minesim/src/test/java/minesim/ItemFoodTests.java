package minesim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import minesim.entities.items.Apparel;
import minesim.entities.items.Food;

import org.junit.Test;

public class ItemFoodTests {
	
	@Test
	public void testFoodConstructor() {
	    Food foodItem = new Food(1,1,1,1,"apple", 99);
	    assertTrue(foodItem.getDurability() > 0);
	    assertTrue(foodItem.getDurability() <= 100);	
	}
	
	@Test
	public void testEquipMethods() {
		Food foodItem = new Food(1,1,1,1,"apple", 99);
		foodItem.setItemToBeUsed();
		assertEquals(foodItem.getUsedStatus(), true);
		foodItem.removeItemBeingUsed();
		assertEquals(foodItem.getUsedStatus(), false);
	}
	
	   @Test
	    public void testImageFileExists(){
	       Food foodItem2 = new Food(1,1,1,1,"test", 99);
	        assertEquals(Optional.of("FoodImages/test.png"), foodItem2.image);
	    }
}