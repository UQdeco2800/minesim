package minesim.tasks;

import minesim.entities.Peon;
import minesim.entities.RequisitionStore;

import java.util.Optional;


/**
 * A task to allow peons to interact with the requisition store.
 */

enum StoreDistance {
    STORE_FAR,
    STORE_CLOSE
}
public class UseStore extends Task   {
    static final int distance_thresh =  200;
    StoreDistance state = StoreDistance.STORE_FAR;

    RequisitionStore store;

    public UseStore(Peon parent, RequisitionStore store) {
        super(parent, 0, 0);
        this.store = store;
    }

    @Override
    public void doTask() {
        super.doTask();
        if (state == StoreDistance.STORE_FAR) {
            if (!getPeon().moveTowards(store.getXpos(), store.getYpos(), 10)) {
                this.state = StoreDistance.STORE_CLOSE;
                store.interact(getPeon());
                getPeon().updateTask(Optional.<Task>empty());
            }
        } else {
            store.interact(getPeon());
            getPeon().updateTask(Optional.<Task>empty());
        }

    }
}
