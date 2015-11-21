package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;

public class PeonEvent extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 3;
    }

    /**
     * Spawns one peon of any of the peon types
     */
    public static void execute() {
        //Random number generated for spawn position
        int x = (int) (Math.random() * 600);

        //Notification printed
        NotificationManager.notify("New random Peon generated.");

        //Second random number to pick peon type generated
        int y = (int) (Math.random() * 4);

        //Switch statement for peon type
        switch (y) {
        case 0:
            minesim.World.getInstance().addEntityToWorld
            (new Peon(x, 100, 32, 32, "Peon"));
            break;
        case 1:
            minesim.World.getInstance().addEntityToWorld
            (new PeonMiner(x, 100, 32, 32, "Miner"));
            break;
        case 2:
            minesim.World.getInstance().addEntityToWorld
            (new PeonGather(x, 100, 32, 32, "Gather"));
            break;
        case 3:
            minesim.World.getInstance().addEntityToWorld
            (new PeonGuard(x, 100, 32, 32, "Guard"));
            break;
        default:
            break;
        }
    }
}
