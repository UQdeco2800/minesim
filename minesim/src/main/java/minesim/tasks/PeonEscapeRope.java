package minesim.tasks;

import minesim.entities.Peon;

public class PeonEscapeRope extends Task {
	
	private int destxpos;
	private int destypos;
	
    public PeonEscapeRope(Peon parent, int destxpos, int destypos) {
		super(parent, destxpos, destypos);
		this.destxpos = destxpos;
		this.destypos = destypos;
	}

	@Override
    public void doTask() {
        super.doTask();
        getPeon().standStill();
        onCompletion();
        getPeon().setXpos(destxpos);
        getPeon().setYpos(destypos);
    }
}
