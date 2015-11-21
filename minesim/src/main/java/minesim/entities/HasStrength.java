package minesim.entities;

public interface HasStrength {
    /**
     * returns the current value for strength
     */
    int getStrength();

    /**
     * set Strength to the supplied value will not set above 10
     * @ensure strength <= 10 && strength >= 0
     */
    void setStrength(int strength);

    /**
     * increment Strength by the supplied value will not increment above 10
     * @ensure strength <= 10 && strength >= 0
     */
    void addStrength(int strength);
}
