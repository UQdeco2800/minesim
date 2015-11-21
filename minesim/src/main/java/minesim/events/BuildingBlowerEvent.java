package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;

public class BuildingBlowerEvent extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 1;
    }

    /**
     * Destroys all buildings present in the game
     */
    public static void execute() {
        // Notification printed
        NotificationManager.notify("Earthquake!");

        // Check if any buildings are present in the game
        for (WorldEntity entity : minesim.World.getInstance().getWorldentities()) {
            if (entity.getClass().equals(Building.class)
                    ||entity.getClass().equals(BuildingBar.class)
                    ||entity.getClass().equals(BuildingBlacksmith.class)
                    ||entity.getClass().equals(BuildingGym.class)
                    ||entity.getClass().equals(BuildingHospital.class)
                    ||entity.getClass().equals(BuildingNoodlehaus.class)
                    ||entity.getClass().equals(BuildingTeleporterIn.class)
                    ||entity.getClass().equals(BuildingTeleporterOut.class)) {
                
                // All buildings take heavy damage
                ((Building) entity).subtractHealth(+90);
            }
        }
    }
}
