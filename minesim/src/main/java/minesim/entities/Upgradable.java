package minesim.entities;

/**
 * IsUpgradable interface determines whether or not an item can be enhanced
 * within the world by a Peon. Upgradable items will likely receive their own
 * stats in future builds, and this interface will build upon changing the
 * values for weapon stats.
 *
 * @author Jamesg
 **/

public interface Upgradable {

	public int getCurrentLevel();

	public void upgradeItem();

	public String getPeonClass();

}
