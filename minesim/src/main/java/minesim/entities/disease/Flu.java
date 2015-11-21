package minesim.entities.disease;

/**
 * Created by Andres on 15/10/2015.
 */
public class Flu extends Illness {

    /**
     * Default constructor for Influenza.
     * Sets the severity, contagiousness, and lifetime of the disease in
     * order for it to be used to infect peons.
     * @param severity derived from the Illness class
     * @param contagiousness derived from the Illness class
     * @param severity derived from the Illness class
     */
    public Flu() {
        this.setSeverity(30);
        this.setContagiousness(50);
        this.setLifetime(10);
    }
    @Override
    public String getName() {
        return "Flu";
    }
}
