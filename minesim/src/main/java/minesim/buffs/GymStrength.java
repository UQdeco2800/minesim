package minesim.buffs;

import minesim.entities.Peon;
import minesim.entities.WorldEntity;

/**
 * GymStrength extends the Buff class and effects Peons.
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class GymStrength extends Buff {

	/**
	 * This set's up the buff for use with a name description and image with a 
	 * cooldown
	 */
	public GymStrength() {
		super();
		setName("Gym Muscles");
		setDesc("Speed Strength");
		setImageUrl("BuffImages/strength.png");
		cooldownmax = 6000;
		setCooldown(cooldownmax);
	}
	
	/**
     * buffTick is what the buff will do to it's owner entity onTick
     * @param sender the entity effected by the buff
     */
	@Override
	public void buffTick(WorldEntity sender) {

		super.buffTick(sender);

		
		if (sender instanceof Peon) {
			
			if (this.buffCooldown%500 == 0) {
				((Peon) sender).addTiredness(-5);
			}
			
			if (this.buffCooldown%1000 == 0) {
				((Peon) sender).subtractHealth(-1);
			}

		}
		
	}
	
	public void startBuff(WorldEntity sender) {
		if (sender instanceof Peon) {
			((Peon) sender).addStrength(2);
			((Peon) sender).addSpeed(2);
			((Peon) sender).addTiredness(+500);
			((Peon) sender).subtractHealth(20);
		}
    	
    }
	
	/* remove the effects that shouldn't be permanent */
	public void removeBuff(WorldEntity sender) {
		
		if (sender instanceof Peon) {
			((Peon) sender).addStrength(-2);
			((Peon) sender).addSpeed(-2);
		}
    }
}


