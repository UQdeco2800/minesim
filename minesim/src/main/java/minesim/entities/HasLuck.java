package minesim.entities;

/**
 * Created by Andres on 1/09/2015.
 */
public interface HasLuck {
    /**
     * return the luck value for this peon
     */
    int getLuck();

    /**
     * set luck to the supplied value
     * @ensure luck <= 10 && luck >= 0
     */
    void setLuck(int luck);

    /**
     * increment luck by the supplied value
     * @ensure luck <= 10 && luck >= 0
     */
    void addLuck(int luck);
}
