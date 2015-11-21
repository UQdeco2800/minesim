package minesim.entities.items.properties;

/**
 * HasSpeed represents the speed property of an item, or the speed stat. Speed
 * refers to how quickly an item can achieve a task. Item speed can increase
 * when items are upgraded.
 * 
 * @author alana_clover
 *
 */
public interface HasSpeed {
	/**
	 * returns the speed value for this entity
	 *
	 * @return speed
	 */
	int getSpeed();

	/**
	 * set the speed value to the supplied value will not set above 10
	 */
	void setSpeed(int speed);

	/**
	 * increments speed by the supplied value will not increment above 10
	 */
	void addSpeed(int speed);
}
