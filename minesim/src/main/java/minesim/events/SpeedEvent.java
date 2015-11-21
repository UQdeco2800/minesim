package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;
import org.apache.log4j.Logger;


public class SpeedEvent extends RandomEvent {

    /**
     * Logger for exceptions
     */
    protected static final Logger LOGGER = Logger.getLogger(minesim.WindowHandler.class); 
    
    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 1;
    }

    /**
     * Gives a speed boost to a randomly selected peon
     */
    public static void execute() {
        //Number for incrementing
        int x = 0;

        //Number for speed deduction timer
        int timer = 0;

        /*Random number for peon selection.
        Has the potential of doing nothing if you dont have enough peons.*/
        int y = (int) (Math.random() * 10);

        for (WorldEntity entity : minesim.World.getInstance().getWorldentities()) {
            //Checks for peons and increments x
            if (entity.getClass().equals(Peon.class)
                    || entity.getClass().equals(PeonGather.class)
                    || entity.getClass().equals(PeonGuard.class)
                    || entity.getClass().equals(PeonMiner.class)) {
                x++;

                //Increases speed for y'th peon
                if (x == y) {
                    //Notification printed
                    NotificationManager.notify("Gotta go fast!");
                    //Speed Increased
                    ((Peon)  entity).addSpeed(+10);
                    while (timer < 9) {
                    	// Decrease speed of peon by 1
                        ((Peon) entity).addSpeed(-1);
                        try {
                            // thread to sleep for 3000 milliseconds
                            Thread.sleep(3000);
                            } catch (Exception e) {
                                LOGGER.error("ERROR");
                        }
                    }
                }
            }
        }
    }
}
