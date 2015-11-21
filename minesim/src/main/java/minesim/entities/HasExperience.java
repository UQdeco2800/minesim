package minesim.entities;

public interface HasExperience {

    int level = 1;
    int base_XP = 200;
    int currentExperience = 0;

    /**
     * Add an x amount of experience to a Peon
     *
     * @require amount >= 0
     */
    void addExperience(int amount);

    /**
     * Get the current experience of Peon
     */
    int getExperience();

    /**
     * Set the current experience of a Peon
     *
     * @require amount >= 0 && amount < (this.base_XP * Math.pow((getLevel()+1), 2)
     * @ensure amount == this.experience
     */
    void setExperience(int amount);

    /**
     * Get the current level of a Peon
     */
    int getLevel();

    /**
     * Set the level of a Peon
     *
     * @require level > 0
     * @ensure level <= 100
     */
    void setLevel(int level);

    /**
     * Returns the amount of experience required for next level up
     */
    public int experienceRequired();
}
