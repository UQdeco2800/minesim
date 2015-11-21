package minesim.events;

import notifications.NotificationManager;
import minesim.entities.*;
import minesim.entities.items.Elevator;
import minesim.entities.items.Ladder;

public class LadderUpgrade extends RandomEvent {

    /**
     * Determines the chance of spawning the event
     */
    public static int chance() {
        return 1;
    }

    /**
     * Removes a ladder in the game and spawns an elevator in its place
     */
    public static void execute() {
        // Notification printed
        NotificationManager.notify("Your ladders have turned into elevators! Maybe that will help you -lift- your performance?");

        //For each entity currently present in the world, check if it is a ladder
        for (WorldEntity entity : minesim.World.getInstance().getWorldentities()) {
            if (entity.getClass().equals(Ladder.class)) {
                //Store the x and y position for use in constructing the elevator in the same spot
                int prevX;
                int prevY;
                prevX = entity.getXpos();
                prevY = entity.getYpos();
                //Remove the current ladder
                minesim.World.getInstance().removeEntityFromWorld(entity);
                //Replace it with an elevator
                minesim.World.getInstance().addEntityToWorld(new Elevator(prevX, prevY, 32, 32, "elevator", 0));
            }
        }
    }
}