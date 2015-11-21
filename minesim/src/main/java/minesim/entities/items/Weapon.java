package minesim.entities.items;

import java.io.File;
import java.util.Optional;

import minesim.entities.Upgradable;
import minesim.entities.items.properties.HasSpeed;
import minesim.entities.items.properties.IsUsable;

/**
 * Weapon class represents an item of the type weapon in the game (e.g. a
 * shield). Weapons have a durability and health and are also usable by peons
 * and are upgradable.
 * 
 * @author alana_clover
 *
 */
public class Weapon extends Item implements IsUsable, Upgradable, HasSpeed {

	// Tracks if the item is currently equipped by the peon
	private boolean itemInUseStatus = false;

	// THe speed stat of a weapon item (aka how fast it can destroy things
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
	 * Creates a new weapon item using the item position in the game, the item
	 * name and the item stack limit. New weapon items should have a health of
	 * 100. New weapon items are randomly assigned a valid durability.
	 */
	public Weapon(int xpos, int ypos, int height, int width, String name,
			int stackLimit, int level, String peonClass, String type) {
		super(xpos, ypos, height, width, name, stackLimit);

		this.durability = 20;
		// assign the new weapon item a maximum health (100)
		this.health = 100;
		this.speed = 10;
		this.currentLevel = level;
		this.peonClass = peonClass;
		this.type = type;
		File imageFile = new File("resources/WeaponImages/" + name + ".png");
		if (imageFile.exists()) {
			setImageurl("WeaponImages/" + name + ".png");
		} else {
			setImageurl("TransportationImages/ladder.png");
		}
	}

	/**
	 * A function which determines whether or not the item is equipped
	 *
	 * @return true if the item is equipped, false otherwise
	 */
	public boolean getUsedStatus() {
		return this.itemInUseStatus;
	}

	/**
	 * Sets the itemInUse flag to true.
	 */
	public void setItemToBeUsed() {
		this.itemInUseStatus = true;
	}

	/**
	 * Removes the item being used and sets the itemInUse flag to false.
	 */
	public void removeItemBeingUsed() {
		this.itemInUseStatus = false;
	}

	/**
	 * Set the durability item stat to the specified durability. Items that are
	 * weapons should have 1.5 times the level of durability than normal.
	 * However, weapons should not have a durability greater than the limit
	 * (100) or less than 20 (to ensure weapons don't "die" in an unreasonable
	 * timeframe)
	 */
	@Override
	public void setDurability(int durability) {
		this.durability = (int) durability;

		if (this.getDurability() > 100) {
			this.durability = 100;
		} else if (this.getDurability() < 20) {
			this.durability = 20;
		}
	}

	/**
	 * Upgrading a tool item involves re-setting health to 100, and incrementing
	 * the item speed and item durability by 5.
	 */
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
		this.setDurability(getDurability() + (getCurrentLevel() * 5));
		this.addSpeed(getCurrentLevel() * 3);
		// need to change animiation
	}

	/**
	 * Getter method for speed stat of an item
	 */
	@Override
	public int getSpeed() {
		return speed;
	}

	/**
	 * Sets speed stat to specified amount. Ensure speed does not go below 10 to
	 * ensure weapons do not take an infeasible amount of time to do things
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
	 * Adds the specified value to the items speed
	 */
	@Override
	public void addSpeed(int speed) {

		this.setSpeed(this.getSpeed() + speed);
	}

	/**
	 * Returns the current level of this instance of the item
	 */
	@Override
	public int getCurrentLevel() {
		return this.currentLevel;
	}

	/**
	 * Returns the class of peons that can use this item type
	 */
	@Override
	public String getPeonClass() {
		return this.peonClass;
	}

	/**
	 * Returns the type of this item (i.e. weapon)
	 */
	@Override
	public String getType() {
		return this.type;
	}
}