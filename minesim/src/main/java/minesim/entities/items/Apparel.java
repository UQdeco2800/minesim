package minesim.entities.items;

import java.io.File;
import java.util.Optional;

import minesim.entities.Peon;

/**
 * Apparel class represents an item of the type apparel (a.k.a clothing item).
 * For example: helmet, boots and jumper are all different types of apparel
 * items. Apparel items implement the properties of general items (name and
 * health). For example, if one apparel item is worn the health of the item
 * would decrease as the item is continued to be worn.
 * 
 * @author alana_clover
 *
 */
public class Apparel extends Item {
	// Durability value of an apparel, but takes damage when hit, instead of on
	// 'use', since clothes cannot be 'used' and have their own inventory slot.

	// The level required to use the item
	private int currentLevel;

	// The peon class allowed to use the item, (all for any peon being able to
	// use it)
	private String peonClass;

	// The type of the item, (this is used for equipping the item, e.g helmet,
	// equip)
	private String type;

	/**
	 * Creates a new apparel item with the position in the game, the item name
	 * and the stack limit. Apparel items can be stacked in the same way as
	 * other items.
	 *
	 * @param xpos
	 *            -The x-position of the clothing when placed in-world
	 * @param ypos
	 *            -The y-position of the clothing when placed in-world
	 * @param height
	 *            -The clothing item's height when placed in-wold
	 * @param width
	 *            -The clothing item's width when placed in-wold
	 * @param name
	 *            -The name of the piece of clothing
	 * @param stackLimit
	 *            -The limit of stackable iterations of the clothing item
	 */
	public Apparel(int xpos, int ypos, int height, int width, String name,
			int stackLimit, int level, String peonClass, String type) {
		super(xpos, ypos, height, width, name, stackLimit);
		this.currentLevel = level;
		this.peonClass = peonClass;
		this.type = type;

		File imageFile = new File("resources/ApparelImages/" + name + ".png");
		if (imageFile.exists()) {
			setImageurl("ApparelImages/" + name + ".png");
		} else {
			setImageurl("TransportationImages/ladder.png");
		}
		addCollisionIgnoredClass(Peon.class);
	}

	@Override
	public String getPeonClass() {
		return this.peonClass;
	}

	@Override
	public String getType() {
		return this.type;
	}
}