package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;
import java.lang.*;
import org.apache.log4j.Logger;

public class ZombieVirus extends RandomEvent {

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

    public static void execute() {
        //Number for incrementing.
        int x = 0;

        //Number for virus timer
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

                //Infects the yth peon
                if (x == y) {
                    //Notification printed
                    NotificationManager.notify("A peon has been infected with a zombie virus and needs medical attention!");
                    
                    //Damage lasts 120 seconds
                    while (timer < 120) {
                        //5 damage per second
                        ((Peon) entity).subtractHealth(+5);
                        
                        //Spawns a zombie if peon dies as a result of virus
                        if (((Peon) entity).isDead()) {
                            ((Peon) entity).zombified();
                            break;
                        }
                        try {
                            // thread to sleep for 1 second per tick
                            Thread.sleep(1000);
                            } catch (Exception e) {
                                LOGGER.error("ERROR");
                        }
                    }
                }
            }
        }
    }
}


