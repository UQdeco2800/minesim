package minesim.entities.disease;

import java.util.HashSet;

import common.disease.Disease;
import minesim.entities.Peon;

/**
 * Top level concrete implementation of an agent affecting disease
 * No added functionality or specialty
 * Created by Andres on 15/10/2015.
 */

public class Illness extends Disease<Peon, Medicine> {

    /**
     * Constructor for default Illness
     * A general illness can target any agent
     */
    public Illness() {
        this.targets = new HashSet<Class<? extends Peon>>();
        this.targets.add(Peon.class);
        this.treatments = new HashSet<Class<? extends Medicine>>();
        this.treatments.add(Medicine.class);
    }

    public String getName() {
        return "Illness";
    }
}
