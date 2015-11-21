package minesim.tasks;

import java.util.Optional;

import minesim.entities.Mob;
import minesim.entities.Peon;

enum MobActionState {
    MOB_IDLE,
    MOB_HOSTILE
}

public class AttackMob extends Task {
    Mob actionableMob;
    MobActionState state = MobActionState.MOB_IDLE;

    public AttackMob(Peon parent, Mob mob) {
        super(parent, 0, 0);
        this.actionableMob = mob;
    }

    @Override
    public void doTask() {
        actionableMob.activeFlag = Boolean.TRUE;
        super.doTask();
        if (state == MobActionState.MOB_IDLE) {
            /*This occurs when the peon has actioned to kill the mob */
            if (!getPeon().moveTowards(this.actionableMob.getXpos(), this.actionableMob.getYpos(), 10)) {
                this.state = MobActionState.MOB_HOSTILE;
                getPeon().standStill();
            }
        } else if (state == MobActionState.MOB_HOSTILE) {	/*Attack the peon*/
            if (!actionableMob.interact(getPeon())) {
                /*Implement an attacking animation*/
            } else {
                onCompletion();
                getPeon().updateTask(Optional.<Task>empty());
        		/*This whill occur when the mob is dead*/
            }
        }
    }

    @Override
    public void switchActiveFlag() {
        actionableMob.activeFlag = !actionableMob.activeFlag;
    }
}
