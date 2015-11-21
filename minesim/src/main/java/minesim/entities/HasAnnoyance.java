package minesim.entities;

public interface HasAnnoyance {
    /**
     * increments annoyance by the supplied value will not set above 1000
     * @ensure annoyance <= 1000 && annoyance >= 0
     */
    void addAnnoyance(int annoyance);

    /**
     * returns the current annoyance value of an entity
     */
    int getAnnoyance();

    /**
     * sets the annoyance value to the supplied value will not set above 1000
     * @ensure annoyance <= 1000 && annoyance >= 0
     */
    void setAnnoyance(int annoyance);
}
