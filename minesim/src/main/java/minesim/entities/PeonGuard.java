package minesim.entities;

import java.util.Optional;

import minesim.tasks.AttackMob;
import minesim.tasks.Task;

public class PeonGuard extends Peon {

    private double mod = 1.5;

    public PeonGuard(int xpos, int ypos, int height, int width, String name) {
        super(xpos, ypos, height, width, name);
        setMaxHP(150);
        setHealth(150);
    }

    // gurads have higher attack than other peons
    @Override
    public int getAttack() {
        return (int) ((int) (super.getStrength() * mod)+getFightSkill());
    }

    @Override
    public void subtractHealth(int factor) {
        setHealth(super.getHealth() - factor);
        if (super.getHealth() > 150) {
            super.setHealth(150);
        }
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
    	return "Guard";
    }

    @Override

    public void customAIFuntion() {
        if (this.getCurrentTask().isPresent()) {
            return;
        } else {
            Optional<?> optionalMob = this.getParentWorld().getNearest(Mob.class, this);
            if (optionalMob.isPresent()) {
                WorldEntity enemy = (WorldEntity) optionalMob.get();
                int enemyXPOS = ((Mob) enemy).getXpos();
                int myXPOS = this.getXpos();
                // Means, if enemy is less than 200 pixels away
                if (Math.abs(myXPOS - enemyXPOS) < 200) {
                    if (myXPOS - enemyXPOS < 0) {
                        animateRight();
                    } else if (myXPOS - enemyXPOS > 0) {
                        animateLeft();
                    }
                    if (this.getCurrentTask().isPresent()) {
                        this.getCurrentTask().get().switchActiveFlag();
                    }
                    Task newtask = new AttackMob(this, (Mob) enemy);
                    this.updateTask(Optional.of(newtask));
                    newtask.switchActiveFlag();
                }
            }
        }
    }
}
