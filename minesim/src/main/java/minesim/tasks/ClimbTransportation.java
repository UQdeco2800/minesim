package minesim.tasks;

import java.util.Optional;

import minesim.entities.Peon;
import minesim.entities.items.Transportation;

/**
 * ClimbTransportation is the task that is triggered by a collision with any entity that extends
 * the transportation class (currently Ropes, Ladders, and Elevators). Peons can interact with
 * transportation in a number of different ways, based on the waypoint they are moving towards.
 *
 * @author Team Gray (Astrid, Riley, Yen)
 */
public class ClimbTransportation extends Task {

	Transportation actionableClimb;
	Transportation nextTransportabove;
	Transportation nextTransportbelow;
	Transportation Transportabove;
	Transportation Transportbelow;
	private int waypointX;
	private int waypointY;
	private Peon currentPeon;
	private Task prevTask;

	/**
	 * Super class and variable constructors for interacting with transportation
	 *
	 * @param parent         The peon completing this task
	 * @param transportation The transportation entity that the peon collided with
	 * @param waypointX      The peon's WalkTowards X waypoint prior to this task
	 * @param waypointY      The peon's WalkTowards Y waypoint prior to this task
	 * @param prevTask       The task that the peon was doing prior to this, so it can resume on
	 *                       completion
	 */
	public ClimbTransportation(Peon parent, Transportation transportation, int waypointX,
							   int waypointY, Task prevTask) {
		super(parent, waypointX, waypointY);
		this.actionableClimb = transportation;
		this.waypointX = waypointX;
		this.currentPeon = parent;
		this.waypointY = waypointY;
		this.prevTask = prevTask;
	}

	/**
	 * Move the peon towards their waypoint, and once they have reached the correct level
	 * call the overridden onCompletion method to finish the task and exit the transportation.
	 */
	@Override
	public void doTask() {
		super.doTask();
		/* If the peon's destination is parallel to any point on the current rope/ladder/elevator,
        skip ahead to the end of the task, and allow them to walk straight through without engaging */
		if (waypointY >= currentPeon.getYpos() && waypointY <= (currentPeon.getYpos() + 32)) {
			onCompletion();
		}
        /* If the peon's waypoint Y position is above their current position, allow them to climb up */
		else if (currentPeon.getYpos() > waypointY) {
			currentPeon.setXpos(actionableClimb.getXpos());
			climbUp();
		}
        /* If the peon's waypoint Y position is above their current position, allow them to climb down */
        else if (currentPeon.getYpos() < waypointY){
        	currentPeon.setXpos(actionableClimb.getXpos());
        	climbDown();
        }
        else{
        	onCompletion();
        } 
    }
    
    /**
     * Move the peon upwards until they reach the top of what they are currently climbing
     */
    public void climbUp(){
    	currentPeon.addCollisionIgnoredClass(actionableClimb.getClass());
    	currentPeon.isClimbing(true);
    	currentPeon.setEntityGravity(false);
    	if((currentPeon.getYpos() + 32) > this.actionableClimb.getYpos()){
    		currentPeon.setYpos(currentPeon.getYpos() - (1 * actionableClimb.getSpeed()));
    		getPeon().setTiredness(getPeon().getTiredness()+1);
    	}
    	else{
    		onCompletion();
    	}
    }

    /**
     * Move the peon downwards until they reach the bottom of what they are currently climbing
     */   
    public void climbDown(){
    	currentPeon.addCollisionIgnoredClass(actionableClimb.getClass());
    	currentPeon.isClimbing(true);
    	currentPeon.setEntityGravity(false);
    	if((currentPeon.getYpos()) <= (this.actionableClimb.getYpos() + 32) ){
    		currentPeon.setYpos(currentPeon.getYpos() + (1 * actionableClimb.getSpeed()));
    		getPeon().setTiredness(getPeon().getTiredness()+1);
    	}
    	else{
    		onCompletion();
    	}
    }

	/**
	 * If the peon is near their destination ypos, exit the ladder and resume their previous task.
	 * Otherwise, if there is a ladder above the peon, call a new climbing task on that ladder.
	 * Else, resume the previous task and mark this one as completed.
	 */
	@Override
	public void onCompletion() {
		//Check if there is a rope, ladder, or elevator above the peon
		if (currentPeon.getParentWorld().getNearestYabove(Transportation.class, currentPeon).isPresent()) {
			Transportabove = (Transportation)
					currentPeon.getParentWorld().getNearestYabove(Transportation.class, currentPeon).get();
			if (Transportabove.getYpos()> currentPeon.getYpos()-32){
			    nextTransportabove = Transportabove;
			}
		}
		//Check if there is a rope, ladder, or elevator below the peon
        if (currentPeon.getParentWorld().getNearestYbelow(Transportation.class, currentPeon).isPresent()) {
            Transportbelow = (Transportation)
                    currentPeon.getParentWorld().getNearestYbelow(Transportation.class, currentPeon).get();
            if (Transportbelow.getYpos() < currentPeon.getYpos()+32){
                nextTransportbelow = Transportbelow;
            }
        }


		//If there is a rope or ladder above the peon, and they still need to climb up higher, start a new task
		if (currentPeon.getYpos() > waypointY && nextTransportabove != actionableClimb && nextTransportabove != null) {
			Task newtask = new ClimbTransportation(currentPeon, nextTransportabove, waypointX, waypointY, prevTask);
			currentPeon.updateTask(Optional.of(newtask));
			newtask.switchActiveFlag();
		} else if (currentPeon.getYpos() < waypointY && nextTransportbelow != actionableClimb && nextTransportbelow != null) {
			Task newtask = new ClimbTransportation(currentPeon, nextTransportbelow, waypointX, waypointY, prevTask);
			currentPeon.updateTask(Optional.of(newtask));
			newtask.switchActiveFlag();
		} else {
			System.out.println("climb complete");
			currentPeon.standStill();
			currentPeon.setEntityGravity(true);
			currentPeon.updateTask(Optional.of(prevTask));
			prevTask.switchActiveFlag();
			currentPeon.isClimbing(false);
		}
	}
}
