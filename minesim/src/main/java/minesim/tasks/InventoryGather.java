package minesim.tasks;

import java.util.Optional;

import minesim.entities.Inventory.InventoryItem;
import minesim.entities.items.Item;
import minesim.entities.items.Transportation;
import minesim.entities.Peon;
import minesim.entities.PeonMiner;
import minesim.pathfinder.Pathfinder;

public class InventoryGather extends Task {
    private int xpos, ypos, steps;
    private Pathfinder pathfinder;
    private String[] solution;
    private Peon target;
   
    public InventoryGather(Peon parent, Peon targetInv, Pathfinder pathfinder) {
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
        int xDist = getPeon().getXpos() - target.getXpos();
        int yDist = getPeon().getYpos() - target.getYpos();
        //if we are within range of our target peon
        if ((xDist < 32 && xDist > -32) && (yDist < 32 && yDist > -32)){
            onCompletion();
        } else {
            pathTo();
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
            makePath();            
        }
    }
    
    public void makePath() {
        String s = new String();
        s = this.pathfinder.pathFind(getPeon().getXpos() / 16, getPeon().getYpos() / 16, target.getXpos() / 16, target.getYpos() / 16);
        solution = s.split(" ");
    }
    
    @Override
    public void onCompletion() {
        Item invItem;
        super.onCompletion();
        getPeon().standStill();
        ((PeonMiner) target).requestGather = false;
        //transfer as many items as possible
        for (int i = 0; i < target.getInventory().getInventory().size(); i++){
           invItem = target.getInventory().getInventory().get(i);
           int x = getPeon().getInventory().findIndexOfItemInInventory(invItem);
           if (x != -1){
               while ((!getPeon().getInventory().getInventory().get(x).stackFull()) && target.getInventory().doesInventoryContain(invItem)){
                   getPeon().getInventory().addItem(invItem);
                   target.getInventory().removeItem(invItem);       
               }
           } else if (getPeon().getInventory().inventorySlotsLeft() > 0){
               while (target.getInventory().doesInventoryContain(invItem)){
                   getPeon().getInventory().addItem(invItem);
                   target.getInventory().removeItem(invItem);
               }
           } 
        }
        //task is done
        getPeon().updateTask(Optional.<Task>empty());
    }
    
    @Override
    public void switchActiveFlag() {
        super.switchActiveFlag();
        makePath();
    }
}
