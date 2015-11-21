package minesim.tasks;

import java.util.Optional;

import minesim.entities.Peon;
import minesim.entities.items.Transportation;
import minesim.pathfinder.Pathfinder;

public class WalkTowards extends Task {
    private int xpos, ypos, steps;
    private Pathfinder pathfinder;
    private String[] solution;
    private int activated;

    public WalkTowards(Peon parent, int destxpos, int destypos, Pathfinder pathfinder) {
        super(parent, destxpos, destypos);
        xpos = destxpos;
        ypos = destypos;
        this.pathfinder = pathfinder;
        activated = 0;
    }

    @Override
    public void doTask() {
    	if (activated != 0) {
	        if (steps < solution.length / 3) {
	            if (solution[steps * 3].equals("m")) {
	            	getPeon().setEntityGravity(true);
	            	if (steps + 1 >= solution.length / 3) {
	            		if (!getPeon().moveTowards(xpos, ypos, 10)) {
	                		getPeon().setEntityGravity(true);
	                		System.out.print("walking complete");
	                		onCompletion();
	                		getPeon().standStill();
	                	}
	            	} else {
	            		if (solution[steps * 3 + 3].equals("c")) {
	            			getPeon().setEntityGravity(false);
	            		}
	            	}
	                if (!getPeon().moveTowards(Integer.parseInt(solution[steps * 3 + 1]) * 16, ypos, 10)) {
	                    steps++;
	                    setAnimation();
	                }
	                
	            } else  if (solution[steps * 3].equals("c")) {
	            	getPeon().setEntityGravity(false);
	            	Task newtask = new ClimbTransportation(getPeon(), (Transportation) 
	            			getPeon().getParentWorld().getEntityForCoordinates(
	            					getPeon().getXpos(), getPeon().getYpos(),Transportation.class).get(),
							Integer.parseInt(solution[steps * 3 + 1]) * 16, 
							Integer.parseInt(solution[steps * 3 + 2]) * 16, 
							this);
	            	steps++;
					getPeon().updateTask(Optional.of(newtask)); 
	            }
	            else {
	                getPeon().setEntityGravity(false);
	                if (!getPeon().moveVertical(Integer.parseInt(solution[steps * 3 + 2]) * 16, 20)) {
	                    steps++;
	                    setAnimation();
	                }
	
	            }
	        } else {
	        	onCompletion();
	        	getPeon().standStill();
	        }
    	}
    }
    
    @Override
    public void switchActiveFlag(){
    	super.switchActiveFlag();
    	if (activated == 0){
	        solution = new String[3000];
	        String s = new String();
	        s = this.pathfinder.pathFind(getPeon().getXpos() / 16, getPeon().getYpos() / 16, xpos / 16, ypos / 16);
	        System.out.println(s);
	        steps = 99999;
	        if (s != null) {
	        	solution = s.split(" ");
	        	activate();
	        	getPeon().removeCollisionIgnoredClass(Transportation.class);
	        	steps = 0;
	        } else {
	        	onCompletion();
	        	getPeon().standStill();
	        }
    	}
    }
    
    private void activate(){
    	activated = 1;
    }

    private void setAnimation() {
        if (steps < solution.length / 3){
            if (solution[steps * 3].equals("m")){
                if (Integer.parseInt(solution[steps * 3 + 1]) * 16 > getPeon().getXpos()){
                    getPeon().animateRight();
                } else {
                    getPeon().animateLeft();
                }
            }
        }
    }
}
