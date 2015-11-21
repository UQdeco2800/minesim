package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;

public class BoostEvent extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 5;
    }

    /**
     * Recovers the stamina of a randomly selected peon
     */
    public static void execute() {
        //Number for incrementing.
        int x = 0;

        /*Random number for peon selection.
        Has the potential of doing nothing if you dont have enough peons.*/
        int y = (int) (Math.random() * 10);

        for (WorldEntity entity : minesim.World.getInstance().getWorldentities()) {
            //Checks for peons and increments x
            if (entity.getClass().equals(Peon.class)) {
                x++;

                //Boosts stats of the y'th peon
                if (x == y) {
                    //Notification printed
                    NotificationManager.notify("A peon found some Mountain Dew.");
                    //Stamina recovered
                    ((Peon) entity).setTiredness(0);
                }
            }
        }
    }
}
