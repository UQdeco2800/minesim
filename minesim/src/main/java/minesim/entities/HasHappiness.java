package minesim.entities;

public interface HasHappiness {
    
    /**
     * Return the happiness level of a peon
     * @return
     */
    int getHappiness();

    /**
     * Set the happiness of peon
     * @ensure happiness <= 100 && happiness >= 0
     */
    void setHappiness(int happiness);

    /**
     * Add happiness or remove happiness from a peon by passing in positive or negative value
     * @ensure happiness <= 100 && happiness >= 0
     */
    void addHappiness(int Happiness);
}
