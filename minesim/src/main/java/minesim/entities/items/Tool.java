package minesim.entities.items;

import java.io.File;
import java.util.Optional;

import minesim.entities.Upgradable;
import minesim.entities.items.properties.HasSpeed;
import minesim.entities.items.properties.IsUsable;

/**
 * Tool class represents an item of the type tool in the game (e.g. shovel,
 * axe). Tools have a durability, speed and health, similar to mined entities.
 * Tools are usable by peons and are also upgradable items. When tools are
 * upgraded, their item stats such as speed and durability are improved and
 * their health is reset to 100.
 *
 * @author alana_clover
 */

public class Tool extends Item implements IsUsable, Upgradable, HasSpeed {

	// If the item is currently equipped by the peon
	private boolean itemInUseStatus = false;

	// The speed at which the item can perform tasks
	private int speed;

	// The level required to use the item
	private int currentLevel;

	// The peon class allowed to use the item, (all for any peon being able to
	// use it)
	private String peonClass;

	// The type of the item, (this is used for equipping the item, e.g helmet,
	// equip)
	private String type;

	/**
	 * Creates a new tool item using the item position in the game, the item
	 * name and the item stack limit. New tool items should have a heath of 100.
	 * New items are randomly assigned a speed and durability.
	 *
	 * @param xpos
	 *            -the x position of the tool when placed in-world
	 * @param ypos
	 *            -the y position of the tool when placed in-world
	 * @param height
	 *            -the tool's height when placed in-world
	 * @param width
	 *            -the tool's width when placed in-world
	 * @param name
	 *            -the tool's name when placed in-world
	 * @param stackLimit
	 *            -the tool's stack limit when placed in inventories
	 */
	public Tool(int xpos, int ypos, int height, int width, String name,

	int stackLimit, int level, String peonClass, String type) {
		super(xpos, ypos, height, width, name, stackLimit);
		this.peonClass = peonClass;
		this.type = type;
		super.durability = 10;
		this.speed = 10;
		// Assign the new tool item to have a full health
		super.health = 100;
		this.currentLevel = level;
		File imageFile = new File("resources/ToolImages/" + name + ".png");
		if (imageFile.exists()) {
			setImageurl("ToolImages/" + name + ".png");
		} else {
			setImageurl("TransportationImages/ladder.png");
		}
	}

	/**
	 * A function which determines whether or not the item is equipped
	 *
	 * @return true if the item is equipped, false otherwise
	 **/
	@Override
	public boolean getUsedStatus() {
		return this.itemInUseStatus;
	}

	/**
	 * Sets the itemInUse flag to true.
	 **/
	@Override
	public void setItemToBeUsed() {
		this.itemInUseStatus = true;
	}

	/**
	 * Removes the item being used and sets the itemInUse flag to false.
	 *
	 **/
	@Override
	public void removeItemBeingUsed() {
		this.itemInUseStatus = false;
	}

	/**
	 * Sets durability of tool to specified amount. Tools should not have a
	 * durability greater than the limit (100) or less than 20 (to ensure tools
	 * don't "die" in an unreasonable timeframe)
	 *
	 * @param health
	 *            -the int val by which a tool's durability will be changed
	 **/
	@Override
	public void setDurability(int durability) {
		this.durability = (int) durability;

		if (this.getDurability() > 100) {
			this.durability = 100;
		} else if (this.getDurability() < 5) {
			this.durability = 5;
		}
	}

	/**
	 * Upgrading a tool item involves re-setting health to 100, and incrementing
	 * the item speed and item durability by 5.
	 **/
	@Override
	public void upgradeItem() {
		this.currentLevel = getCurrentLevel() + 1;
		if (getCurrentLevel() == 1) {
			this.setName(getName() + getCurrentLevel());
		} else {
			this.setName(getName().substring(0, getName().length() - 1)
					+ getCurrentLevel());
		}

		this.setHealth(100);
		this.setDurability(getDurability() + (getCurrentLevel() * 3));
		this.addSpeed(getCurrentLevel() * 3);
		// need to change animiation
	}

	/**
	 * Getter method for speed stat of a tool item
	 *
	 * @return int value for tool's speed
	 **/
	@Override
	public int getSpeed() {
		return speed;
	}

	/**
	 * Sets speed stat to specified amount
	 *
	 * @param speed
	 *            -the int val by which speed is to be changed
	 */
	@Override
	public void setSpeed(int speed) {
		this.speed = speed;
		if (speed < 10) {
			this.speed = 10;
		}
		if (speed > 100) {
			this.speed = 100;
		}
	}

	/**
	 * Adds the specified value to the item speed
	 * 
	 * @param speed
	 *            the value to increment item speed by
	 */
	@Override
	public void addSpeed(int speed) {
		this.setSpeed(this.getSpeed() + speed);
	}

	/**
	 * Returns the class of peon's that can use this item
	 */
	@Override
	public String getPeonClass() {
		return this.peonClass;
	}

	/**
	 * Returns the type of this item (i.e. tool)
	 */
	@Override
	public String getType() {
		return this.type;
	}

	/**
	 * Returns the current level of the instance of this item
	 */
	@Override
	public int getCurrentLevel() {
		return this.currentLevel;
	}
}