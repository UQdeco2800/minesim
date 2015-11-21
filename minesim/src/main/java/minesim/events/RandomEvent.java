package minesim.events;

import org.apache.log4j.Logger;

/**
 * Base class of all random events. It should be extended and both methods overridden.
 */
public class RandomEvent {

    protected static final Logger LOGGER = Logger.getLogger(minesim.WindowHandler.class); 
    
    /**
     * OVERRIDE THIS to return the chance of the event spawning
     * Note: the chances of all random events are relative to each other
     */
    public static int chance() {
        return -1;
    }

    /**
     * OVERRIDE THIS to define what will happen when the random event is spawned
     */
    public static void execute() {
        LOGGER.info("You need to override the execute() method for this RandomEvent");
    }
}
