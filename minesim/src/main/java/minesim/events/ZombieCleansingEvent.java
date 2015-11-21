package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;

public class ZombieCleansingEvent extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 1;
    }

    /**
     * Destroys all zombies present in the game
     */
    public static void execute() {
        // Notification printed
        NotificationManager.notify("The Walking Dead have been cleansed.");

        // Check if any zombies are present in the game
        for (WorldEntity entity : minesim.World.getInstance().getWorldentities()) {

            if (entity.getClass().equals(ZombieMob.class)) {

                // All zombies are removed from game apart from their graves
                minesim.World.getInstance().removeEntityFromWorld(entity);
            }
        }
    }
}
