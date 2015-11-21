package minesim.entities;

import java.io.Serializable;
import java.util.ArrayList;

import minesim.contexts.PeonStatusContextControllerHandler;
import minesim.entities.items.Item;

/**
 * Each peon will have their own inventory where inventory items are stored. An
 * inventory currently has 9 slots for items, however a peon can possess 10
 * items at a time including the one item they are equipped with.
 *
 * @author alana_clover
 **/
@SuppressWarnings("serial")
public class Inventory implements Serializable {
	final int head = 0;
	final int body = 1;
	final int legs = 2;
	final int shoes = 3;
	final int equip = 4;
	// A peon's inventory of inventory items
	ArrayList<InventoryItem> peonInventory;
	// ArrayList<Integer> matchingInstanceIndexList = new ArrayList<Integer>();
	// The maximum number of different inventory items a peon can have in their
	// inventory
	int defaultMaxNumberOfItemSlots = 9;
	// The maximum number of inventory items a peon can have of one type
	int defaultMaxItemStackLimit = 99;
	// The item variable for a peon's head wear
	Item headSlot = null;
	// The item variable for a peon's shirt wear
	Item bodySlot = null;
	// The item variable for a peon's shorts
	Item legsSlot = null;
	// The item variable for a peon's footwear
	Item shoesSlot = null;
	// The current inventory item currently in use by the peon
	Item equipSlot = null;
	// The maximum number of items a peon can use at once
	int defaultMaxInventoryItemsInUse = 1;
	private int amount;
	// The maximum number of items can be stacked by default, but can be changed
	// by the Item itself in special circumstances
	private int stackLimit;

	private Peon peon;

	/**
	 * Constructor method for a peon's Inventory
	 *
	 * @param defaultNumberOfItemSlots
	 *            -the number of inventory slots in a peon's inventory
	 *            (inventory capacity)
	 * @param defaultItemStackLimit
	 *            -the maximum number of inventory items of the same type that
	 *            can be stored in one inventory slot
	 * @param head
	 *            -the slot for apparel items worn on head
	 * @param body
	 *            -the slot for apparel to be equipped on the body
	 * @param legs
	 *            -leg slot for equippable apparel items
	 * @param shoes
	 *            -feet slot for wearable apparel items/shoes
	 * @param currentItemEquipped
	 *            the inventory item currently beind using
	 * @param peonInventory
	 *            a peon's inventory of inventory items
	 **/

	public Inventory(int defaultNumberOfItemSlots, int defaultItemStackLimit,
			Item head, Item body, Item legs, Item shoes,
			Item currentItemEquipped, ArrayList<InventoryItem> peonInventory,
			Peon peon) {

		this.defaultMaxNumberOfItemSlots = defaultNumberOfItemSlots;
		this.defaultMaxItemStackLimit = defaultItemStackLimit;
		this.peonInventory = new ArrayList<InventoryItem>();
		this.bodySlot = body;
		this.headSlot = head;
		this.legsSlot = legs;
		this.shoesSlot = shoes;
		this.equipSlot = currentItemEquipped;
		this.peon = peon;
	}

	/**
	 * Adds an item into a peon's inventory. If the item already exists and the
	 * stack limit has not been exceeded, the item count will be incremented by
	 * 1. If the item does not exist or if the stack limit of a pre-existing
	 * item has been reached, the item will be added into a new "slot" of a
	 * peon's inventory providing there is a free slot.
	 *
	 * @param item
	 *            -the item to be added to the inventory
	 **/
	public void addItem(Item item) {
		InventoryItem newItem = new InventoryItem(item, 1);
		// If the item to be added already exists as an inventory item
		if (findIndexOfItemInInventory(item) >= 0) {
			// store index of existing item
			int itemIndex = findIndexOfItemInInventory(item);
			// If the existing inventory item stack is not full
			if (!peonInventory.get(itemIndex).stackFull()) {
				peonInventory.get(itemIndex).incrementAmount(1);
				try {
					PeonStatusContextControllerHandler.getInstance()
							.showPeonStatus(this.peon);
				} catch (Exception E) {
					// this exception will only happen in tests
				}
				return;
			} else {
				if (inventorySlotsLeft() != 0) {
					peonInventory.add(newItem);
					try {
						PeonStatusContextControllerHandler.getInstance()
								.showPeonStatus(this.peon);
					} catch (Exception E) {
						// this exception will only happen in tests
					}
					return;
				}
			}
			// If item to be added does not already exist of if stack is full,
			// add a to inventory if there is a spare slot
		} else {
			if (inventorySlotsLeft() != 0) {
				peonInventory.add(newItem);
				try {
					PeonStatusContextControllerHandler.getInstance()
							.showPeonStatus(this.peon);
				} catch (Exception E) {
					// this exception will only happen in tests
				}
				return;
			}
		}
		this.peon.dropItem(item, false);
		try {
			PeonStatusContextControllerHandler.getInstance().showPeonStatus(
					this.peon);
		} catch (Exception E) {
			// this exception will only happen in tests
		}
	}

	/**
	 * Getter method for peon inventory size
	 *
	 * @return the current size (number of occupied slots) in a peon's inventory
	 **/
	public int getInventorySize() {
		return peonInventory.size();
	}

	/**
	 * Removes an item from a peon's inventory. If there is only one of the
	 * item, the item will be completely removed. If there is more than one of
	 * this item, the amount of the item will be decremented by 1.
	 *
	 * @param item
	 *            -the item to be removed
	 */
	public void removeItem(Item item) {
		if (findIndexOfItemInInventory(item) >= 0) {
			int itemIndex = findIndexOfItemInInventory(item);
			if (peonInventory.get(itemIndex).getAmount() > 1) {
				peonInventory.get(itemIndex).decrementAmount(1);
			} else {
				peonInventory.remove(itemIndex);
			}
		}
		try {
			PeonStatusContextControllerHandler.getInstance().showPeonStatus(
					this.peon);
		} catch (Exception E) {
			// this exception will only happen in tests
		}
	}

	/**
	 * Finds the index for a given item in a peon inventory
	 * 
	 * @param item
	 *            the item to be found
	 * @return the index of the item or -1 if it does not exist
	 */
	public int findIndexOfItemInInventory(Item item) {
		for (int i = peonInventory.size() - 1; i >= 0; i--) {
			if (peonInventory.get(i).getItem().getName() == item.getName()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Finds the index for a given item name in a peon inventory
	 * 
	 * @param itemName
	 *            the item name to be found
	 * @return the index of the item that corresponds to the item name and -1 if
	 *         it does not exist
	 */
	public int findIndexOfItemInInventoryByName(String itemName) {
		for (int i = peonInventory.size() - 1; i >= 0; i--) {
			if (peonInventory.get(i).getItem().getName() == itemName) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Getter method for peon inventory
	 *
	 * @return an ArrayList of InventoryItems, which is an inventory subclass
	 **/
	public ArrayList<InventoryItem> getInventory() {
		return this.peonInventory;
	}

	/**
	 * Clears/removes all inventory items from a peon inventory
	 */
	public void clearInventory() {
		peonInventory.clear();
	}

	/**
	 * Returns true if a peon inventory contains the item passed and false if it
	 * does not
	 *
	 * @param item
	 *            -the item to be checked for containing
	 * @return true if item is in inventory, false otherwise
	 */
	public boolean doesInventoryContain(Item item) {
		for (int i = 0; i < peonInventory.size(); i++) {
			if (peonInventory.get(i).getItem().getName() == item.getName()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if a peon inventory contains an item with the name passed
	 * and false if otherwise
	 * 
	 * @param itemName
	 *            the item name to be checked for in the peon inventory
	 * @return true if the item name corresponds to an item in the inventory,
	 *         false if otherwise
	 */
	public boolean doesInventoryContainItemWithName(String itemName) {
		for (int i = 0; i < peonInventory.size(); i++) {
			if (peonInventory.get(i).getItem().getName() == itemName) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Getter method for a peon's digging tool (i.e. shovel)
	 * 
	 * @param name
	 *            the name of the digging tool (currently always "shovel")
	 * @return the digging tool item
	 */
	public Item getDiggingTool(String name) {
		for (int i = 0; i < peonInventory.size(); i++) {
			if (peonInventory.get(i).getItem().getName() == name) {
				return peonInventory.get(i).getItem();
			}
		}
		return null;
	}

	/**
	 * Method removes the digging tool (i.e. shovel) from the peon's inventory
	 * 
	 * @param name
	 *            the name of the digging tool (currently always "shovel") that
	 *            corresponds to the item to be removed
	 */
	public void removeDiggingTool(String name) {
		for (int i = 0; i < peonInventory.size(); i++) {
			if (peonInventory.get(i).getItem().getName() == name) {
				peonInventory.remove(i);
			}
		}
	}

	/**
	 * Returns true if the item is currently equipped by the poen
	 * 
	 * @param item
	 *            the item being observed in the equip slot
	 * @return true if equipped, false otherwise
	 **/
	public boolean isItemEquipped(Item item) {
		return item.getItem().equals(this.equipSlot);
	}

	/**
	 * Returns the number of remaning empty slots in a peon inventory
	 *
	 * @return the number of available slots in a peon inventory
	 **/
	public int inventorySlotsLeft() {
		return this.defaultMaxNumberOfItemSlots - this.peonInventory.size();

	}

	/**
	 * Returns string summary of inventory contents by combining them in a
	 * stringbuilder and converting it to a normal string.
	 *
	 * @return String representation of inventory contents, with each item and
	 *         its amount being returned as tuples
	 **/
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		if (this.peonInventory.isEmpty()) {
			return "Inventory is empty";
		} else {
			sb.append("Inventory contains: ");
			for (InventoryItem item : this.peonInventory) {
				sb.append(item.getAmount() + " " + item.getItem());
			}
		}
		return sb.toString();
	}

	/**
	 * Method equips a peon with the given item
	 * 
	 * @param item
	 *            the item to be equipped by the peon
	 * @return true if the item was successfully equipped, false if otherwise
	 */
	public boolean equipItem(Item item) {
		if (item.getType() == "none") {
			return false;
		} else if (!(this.peon.getClass().equals(item.getPeonClass()) || "all"
				.equals(item.getPeonClass()))) {
			return false;
		}
		switch (item.getType()) {
		case "head":
			if (this.headSlot == null) {
				this.headSlot = item;
			} else {
				addItem(this.headSlot);
				this.headSlot = item;
			}
			removeItem(item);
			return true;
		case "body":
			if (this.bodySlot == null) {
				this.bodySlot = item;
			} else {
				addItem(this.bodySlot);
				this.bodySlot = item;
			}
			removeItem(item);
			return true;
		case "legs":
			if (this.legsSlot == null) {
				this.legsSlot = item;
			} else {
				addItem(this.legsSlot);
				this.legsSlot = item;
			}
			removeItem(item);
			return true;
		case "shoes":
			if (this.shoesSlot == null) {
				this.shoesSlot = item;
			} else {
				addItem(this.shoesSlot);
				this.shoesSlot = item;
			}
			removeItem(item);
			return true;
		case "equip":
			if (this.equipSlot == null) {
				this.equipSlot = item;
			} else {
				addItem(this.equipSlot);
				this.equipSlot = item;
			}
			removeItem(item);
			return true;
		}
		return false;
	}

	/**
	 * Method dequips an item from a peon
	 * 
	 * @param item
	 *            the item to be dequipped
	 */
	public void dequipItem(Item item) {
		switch (item.getType()) {
		case "head":
			this.headSlot = null;
			addItem(item);
			break;
		case "body":
			this.bodySlot = null;
			addItem(item);
			break;
		case "legs":
			this.legsSlot = null;
			addItem(item);
			break;
		case "shoes":
			this.shoesSlot = null;
			addItem(item);
			break;
		case "equip":
			this.equipSlot = null;
			addItem(item);
			break;
		}
	}

	public Item getHeadSlot() {
		return this.headSlot;
	}

	public Item getBodySlot() {
		return this.bodySlot;
	}

	public Item getLegsSlot() {
		return this.legsSlot;
	}

	public Item getShoesSlot() {
		return this.shoesSlot;
	}

	public Item getEquipSlot() {
		return this.equipSlot;
	}

	/**
	 * The InventoryItem class represents an item in a peon's inventory, and
	 * stores the item itself and the quantitY of that item held. InventoryItem
	 * is therefore a tuple, of an item and an amount. InventoryItems are stored
	 * in a peonInventory. The stack limit of an item is the pre-defined
	 * defaultMaxItemStackLimit.
	 **/
	public class InventoryItem extends Item {
		// The name of an item
		private Item item;
		// The quantity of the item held in the inventory slot
		private int amount;
		// The maximum amount of an item that can be held in one inventory slot
		private int stackLimit;

		/**
		 * Constructor method that defines an inventory item
		 *
		 * @param item
		 *            -the name of the item being stored
		 * @param amount
		 *            -the quantity of the item in the inventory. Max amount =
		 *            stackLimit
		 **/
		public InventoryItem(Item item, int amount) {
			super(0, 0, 0, 0, item.getName(), amount);
			this.item = item;
			this.amount = amount;
			this.stackLimit = item.getStackLimit();
		}

		/**
		 * Getter method for the name of an inventory item
		 *
		 * @return the Item object tied to this slot
		 **/
		public Item getItem() {
			return this.item;
		}

		/**
		 * Getter method for the amount of an inventory item
		 *
		 * @return amount of items in slot, as an int
		 **/
		public int getAmount() {
			return this.amount;
		}

		/**
		 * Increments the count of an inventory item by the specified amount
		 *
		 * @param incrementAmount
		 *            the amount to increase the item count by
		 **/
		public void incrementAmount(int incrementAmount) {
			this.amount += incrementAmount;
		}

		/**
		 * Decrements the count of an inventory item by the specified amount
		 *
		 * @param decrementAmount
		 *            the amount to decrease the item count by
		 **/
		public void decrementAmount(int decrementAmount) {
			this.amount -= decrementAmount;
		}

		/**
		 * Returns true if the amount of an inventory item in a stack has
		 * reached the stack limit.
		 *
		 * @return true if item amount has reached stack limit, false otherwise
		 **/
		public boolean stackFull() {
			return (this.amount == this.stackLimit);
		}

	}
}