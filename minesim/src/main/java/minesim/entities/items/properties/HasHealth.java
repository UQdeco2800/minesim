package minesim.entities.items.properties;

/**
 * HasHealth interface represents the item stat health. A new item will always
 * have a full health (100), and will decrement down to 0 (which is when the
 * item is dead) as the item is used.
 *
 * @author alana_clover
 */
public interface HasHealth {

	/**
	 * return this entities current health value
	 */
	int getHealth();

	/**
	 * set health to the supplied value and ensure it does not exceed maximum of
	 * 100
	 */
	void setHealth(int health);

	// Item is dead when item.getHealth() <= 0
	Boolean isDead();

	/**
	 * reduce health by the supplied amount and ensure that health does not fall
	 * below 0
	 */
	void subtractHealth(int factor);

}
