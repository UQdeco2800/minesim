package minesim.entities.items;

import minesim.World;
import minesim.entities.HasName;
import minesim.entities.WorldEntity;
import minesim.entities.items.properties.HasDurability;
import minesim.entities.items.properties.HasHealth;

/**
 * Item class represents items in the game. The item class extends WorldEntity.
 * Different types of items (e.g. tools, weapons, mined entities, food, clothing
 * etc.) will extend the item class. The interfaces that the item class
 * implements represent the properties or stats that all items will have in the
 * game
 * 
 * @author alana_clover
 *
 */
public class Item extends WorldEntity implements HasHealth, HasDurability,
		HasName {

	// Item Information
	// The name of an item
	public String itemName;
	// The limit that an item can be stacked to both in-world and in containers
	public int itemStackLimit;
	// Item durability (ability to withstand damage)
	public int durability;
	// item health (remaining life)
	protected int health;
	/*
	 * The integer representation of an item, calculated by taking the hashcode
	 * of the name
	 */
	public int id;

	/**
	 * Constructs an item that extends world entity from the parameters being
	 * passed in. Items must have a "position" in the game, a name and a stack
	 * limit.
	 *
	 * @param xpos
	 *            -Item's x position in the world
	 * @param ypos
	 *            -Item's y position in the world
	 * @param height
	 *            -Item's height
	 * @param width
	 *            -Item's width
	 * @param name
	 *            -Item's name
	 * @param stackLimit
	 *            -Item's stack limit in an inventory
	 */
	public Item(int xpos, int ypos, int height, int width, String name,
			int stackLimit) {
		super(xpos, ypos, height, width);
		this.itemName = name;
		this.itemStackLimit = stackLimit;
		setDurability(((int) (((Math.random() % .5) + .1) * 100)));
		this.health = 100;
		this.id = name.hashCode();
		addCollisionIgnoredClass(Item.class);
	}

	/**
	 * Returns the int value representing an item's durability. By default all
	 * items have durability, however with the HasDurability interface this
	 * method will soon likely become obsolete.
	 *
	 * @return returns an int val representing the remaining uses on an item
	 *         before it is broken/destroyed
	 **/
	@Override
	public int getDurability() {
		return this.durability;
	}

	@Override
	/** Sets the durability of the item to the supplied value, representing how
	 * fast the items health will deplete.
	 **/
	public void setDurability(int durability) {
		this.durability = durability;
		if (this.getDurability() > 100) {
			this.durability = 100;
		} else if (this.getDurability() < 0) {
			this.durability = 0;
		}
	}

	/**
	 * Getter method for an item's name/identifier
	 *
	 * @return String form representation of item's name
	 **/
	@Override
	public String getName() {
		return this.itemName;
	}

	/**
	 * Allows name of an item to be changed. Seems strange to return a boolean
	 * string value, but perhaps this is useful elsewhere.
	 *
	 * @param name
	 *            -The set String name to replace the item's current name
	 * @return returns a string representation of boolean value of changing an
	 *         item's name.
	 **/
	@Override
	public void setName(String name) {
		this.itemName = name;
	}

	/**
	 * Getter method for an item's stack limit
	 *
	 * @return int val representing the limit to which this item can be stacked
	 **/
	public int getStackLimit() {
		return this.itemStackLimit;
	}

	/**
	 * Sets stack limit of this item to specified limit
	 *
	 * @param limit
	 *            -the val to which the item's stack limit is being set
	 **/
	public void setStackLimit(int limit) {
		this.itemStackLimit = limit;
		if (itemStackLimit < 0) {
			this.itemStackLimit = 0;
		}
		if (itemStackLimit > 99) {
			this.itemStackLimit = 99;
		}
	}

	/**
	 * For now toString just returns the name of an item, however this is
	 * redundant with the getName function unless protecting the peon's name.
	 * Since getName is also public this function becomes useless for protection
	 * and in future should be changed to reflect an item's various states and
	 * properties
	 *
	 * @return String form of item's name
	 * @author Jamesg
	 **/
	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * Returns the item
	 */
	public Item getItem() {
		return this;
	}

	/**
	 * Returns the item health
	 */
	@Override
	public int getHealth() {
		return this.health;
	}

	/**
	 * Sets the item health to the specified value. Item health should not fall
	 * below 0 or exceed 100.
	 */
	@Override
	public void setHealth(int health) {
		this.health = health;
		if (getHealth() < 0) {
			this.health = 0;
		}
		if (getHealth() > 100) {
			this.health = 100;
		}
	}

	/**
	 * Returns true if the item is "dead" and false if otherwise. An item is
	 * dead if its health is less than or equal to zero
	 */
	@Override
	public Boolean isDead() {
		if (health <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Reduces an items health by the specified value. Item health should not
	 * fall below 0.
	 */
	@Override
	public void subtractHealth(int factor) {
		this.health = this.health - factor;
		if (this.health < 0) {
			this.health = 0;
		}
	}

	/**
	 * Return the type class of the item. The type of general items is simply
	 * "none" as the general item class is not an item type.
	 * 
	 * @return
	 */
	public String getType() {
		return "none";
	}

	/**
	 * Return the type class of peons that can use this item.
	 * 
	 * @return
	 */
	public String getPeonClass() {
		return "all";
	}

	/**
	 * Remove item from the game
	 */
	public void deleteItem() {
		World.getInstance().removeEntityFromWorld(this);
	}

	/**
	 * Getter method for an items id, which is used when items are used in
	 * stores
	 */
	public int getID() {
		return this.id;
	}
}