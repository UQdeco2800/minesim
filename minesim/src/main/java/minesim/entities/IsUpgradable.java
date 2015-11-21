package minesim.entities;

/**
 * IsUpgradable interface determines whether or not an item can be enhanced within the world by a
 * Peon. Upgradable items will likely receive their own stats in future builds, and this interface
 * will build upon changing the values for weapon stats.
 *
 * @author Jamesg
 **/

public interface IsUpgradable {
    //upgradeEnable determines whether methods here can be used by the entity
    boolean upgradeEnable = false;

    /**
     * getUpgradable should simply return upgradeEnable or a similar value once the interface is
     * expanded further. For
     **/
    //void getUpgradable();

    /* setUpgradableStatus should change upgradeEnable status, which is useful
     * because we may want to temporarily/permanently alter this ability as a
     * form of punishment, or if the entity using the interface has a change of
     * state. For now upgrades just increase an item's health.
     */
    //void setUpgradableStatus(boolean status);
    
    public int getLevelRequirementToUpgrade();
    
    public void setLevelRequirementToUpgrade();
    
    public int getCurrentLevel();

    public void upgradeItem();
    
    public int getLevel();
    
    public String getPeonClass();
    
    public String getType();
    
}
