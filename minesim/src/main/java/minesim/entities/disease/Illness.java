package minesim.entities.disease;

import java.util.ArrayList;
import java.util.HashSet;

import minesim.entities.Peon;

/**
 * Top level concrete implementation of an agent affecting disease
 * No added functionality or specialty
 * Created by Andres on 15/10/2015.
 */

public class Illness extends Disease<Peon, Medicine> {
    private HashSet<Peon> targets;
    private HashSet<Medicine> treatments;
    /**
     * Constructor for default Illness
     * A general illness can target any agent
     */
    public Illness() {
        this.targets = new HashSet<Peon>();
//        this.targets.add(p);
        this.treatments = new HashSet<Medicine>();
//        this.treatments.add(m);
    }

    public String getName() {
        return "Illness";
    }
}
