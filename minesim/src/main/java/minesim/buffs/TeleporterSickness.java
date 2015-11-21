package minesim.buffs;

import minesim.entities.Peon;
import minesim.entities.WorldEntity;

/**
 * TeleporterSickness extends the Buff class and effects Peons.
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class TeleporterSickness extends Buff {
	
	/**
	 * This set's up the buff for use with a name description and image with a 
	 * cooldown
	 */
	public TeleporterSickness() {
		super();
		setName("TeleporterSickness");
		setDesc("Losing some hp and unhappiness");
		setImageUrl("BuffImages/portal.png");
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
			
			if (this.buffCooldown%300 == 0) {
				((Peon) sender).subtractHealth(2);
				((Peon) sender).addTiredness(30);
			}
			
			if (this.buffCooldown%500 == 0) {
				((Peon) sender).addHappiness(-1);
			}
		}
	}
	
	@Override
	public void startBuff(WorldEntity sender) {
		if (sender instanceof Peon) {
			((Peon) sender).subtractHealth(10);
		}
    	
    }

	@Override
	public void removeBuff(WorldEntity sender) {
		// TODO Auto-generated method stub
		
	}
}
