package minesim.entities;

public interface HasRecovery {
    
    /**
     * Returns the rate at which the peon will recover health
     */
    int getRecoveryRate();
    
    /**
     * Set the rate of which a peon will regain health per clock tick
     * @param rateOfRecovery
     */
    void setRecoveryRate(int rateOfRecovery);
}
