package minesim.entities;

public interface HasTiredness {
    /**
     * return the entities current tiredness
     */
    int getTiredness();

    /**
     * set tiredness to the supplied value will not set above 1000
     * @ensure tiredness <= 1000 && tiredness >= 0
     */
    void setTiredness(int tiredness);

    /**
     * increment tiredness by the supplied value will not increment above 1000
     * @ensure tiredness <= 1000 && tiredness >= 0
     */
    void addTiredness(int tiredness);
}
