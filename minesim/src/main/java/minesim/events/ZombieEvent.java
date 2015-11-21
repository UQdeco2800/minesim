package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;

public class ZombieEvent extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 2;
    }

    /**
     * Spawns one zombie
     */
    public static void execute() {
        //Random number generated for spawn position
        int x = (int) (Math.random() * 1500);

        //Notification printed
        NotificationManager.notify("Oh no it's a zombie!");

        //Spawns zombie at random location
        minesim.World.getInstance().addEntityToWorld(new ZombieMob(x, 100, 80, 42, 42));
    }
}
