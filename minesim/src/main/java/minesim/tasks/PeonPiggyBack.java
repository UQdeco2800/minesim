package minesim.tasks;

import java.util.Optional;
import minesim.entities.Peon;
import minesim.pathfinder.Pathfinder;

public class PeonPiggyBack extends Task {
    private int xpos, ypos, steps;
    private Pathfinder pathfinder;
    private String[] solution;
    private Peon target;
    private Peon targetCpy;
    private int taskComp = -1;
    public PeonPiggyBack(Peon parent, Peon targetInv, Pathfinder pathfinder) {
        super(parent, targetInv.getXpos(), targetInv.getYpos());
        xpos = parent.getXpos();
        ypos = parent.getYpos();
        target = targetInv;
        this.pathfinder = pathfinder;
        solution = new String[3000];
        steps = 0;
    }

    @Override
    public void doTask() {
        if (taskComp == 0){
            pathTo();
            int xDist = getPeon().getXpos() - target.getXpos();
            int yDist = getPeon().getYpos() - target.getYpos();
            //if we are within range of our target peon
            if ((xDist < 32 && xDist > -32) && (yDist < 32 && yDist > -32)){
                taskComp = 1;
            }
        } else if (taskComp == 1){
            getPeon().getParentWorld().removeEntityFromWorld(target);
            makePath(0, 25);
            taskComp = 2;
        } else {
            pathHome();
        }
    }
    
    private void pathTo() {
        if (steps < solution.length / 3) {
            if (solution[steps * 3].equals("m")) {
                if (!getPeon().moveTowards(Integer.parseInt(solution[steps * 3 + 1]) * 16, ypos, 10)) {
                    steps++;
                }
                getPeon().setEntityGravity(true);
            } else {
                getPeon().setEntityGravity(false);
                if (!getPeon().moveVertical(Integer.parseInt(solution[steps * 3 + 2]) * 16, 20)) {
                    steps++;
                }

            }
        } else {
            makePath(target.getXpos(), target.getYpos());
        }
    }
    
    private void makePath(int destX, int destY) {
        String s = new String();
        s = this.pathfinder.pathFind(getPeon().getXpos() / 16, getPeon().getYpos() / 16, destX / 16, destY / 16);
        solution = s.split(" ");
    }
    
    private void pathHome() {
        if (steps < solution.length / 3) {
            if (solution[steps * 3].equals("m")) {
                if (!getPeon().moveTowards(Integer.parseInt(solution[steps * 3 + 1]) * 16, ypos, 10)) {
                    steps++;
                }
                getPeon().setEntityGravity(true);
            } else {
                getPeon().setEntityGravity(false);
                if (!getPeon().moveVertical(Integer.parseInt(solution[steps * 3 + 2]) * 16, 20)) {
                    steps++;
                }
            }
        } else {
            onCompletion();           
        }
    }
    
    @Override
    public void onCompletion() {
        super.onCompletion();
        getPeon().standStill();
        getPeon().getParentWorld().addEntityToWorld(target);
        target.setXpos(getPeon().getXpos());
        target.setYpos(getPeon().getYpos());
        target.setCollapsed(false);
        target.standStill();
        //task is done
        getPeon().updateTask(Optional.<Task>empty());
    }
    
    @Override
    public void switchActiveFlag() {
        super.switchActiveFlag();
        makePath(target.getXpos(), target.getYpos());
        if (taskComp == 2){
            getPeon().getParentWorld().addEntityToWorld(target);
            target.setXpos(getPeon().getXpos());
            target.setYpos(getPeon().getYpos());
            target.setCollapsed(true);
        } else if (taskComp == -1){
            taskComp = 0;
        } else {
            taskComp = -1;
            target.setCollapsed(true);
        }
        
    }
}
