package minesim.buffs;

import minesim.entities.Peon;
import minesim.entities.WorldEntity;
import minesim.entities.disease.Flu;
import minesim.entities.disease.Illness;

/**
 * Disease extends the Buff class and effects Peons.
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class Infection extends Buff {
	
	/**
	 * This set's up the buff for use with a name description and image with a 
	 * cooldown
	 */

	private int speedBefore;
	public Infection() {
		super();
		setName("Flu");
		setDesc("Feels sick after fighting a zombie");
		setImageUrl("BuffImages/disease.png");
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
		speedBefore = ((Peon) sender).getSpeed();
		if (sender instanceof Peon) {

			if (this.buffCooldown%300 == 0) {
				((Peon) sender).subtractHealth(2);

				((Peon) sender).addSpeed(((Peon) sender).getSpeed() / -3);
				if (((Peon) sender).getHealth()==0) {
					((Peon) sender).zombified();
				}
			}

			if (this.buffCooldown%600 == 0) {
				((Peon) sender).subtractHealth(2);
				if (((Peon) sender).getHealth()==0) {
					((Peon) sender).zombified();
				}
			}
		}
	}

	@Override
	public void startBuff(WorldEntity sender) {
		((Peon) sender).addSpeed(((Peon) sender).getSpeed()/-4);

	}

	@Override
	public void removeBuff(WorldEntity sender) {

		((Peon) sender).setSpeed(speedBefore);
		// TODO Auto-generated method stub

	}

}
