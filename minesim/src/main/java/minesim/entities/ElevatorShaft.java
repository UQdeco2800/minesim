package minesim.entities;

import minesim.entities.items.Transportation;

public class ElevatorShaft extends Transportation {
	
	public ElevatorShaft(int xpos, int ypos, int height, int width, String name, int stackLimit) {
		super(xpos, ypos, height, width, name, stackLimit);
		setName("ElevatorShaft");
        setSpeed(4);
        setImageurl("TransportationImages/elevatorshaft.png");
        setEntityGravity(false);
	}
}
