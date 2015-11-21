package minesim.entities.items;

/**
 * The ladder item type class represents a type of transportation item.
 * 
 * @author alana_clover
 *
 */
public class Ladder extends Transportation {

	public Ladder(int xpos, int ypos, int height, int width, String name,
			int stackLimit) {

		super(xpos, ypos, height, width, name, stackLimit);
		setName("Ladder");
		setSpeed(2);
		setImageurl("TransportationImages/ladder.png");
		setEntityGravity(false);
	}
}