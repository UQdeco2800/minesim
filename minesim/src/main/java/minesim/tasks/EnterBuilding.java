package minesim.tasks;

import java.util.Optional;

import minesim.entities.Building;
import minesim.entities.Peon;

/**
 * Enter Building is a specialty task to interact with all buildings.
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class EnterBuilding extends Task {

    private Building targetBuilding;
    private int targetx = 0;
    private int targety = 0;
    //Store peons position to check if stuck..
    private int peonx = 0;
    private int peony = 0;

    /**
     * Run the super class Task as usual sending in the peon and the x destination of the building
     *
     * @param parent   the Peon doing the Task
     * @param building the Building the Peon is targetting
     */
    public EnterBuilding(Peon parent, Building building) {
        super(parent, building.getXpos(), building.getYpos());
        this.targetBuilding = building;
        
        targetx = this.targetBuilding.getXpos() + targetBuilding.width/2;
        targety = this.targetBuilding.getYpos();
    }


    /**
     * As this is a 2D game we only have the x coordinate ie the scrolling sideways coordinate
     */
    @Override
    public void doTask() {
    	
    	/* continually move peon closer to targetBuilding */
    	peonx = getPeon().getXpos();
    	peony = getPeon().getYpos();
    	getPeon().moveTowards(targetx, targety, 10);
    	
    	/* Check if peon is within threshold of target Peon needs to be in the
    	 * middle of the building zone
    	 */
    	if (getPeon().getXpos() > targetx - targetBuilding.width/2 && 
    			getPeon().getXpos() < targetx + targetBuilding.width/2 &&
    			getPeon().getYpos() >= targety && 
    			getPeon().getYpos() < targety + targetBuilding.height) {
    		
    		//now im inside building boundary
    		
    		if(!targetBuilding.isConstructed()) {
    			targetBuilding.interact(getPeon());
    			endThisTask();
    		} else if (getPeon().getBuildingBonus() > 0) {
    			//do interaction
    			
    			int[] temp = getPeon().getBuildingBonusArray();
    			//check if i already have this building's buff cant go twice
    			if(!targetBuilding.isConstructed()) {
        			targetBuilding.interact(getPeon());
        			endThisTask();
        		} else if (temp[targetBuilding.getBuildingType()] == 1) {
	            	endThisTask();
	            } else {
	            	targetBuilding.interact(getPeon());
	            	// We entered the building
	                getPeon().addBuildingBonusStack(-1);
	                getPeon().addBuildingBuff(targetBuilding.getBuildingType());
	                endThisTask();
	            }
	            
    		} else {
    			 endThisTask();
    		}
    	}
    	
    	//check if peon is stuck.. then complete task if so.
    	if (peonx == getPeon().getXpos() && peony == getPeon().getYpos()) {
    		endThisTask();
    	}
    	
    }
    
    public void endThisTask() {
    	onCompletion();
        getPeon().standStill();
        getPeon().updateTask(Optional.<Task>empty());
    }

}
