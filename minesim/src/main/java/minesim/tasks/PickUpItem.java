package minesim.tasks;

import java.util.Optional;

import minesim.entities.Inventory;
import minesim.entities.Peon;
import minesim.entities.items.Item;



/**
 * A task to allow peons to interact with the requisition store.
 */

enum ItemDistance {
    ITEM_FAR,
    ITEM_CLOSE
}
public class PickUpItem extends Task   {
    static final int distance_thresh =  200;
    ItemDistance state = ItemDistance.ITEM_FAR;

    Inventory inventory;
    Item item;

    public PickUpItem(Peon parent, Item item) {
        super(parent, 0, 0);
        this.item = item;
        //this.inventory = super.getPeon().inven
    }

    @Override
    public void doTask() {
        super.doTask();
        if (state == ItemDistance.ITEM_FAR) {
            if (!getPeon().moveTowards(item.getXpos(), item.getYpos(), 10)) {
                this.state = ItemDistance.ITEM_CLOSE;
                getPeon().pickUpItem(item);
                getPeon().updateTask(Optional.<Task>empty());
            }
        } else {
        	getPeon().pickUpItem(item);
            getPeon().updateTask(Optional.<Task>empty());
        }

    }
}
