package minesim.entities.items;

import java.io.File;
import java.util.Optional;

/**
 * MinedEntity class represents mined items in the game (e.g. ore such as gold,
 * rock, copper). MinedEntity items have a durability and health. The durability
 * of a mined entity dictates how quickly a mined entity can be destroyed/mined.
 * The health of a mined entity represents how much of it has already been
 * "mined".
 *
 * @author alana_clover
 **/
public class MinedEntity extends Item {

	/**
	 * Construct a mined entity item using the item position in the game, the
	 * item name and the item stack limit. New items should have a health of 100
	 * (i.e. full health). New items are assigned a random durability.
	 *
	 * @param xpos
	 *            -The entity's starting x position
	 * @param ypos
	 *            -The entity's starting y position
	 * @param height
	 *            -The entity's height
	 * @param width
	 *            -The entity's width
	 * @param name
	 *            - The entity's name
	 * @param stackLimit
	 *            - The limit for how many of this entity can be stacked in an
	 *            inventory
	 **/
	public MinedEntity(int xpos, int ypos, int height, int width, String name,
			int stackLimit) {
		super(xpos, ypos, height, width, name, stackLimit);
		// assign the mined entity item a random durability (between 0 and 100)
		super.setDurability((int) (((Math.random() % .5) + .1) * 100));
		// assign the new mined entity a full health (100)
		super.setHealth(100);

		File imageFile = new File("resources/MinedEntityImages/" + name
				+ ".png");
		if (imageFile.exists()) {
			setImageurl("MinedEntityImages/" + name + ".png");
		} else {
			setImageurl("TransportationImages/ladder.png");
		}
	}

	/**
	 * Set durability to specified durability number. The durability value of a
	 * mined entity should be restricted to be within 10 and 90 to ensure items
	 * are not miend to quickly or too slowly
	 *
	 * @param -durability the integer value that this.durability is set to
	 **/
	@Override
	public void setDurability(int durability) {
		super.durability = durability;
		if (super.getDurability() < 10) {
			super.durability = 10;
		}
		if (super.getDurability() > 100) {
			super.durability = 100;
		}
	}
}