package minesim.buffs;

import minesim.entities.Peon;
import minesim.entities.WorldEntity;
import minesim.entities.disease.Flu;
import minesim.entities.disease.Illness;

/**
 * Disease extends the Buff class and effects Peons.
 *
 * Created by Andres on 25/10/2015.
 */
public class Hospitalization extends Buff {

    /**
     * This set's up the buff for use with a name description and image with a
     * cooldown
     */

    private int speedBefore;
    public Hospitalization() {
        super();
        setName("St. Mary Hospital");
        setDesc("Health Sickness");
        setImageUrl("BuffImages/redCross.png");
        cooldownmax = 2;
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

            if (this.buffCooldown%1 == 0) {
                ((Peon) sender).subtractHealth(-40);
            }

            if (this.buffCooldown%2 == 0) {
                ((Peon) sender).subtractHealth(-40);
                removeBuff(sender);
            }
        }
    }

    @Override
    public void startBuff(WorldEntity sender) {
        ((Peon) sender).addSpeed(1);

    }

    @Override
    public void removeBuff(WorldEntity sender) {

        ((Peon) sender).addSpeed(-1);
        // TODO Auto-generated method stub

    }

}
