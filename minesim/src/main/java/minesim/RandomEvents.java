package minesim;

import org.apache.log4j.Logger;

import minesim.events.RandomEvent;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Random Event Generator & Spawner Team Reddy
 */
public class RandomEvents {

    /**
     * Logging
     */
    private static final Logger LOGGER = Logger.getLogger(WindowHandler.class);
    /**
     * Shows if random events are enabled
     */
    private static boolean enabled = false;
    /**
     * The threshold for a random event to occur (out of 1000)
     */
    private static int threshold = 900;
    /**
     * Hold the amount of ticks the game has made
     */
    private static int tickCount = 0;
    /**
     * A collection of events that are possible to be spawned
     */
    private static ArrayList<Class<? extends RandomEvent>> events;
    /**
     * Unchanced array for events for debug
     */
    private static ArrayList<Class<? extends RandomEvent>> debugEvents;

    /**
     * Generates a random number and checks to see if it should spawn a random event
     */
    public static boolean onTick() {
        if (enabled) {
            // Check if we have a list of events
            // We should also only try to spawn a random event every 500 ticks
            if (events != null && ++tickCount % 500 == 0) {
                // Generate random number
                int i = generateRandom();
                // Check if it meets the threshold for event spawning
                if (checkChance(i)) {
                    // Threshold was met, so spawn a random event
                    spawnEvent();
                }
                return true;
            }
        }
        return false;
    }


    /**
     * Toggles random events on or off
     */
    public static void toggleEnabled() {
        enabled = !enabled;
    }

    /**
     * Returns whether or not this event is enabled
     */
    public static boolean isEnabled() {
        return enabled;
    }

    /**
     * Spawns a random event in a new thread
     */
    public static void spawnEvent() {
        // Generate a random index from the collection of available events
        int i = generateRandom(events.size()-1);
        // Get the class of the event
        Class<? extends RandomEvent> c = events.get(i);
        LOGGER.info("Spawning random event " + c.toString());

        // Create new thread for the spawning of the event
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // invoke the execute method on the event
                    c.getMethod("execute").invoke(null);
                } catch (Exception ex) {
                    LOGGER.error(c.toString() + " does not contain an execute() method!");
                }
            }
        }).start(); // start the thread straight away
    }

    /**
     * Generates a random number between 0 and 1000
     */
    public static int generateRandom() {
        return generateRandom(1000);
    }

    /**
     * Generates a random number between 0 and upper
     *
     * @param int upper - the upper limit of the randomly generated number
     */
    public static int generateRandom(int upper) {
        return new Random().nextInt(upper);
    }

    /**
     * Checks if a random number meets the chance threshold, and adjusts the threshold to better
     * manage consecutive success/fails
     *
     * @param int i - The random number (chance)
     */
    public static boolean checkChance(int i) {
        // Check if the chance is greater than the threshold
        if (i > threshold) {
            // Check if the threshold should be adjusted up to decrease the chances of a consecutive pass
            if (threshold < 900) {
                threshold += 5;
            }
            // yes, we met the threshold to spawn a new random event
            return true;
        } else {
            // Check if the threshold should be adjusted down to increase the chances of pass
            if (threshold > 100) {
                threshold -= 5;
            }
            // no, we did not meet the threshold to spawn a new random event
            return false;
        }
    }

    /**
     * Load a collection of random event classes into the random event spawner
     *
     * @param List<Class> randomEvents - The collection of random events
     */
    public static void loadEvents(List<Class<? extends RandomEvent>> randomEvents) {
        // create new arraylist of events
        events = new ArrayList<Class<? extends RandomEvent>>();
        try {
            // loop over each of the supplied random event classes
            for (Class<? extends RandomEvent> c : randomEvents) {
                // get the chance of this random event
                int chance = (int)c.getMethod("chance").invoke(null);
                // add the random event to the array of available events the amounts of times it has chance
                // (ie, the more chance it has, the more times it will be loaded into the array)
                for (int i=0; i<chance; i++) {
                    events.add(c);
                }
            }
        } catch (Exception ex) {
            // shouldnt ever get here, but strange things happen (when you're going round the twist!)
            LOGGER.info(ex.getMessage());
            return;
        }
    }

    /**
     * Get's an event at the specified index
     * @param i
     * @return
     */
    public static Class<? extends RandomEvent> getEvent(int i) {
        try {
            return events.get(i);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Gets the total chance (size) of the loaded events
     * @return
     */
    public static int getTotalChance() {
        return events.size();
    }


    /**
     * Load a collection of random event classes into the random event spawner (debug)
     * @param randomEvents
     */
    public static void loadDebugEvents(List<Class<? extends RandomEvent>> randomEvents) {
        // create new arraylist of events
        debugEvents = new ArrayList<Class<? extends RandomEvent>>();
        try {
            // loop over each of the supplied random event classes and add it to the array
            for (Class<? extends RandomEvent> c : randomEvents) {
                debugEvents.add(c);
            }
        } catch (Exception ex) {
            // shouldnt ever get here, but strange things happen (when you're going round the twist!)
            LOGGER.info(ex.getMessage());
            return;
        }
    }

    /**
     * Executes an event (Debug) at the specified index
     * @param i
     */
    public static Class<? extends RandomEvent> executeDebugEvent(int i) {
        // check to make sure the event exists
        try {
            debugEvents.get(i);
        }
        catch (Exception ex) {
            LOGGER.info(ex.getMessage());
            return null;
        }

        // get the event at the specified index
        Class<? extends RandomEvent> c = debugEvents.get(i);

        LOGGER.info("Spawning random event " + c.toString());

        // Create new thread for the spawning of the event
        new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    // invoke the execute method on the event
                    c.getMethod("execute").invoke(null);
                } catch (Exception ex) {
                    LOGGER.info(c.toString() + " does not contain an execute() method!");
                }
            }
        }).start(); // start the thread straight away

        return c;
    }

    /**
     * Gets the size of the debug events array
     * @return
     */
    public static int getDebugEventsSize() {
        if (debugEvents != null) {
            return debugEvents.size();
        } else {
            return 0;
        }
    }
}
