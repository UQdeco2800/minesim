package minesim.buffs;

import minesim.entities.Peon;
import minesim.entities.WorldEntity;

/**
 * Luck extends the Buff class and effects Peons.
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class Luck extends Buff {
	
	/**
	 * This set's up the buff for use with a name description and image with a 
	 * cooldown
	 */
	public Luck() {
		super();
		setName("Luck of the Irish");
		setDesc("Felling lucky punk,");
		setImageUrl("BuffImages/luck.png");
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
				((Peon) sender).addAnnoyance(-1);
				((Peon) sender).addHappiness(1);
			}
			
		}

	}
	
	@Override
	public void startBuff(WorldEntity sender) {
		if (sender instanceof Peon) {
			((Peon) sender).addLuck(2);
		}
    	
    }
	
	@Override
	public void removeBuff(WorldEntity sender) {
		
		if (sender instanceof Peon) {
			((Peon) sender).addLuck(-2);
		}
    }
}


