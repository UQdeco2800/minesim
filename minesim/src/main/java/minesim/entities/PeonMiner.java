package minesim.entities;

import minesim.tasks.InventoryGather;

public class PeonMiner extends Peon {

    private double mod = 1.5;
    public boolean requestGather = false;
    //Bonus to mining & less tiredness
    public PeonMiner(int xpos, int ypos, int height, int width, String name) {
        super(xpos, ypos, height, width, name);

    }
    
    @Override
    public void onTick(){
        super.onTick();
        //miners will request gatherers to empty their inventories if they have 3 or fewer inventory slots, and they haven't already asked since they last emptied their inventory
        //(set to less than 9 for testing purposes)
        if ((this.getInventory().inventorySlotsLeft() < 9) && (!requestGather)){
            requestGather = true;
            this.getParentWorld().addTask(new InventoryGather(new Peon(16, 16, 0, 0, "tempPeon"), this, getParentWorld().getPathfinder()));
        }
    }
    
    @Override
    public int getStrength() {
        return (int) (super.getStrength() * mod);
    }

    public double getMod() {
        return this.mod;
    }

    public void setMod(double newMod) {
        this.mod = newMod;
    }

    public void addMod(double modAdd) {
        this.setMod(this.getMod() + modAdd);
    }
    
    @Override
    public String getPeonClass() {
    	return "Miner";
    }
}
