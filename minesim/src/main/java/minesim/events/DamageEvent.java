package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;

public class DamageEvent extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 1;
    }

    /**
     * Gives health deduction to a randomly selected peon
     */
    public static void execute() {
        //Number for incrementing.
        int x = 0;

        /*Random number for peon selection.
        Has the potential of doing nothing if you dont have enough peons.*/
        int y = (int) (Math.random() * 10);

        //Random number for health deduction
        int z = (int) (Math.random() * 50);

        for (WorldEntity entity : minesim.World.getInstance().getWorldentities()) {
            //Checks for peons and increments x
            if (entity.getClass().equals(Peon.class)
                    ||entity.getClass().equals(PeonMiner.class)
                    ||entity.getClass().equals(PeonGuard.class)
                    ||entity.getClass().equals(PeonGather.class)) {
                x++;
                
                //Damages y'th peon
                if (x == y) {
                    //Notification printed
                    NotificationManager.notify("A peon slip and fell.");
                    //Health deducted
                    ((Peon) entity).subtractHealth(+z);
                }
            }
        }
    }
}
