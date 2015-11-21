package minesim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import minesim.entities.items.Apparel;

import org.junit.Test;

public class ItemApparelTests {
	
	@Test
	public void testApparelConstructor() {
	    Apparel apparelItem = new Apparel(1,1,1,1,"hat", 99, 0, "none", "none");
	    assertTrue(apparelItem.getHealth() == 100);	
	    assertTrue(apparelItem.getType() == "none");
	    assertTrue(apparelItem.getPeonClass() == "none");
	}
}
