package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;

public class ZombieMobAttack extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 1;
    }

    /**
     * Spawns multiple zombies
     */
    public static void execute() {
        //Notification printed
        NotificationManager.notify("TOO MANY ZOMBIES!");

        //Loop for zombie spawns
        for (int i = 0; i < 4; i++) {

            //Number generated for position
            int x = (int) (Math.random() * 2500);

            //Zombies spawn at random positions
            minesim.World.getInstance().addEntityToWorld(new ZombieMob(x, 100, 40, 42, 42));
        }
    }
}
