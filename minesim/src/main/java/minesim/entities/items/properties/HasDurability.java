package minesim.entities.items.properties;

/**
 * HasDurability interface represents the item stat durability. Durability
 * represents the ability of an item to withstand damage. An item with a higher
 * durability will have its health stat decrement slower in comparison to an
 * item with a lower durability. [
 *
 * @author alana_clover
 **/
public interface HasDurability {
	/**
	 * Returns an items durability value
	 * 
	 * @return
	 */
	int getDurability();

	/**
	 * Sets an items durability to the specified amount. Method also checks that
	 * durability is valid (an intger between 0 and 1000
	 *
	 * @param durability
	 *            - the durability value to be set to for the item
	 *
	 */
	void setDurability(int durability);

}
