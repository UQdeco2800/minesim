package minesim.entities.items.properties;

/**
 * IsUsable represents the item property of usability. Item type classes that
 * implement the IsUsable interface are items that can be used by peons. Peons
 * that "use" items will have this item in their inventory. This interface
 * describes how items can be set to be used and set to being not used.
 * 
 * @author alana_clover
 *
 */

public interface IsUsable {
	// getUseStatus returns a boolean saying whether or not an item can be used
	public boolean getUsedStatus();

	/**
	 * setItemToBeUsed should change getUsedStatus for the entity its attached
	 * to.
	 */
	public void setItemToBeUsed();

	/*
	 * removeItemBeingUsed should allow an item to be removed upon use.
	 */
	public void removeItemBeingUsed();

}
