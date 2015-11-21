package minesim.entities.items;

import java.io.File;
import java.util.Optional;

import minesim.entities.items.properties.IsUsable;

/**
 * Food items are usable (as they can be eaten by peon's). Food items also
 * implement the standard stats for general items (HasHealth and HasName). A
 * food items health decreases as it is used.
 *
 * @author alana_clover
 **/
public class Food extends Item implements IsUsable {
	// itemInUseStatus is false until food is used/consumed
	private boolean itemInUseStatus = false;

	/**
	 * Constructor class for a food item, which can be created in-world.
	 *
	 * @param xpos
	 *            -the food item's x position in-world when created
	 * @param ypos
	 *            -the food item's y position in-world when created
	 * @param height
	 *            -the food's height when spawned in the world
	 * @param width
	 *            -the food's width when spawned in the world
	 * @param name
	 *            -the food's name given to all instances of it
	 * @stackLimit -the food's stack limit
	 **/
	public Food(int xpos, int ypos, int height, int width, String name,
			int stackLimit) {
		super(xpos, ypos, height, width, name, stackLimit);
		this.durability = ((int) (((Math.random() % .5) + .1) * 100));
		/*
		 * Food items should have a durability of 0.5 times normal so they can
		 * be consumed relatively fast
		 */
		this.durability = (int) ((int) this.durability * 0.5);
		File imageFile = new File("resources/FoodImages/" + name + ".png");
		if (imageFile.exists()) {
			setImageurl("FoodImages/" + name + ".png");
		} else {
			setImageurl("TransportationImages/ladder.png");
		}
	}

	/**
	 * Returns true if food is currently being used a.k.a. "consumed"
	 *
	 * @return true if being used, false otherwise
	 **/
	@Override
	public boolean getUsedStatus() {
		return this.itemInUseStatus;
	}

	/**
	 * Method will allow the food to be able to be consumed
	 **/
	@Override
	public void setItemToBeUsed() {
		this.itemInUseStatus = true;

	}

	/**
	 * Method will set a food items "in use" status to false
	 **/
	@Override
	public void removeItemBeingUsed() {
		this.itemInUseStatus = false;

	}
}