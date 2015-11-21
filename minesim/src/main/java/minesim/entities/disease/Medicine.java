package minesim.entities.disease;
import java.util.HashSet;
/**
 * Class for all types of Medicines to inherit.
 */
public class Medicine {
    private final HashSet<Class<? extends Illness>> targets;

    /**
     * Constructor for default Medicine
     */
    public Medicine() {
        this.targets = new HashSet<Class<? extends Illness>>();
        this.targets.add(Illness.class);
    }

    public int getPotency() {
        return -10;
    }
}
