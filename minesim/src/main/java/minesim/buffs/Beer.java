package minesim.buffs;

import minesim.entities.Peon;
import minesim.entities.WorldEntity;

/**
 * Beer extends the Buff class and effects Peons.
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class Beer extends Buff {

	/**
	 * This set's up the buff for use with a name description and image with a 
	 * cooldown
	 */
	public Beer() {
		super();
		setName("Boozed up");
		setDesc("I can't remember how much I have had");
		setImageUrl("BuffImages/beer.png");
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
				((Peon) sender).addTiredness(15);
				((Peon) sender).addAnnoyance(-2);
				((Peon) sender).addHappiness(2);
			}
			
		}
		
	}
	
	@Override
	public void startBuff(WorldEntity sender) {
		if (sender instanceof Peon) {
			((Peon) sender).subtractHealth(30);
		}
    	
    }

	@Override
	public void removeBuff(WorldEntity sender) {
		
	}
	
}


