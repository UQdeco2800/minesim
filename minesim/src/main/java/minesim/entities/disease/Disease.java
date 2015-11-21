package minesim.entities.disease;

import java.util.ArrayList;

public abstract class Disease<T1, T2> {

    // Severity will be incorporated in what kind of damage it does per tick
    private int severity;
    // The likelihood of infecting neighbouring plants / people per tick
    private int contagiousness;
    // Current health of the disease.  Reduced by treatments
    private int health;
    // How long the disease will last
    private int lifetime;
    //List of targets and treatments
    private ArrayList<T1> targets;
    private ArrayList<T2> treatments;

    /**
     * Test if an object can be infected
     * @param obj
     * @return True iff obj is in the targets list
     */
    public boolean canInfect(Object obj) {
        if (this.targets.contains(obj)) {
            return true;
        }
        return false;
    }


    public boolean isVulnerableTo(Object obj) {
        if (this.treatments.contains(obj)) {
            return true;
        }
        return false;
    }

    public void kill() {
        this.health = 0;
    }

    public int getHealth() {
        return health;
    }


    public int getSeverity() {
        return severity;
    }


    public void setSeverity(int severity) {
        this.severity = severity;
    }


    public int getContagiousness() {
        return contagiousness;
    }


    public void setContagiousness(int contagiousness) {
        this.contagiousness = contagiousness;
    }


    public int getLifetime() {
        return lifetime;
    }


    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }


    public ArrayList<T1> getTargets() {
        return targets;
    }


    public ArrayList<T2> getTreatments() {
        return treatments;
    }
}