package minesim.buffs;

import minesim.entities.Peon;
import minesim.entities.WorldEntity;

/**
 * Noodles extends the Buff class and effects Peons.
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class Noodles extends Buff {
	
	/**
	 * This set's up the buff for use with a name description and image with a 
	 * cooldown
	 */
	public Noodles() {
		super();
		setName("Well Fed");
		setDesc("Health");
		setImageUrl("BuffImages/noodles.png");
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
	
	@Override
	public void startBuff(WorldEntity sender) {
		if (sender instanceof Peon) {
			((Peon) sender).addStrength(1);
			((Peon) sender).addTiredness(+300);
			((Peon) sender).subtractHealth(-10);
		}
    	
    }
	
	@Override
	public void removeBuff(WorldEntity sender) {
		
		if (sender instanceof Peon) {
			((Peon) sender).addStrength(-1);

		}
    }
}


