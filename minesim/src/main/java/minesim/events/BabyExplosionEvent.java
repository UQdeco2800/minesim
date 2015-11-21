package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;

public class BabyExplosionEvent extends RandomEvent {

    /**
     * Chance of spawning event
     */
    public static int chance() {
        return 1;
    }
    
    /**
     * Adds a group of peons of different types
     */
    public static void execute() {
        //Random number generated for spawn position
        int x = (int) (Math.random() * 600);

        //Notification printed
        NotificationManager.notify("Reinforcements have arrived.");

        //A set of peons is spawned
        minesim.World.getInstance().addEntityToWorld(new Peon(x, 0, 32, 32, "Peon"));
        minesim.World.getInstance().addEntityToWorld(new PeonMiner(x + 50, 100, 32, 32, "Miner"));
        minesim.World.getInstance().addEntityToWorld(new PeonGather(x + 100, 100, 32, 32, "Gather"));
        minesim.World.getInstance().addEntityToWorld(new PeonGuard(x + 150, 100, 32, 32, "Guard"));
    }
}
