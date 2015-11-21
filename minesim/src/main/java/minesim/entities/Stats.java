/*
 * Team Stonebraker: Peon Stats class
 */

package minesim.entities;

import minesim.entities.items.properties.HasHealth;
import minesim.entities.items.properties.HasSpeed;
import minesim.events.tracker.PeonDamagedTracker;
import minesim.events.tracker.PeonDeathTracker;

public class Stats extends WorldEntity implements HasTiredness, HasHealth, HasHappiness, HasStrength, HasSpeed,
        HasAnnoyance, HasLuck, HasExperience, HasRecovery, HasSkills {

    private int currentExperience = 0;
    private int level = 1;
    private int luck = 1;
    private int annoyance = 0;
    private int speed = 1;
    private int strength = 1;
    private int happiness = 100;
    
    private double tradeSkill = 1.0;
    private double inteligenceSkill = 1.0;
    private double fightSkill = 1.0;
    
    private int health = 100;
    private int maxHP = 100;
    boolean deadFlag = Boolean.FALSE;
    private boolean collapsed = Boolean.FALSE;

    private int tiredness = 0;
    private int recoveryRate = 100;

    protected Stats(int xpos, int ypos, int height, int width) {
        super(xpos, ypos, height, width);
    }

    @Override
    public void addExperience(int amount) {
        this.currentExperience += amount;
    }

    @Override
    public int getExperience() {
        return this.currentExperience;
    }

    @Override
    public void setExperience(int amount) {
        this.currentExperience = amount;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        this.currentExperience = 0;
    }

    @Override
    public int experienceRequired() {
        int base_XP = 50;
        return (int) (base_XP * Math.pow(getLevel() + 1, 2));
    }

    @Override
    public int getLuck() {
        return this.luck;
    }

    @Override
    public void setLuck(int luck) {
        this.luck = luck;
        if (this.getLuck() > 10) {
            this.luck = 10;
        } else if (this.getLuck() < 0) {
            this.luck = 1;
        }
    }

    @Override
    public void addLuck(int luck) {
        this.setLuck(this.getLuck() + luck);
    }

    @Override
    public void addAnnoyance(int annoyance) {
        this.setAnnoyance(this.getAnnoyance() + annoyance);
    }

    @Override
    public int getAnnoyance() {
        return this.annoyance;
    }

    @Override
    public void setAnnoyance(int annoyance) {
        this.annoyance = annoyance;
        if (this.getAnnoyance() > 1000) {
            this.annoyance = 1000;
        } else if (this.getAnnoyance() < 0) {
            this.annoyance = 0;
        }
    }

    @Override
    public int getSpeed() {
        return this.speed;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
        if (this.getSpeed() > 10) {
            this.speed = 10;
        } else if (this.getSpeed() < 1) {
            this.speed = 1;
        }
    }

    @Override
    public void addSpeed(int speed) {
        this.setSpeed(this.getSpeed() + speed);
    }

    @Override
    public int getStrength() {
        return this.strength;
    }

    @Override
    public void setStrength(int strength) {
        this.strength = strength;
        if (this.getStrength() > 10) {
            this.strength = 10;
        } else if (this.getStrength() < 1) {
            this.strength = 1;
        }
    }

    @Override
    public void addStrength(int strength) {
        this.setStrength(this.getStrength() + strength);
    }

    @Override
    public int getHappiness() {
        return this.happiness;
    }

    @Override
    public void setHappiness(int happiness) {
        this.happiness = happiness;
        if (this.getHappiness() > 100) {
            this.happiness = 100;
        } else if (this.getHappiness() < 0) {
            this.happiness = 0;
        }
    }

    @Override
    public void addHappiness(int happiness) {
        this.setHappiness(this.getHappiness() + happiness);
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
        if (this.getHealth() >= maxHP) {
            this.health = maxHP;
        }
    }

    @Override
    public Boolean isDead() {
        return this.health <= 0;
    }

    @Override
    public void subtractHealth(int factor) {
        this.setHealth(this.getHealth() - factor);
        if (this.isDead() && this.deadFlag == false) {
            PeonDeathTracker.peonDied();
            this.deadFlag = true;
        } else {
            PeonDamagedTracker.peonDamaged();
        }
    }

    /**
     * Increase the max health of a peon
     * @param hp
     */
    public void setMaxHP(int hp) {
        this.maxHP = hp;
    }

    /**
     * Set the maximum health a peon can recover up to
     * @return maxHP
     */
    public int getMaxHP() {
        return this.maxHP;
    }

    /**
     * Return the state if peon has collapsed or not
     * @return collapsed
     */
    public boolean getCollapse() {
        return this.collapsed;
    }

    /**
     * Set a peon status to collapsed
     * @require state = Boolean.TRUE || state = Boolean.FALSE
     */
    public void setCollapse(boolean state) {
        this.collapsed = state;
    }

    @Override
    public int getTiredness() {
        return this.tiredness;
    }

    @Override
    public void setTiredness(int tiredness) {
        this.tiredness = tiredness;
        if (this.getTiredness() > 999) {
            this.tiredness = 1000;
        } else if (this.getTiredness() < 0) {
            this.tiredness = 0;
        }
    }

    @Override
    public void addTiredness(int tiredness) {
        this.setTiredness(this.getTiredness() + tiredness);
    }

    @Override
    public int getRecoveryRate() {
        return recoveryRate;
    }

    @Override
    public void setRecoveryRate(int rateOfRecovery) {
        this.recoveryRate = rateOfRecovery;
    }

    @Override
    public double getTradeSkill() {
        return tradeSkill;
    }

    @Override
    public void setTradeSkill(double skill) {
        tradeSkill = skill;
        
    }

    @Override
    public double getIntelligenceSkill() {
        return inteligenceSkill;
    }

    @Override
    public void setIntelligenceSkill(double skill) {
        this.inteligenceSkill = skill;
    }

    @Override
    public double getFightSkill() {
        return fightSkill;
        
    }

    @Override
    public void setFightSkill(double skill) {
        this.fightSkill = skill;
    }

}
