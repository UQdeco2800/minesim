package minesim.events.tracker;

import org.apache.log4j.Logger;

import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

//import java.util.Observable;
//import minesim.WorldTimer;

/**
 * This class acts as the handler for event checking classes. Other entities and
 * classes that are interested in an event can register a method with said
 * event, causing that method to be run when that event is triggered.
 */
public class MineEventHandler implements Observer {
    private static final Logger LOG = Logger.getLogger(MineEventHandler.class);

    int counter = 0;

    private List<EventTracker> trackedEvents = new ArrayList<>();

    private static MineEventHandler instance = new MineEventHandler();

    protected MineEventHandler() {
    }

    public static MineEventHandler getInstance() {
        return instance;
    }

    /**
     * Gets passed an event tracker and a watcher. The tracker is added to the
     * list of currently active event trackers and the watcher is passed on to
     * the tracker to be called later when it triggers.
     */
    public void registerFor(Class eventTracker, Watcher w) throws InvalidClassException {
        if (!EventTracker.class.isAssignableFrom(eventTracker))
            throw new InvalidClassException("First argument must be a class that extends minesim.events.tracker.EventTracker");
        EventTracker tracker = null;
        try {
            tracker = (EventTracker) eventTracker.getConstructor().newInstance();
        } catch (Exception e) {
            LOG.error("Couldn't instantiate the eventTracker", e);
        }

        if (tracker == null)
            throw new InvalidClassException("We couldn't instantiate the EventTracker Class");

        // Add the tracker and add it's watcher, making sure we don't double up
        int index;
        if (!trackedEvents.contains(tracker)) {
            trackedEvents.add(tracker);
            tracker.registerWatcher(w);
        }
        else if ((index = trackedEvents.indexOf(tracker)) > -1)
            trackedEvents.get(index).registerWatcher(w);
    }

    /**
     * This will be called whenever an observed event is triggered It takes the
     * triggered event and runs all the registered functions
     */
    @Override
    public void update(Observable o, Object arg) {
        EventTracker e = (EventTracker) o;
        for (Watcher w : e.getWatcherList()) {
            w.handle(arg);
        }
    }

    /**
     * Looks through the events with registered listeners and checks to see if
     * they should be triggered
     */
    public void checkEvents() {
        counter++;
        for (EventTracker e : trackedEvents) {
            if (counter % e.getFreq() == 0){
                e.checkForEvent();
            }
        }
    }

	public List<EventTracker> getTrackedEvents() {
		return trackedEvents;
	}
}
