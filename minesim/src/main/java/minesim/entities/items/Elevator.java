package minesim.entities.items;

/**
 * The elevator item type class represents a type of transportation item.
 * 
 * @author alana_clover
 *
 */
public class Elevator extends Transportation {

	public Elevator(int xpos, int ypos, int height, int width, String name,
			int stackLimit) {

		super(xpos, ypos, height, width, name, stackLimit);
		super.setName("Elevator");
		super.setSpeed(4);
		super.setImageurl("TransportationImages/elevator.png");
		setEntityGravity(false);
	}
}
