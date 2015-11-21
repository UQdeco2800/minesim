package minesim.entities;

public class PeonGather extends Peon {

    private double mod = 1.5;

    public PeonGather(int xpos, int ypos, int height, int width, String name) {
        super(xpos, ypos, height, width, name);
    }

    @Override
    public int getSpeed() {
        return (int) (super.getSpeed() * mod);
    }

    @Override
    public int getStrength() {
        //we don't want peons being completely unable to complete tasks, so the minimum return is 10
        if (super.getStrength() >= 2) {
            return (super.getStrength() / 2);
        }
        //else, returning the strength/2 would be cast into an int as 0, which we don't want, so just return the minimum value.
        else {
            return 1;
        }
    }

    //gatherers tire four times as fast as other peons
    @Override
    public void taskTiredness(int tired, int annoyed) {
        super.addTiredness(tired * 4);
        super.addAnnoyance(annoyed);
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
    	return "Gather";
    }

}
