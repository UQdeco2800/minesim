package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;
import minesim.entities.items.Ladder;
import minesim.entities.items.Rope;

public class RopeUpgrade extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 1;
    }

    /**
     * Removes a rope in the game and spawns an ladder in its place
     */
    public static void execute() {
        // Notification printed
        NotificationManager.notify("Your ropes have turned into ladders! Maybe that will give you a -step- up?");

        // Check if any ropes are present in the game
        for (WorldEntity entity : minesim.World.getInstance().getWorldentities()) {
            if (entity.getClass().equals(Rope.class)) {
                int prevX;
                int prevY;
                prevX = entity.getXpos();
                prevY = entity.getYpos();
                minesim.World.getInstance().removeEntityFromWorld(entity);
                minesim.World.getInstance().addEntityToWorld(new Ladder(prevX, prevY, 32, 32, "ladder", 0));
            }
        }
    }
}