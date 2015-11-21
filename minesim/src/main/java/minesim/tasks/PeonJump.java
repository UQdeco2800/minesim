package minesim.tasks;

import java.util.Optional;

import minesim.entities.Peon;

public class PeonJump extends Task {

    public PeonJump(Peon parent, int destxpos, int destypos) {
        super(parent, destxpos, destypos);
    }

    @Override
    public void doTask() {
        super.doTask();
        //JUMP
        //maybe need to construct again later
        if (!(getPeon().moveVertical(this.getDestYPos(), 2))) {
            // JUMP has completed
            getPeon().standStill();
            onCompletion();
            getPeon().setGravity(15);
            getPeon().setEntityGravity(true);
            getPeon().updateJump(Optional.<Task>empty());
        }
    }
}
