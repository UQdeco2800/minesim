package minesim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import minesim.entities.items.Apparel;

public class ApparelTests {

	@Test
	public void testApparelConstructor() {
			Apparel apparelItem = new Apparel(1,1,1,1,"hat", 99, 0, "all", "head");
			assertTrue(apparelItem.getDurability() > 0);
			assertTrue(apparelItem.getDurability() <= 100);
			assertEquals(apparelItem.getHealth(), 100);
	}
	
	@Test
	public void testImageFileExists(){
	    Apparel apparelItem2 = new Apparel(1,1,1,1,"test", 99, 0, "all", "head");
	    assertEquals(Optional.of("ApparelImages/test.png"), apparelItem2.image);
	}
}
