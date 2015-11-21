package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;

public class PeonKillerEvent extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 1;
    }

    /**
     * Kills or severly damages all peons in the game
     */
    public static void execute() {
        // Random number generated for health decrease value
        int x = (int) (Math.random() * 50);

        // Notification printed
        NotificationManager.notify("Dropbear attack!");

        // Loop for peon damage
        for (int i = 0; i < 10; i++) {

            // Check for all peons present in the game
            for (WorldEntity entity : minesim.World.getInstance().getWorldentities()) {
                if (entity.getClass().equals(Peon.class)
                        || entity.getClass().equals(PeonGather.class)
                        || entity.getClass().equals(PeonGuard.class)
                        || entity.getClass().equals(PeonMiner.class)) {
                    // All peons are injured or killed
                    ((Peon) entity).subtractHealth(+x);
                }
            }
        }
    }
}
