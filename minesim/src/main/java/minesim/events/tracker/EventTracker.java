package minesim.events.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

/**
 * This is the class that people need to extend Created by Michael on
 * 14/10/2015.
 */
public abstract class EventTracker extends Observable {
    
    List<Watcher> watcherList = new ArrayList<>();
    
    public int freq = 1;
    
    public String name;

    /**
     * This
     */
    public EventTracker() {
        super();
        this.addObserver(MineEventHandler.getInstance());
        //System.out.println("WE ARE COUNT THIS MANU OBSERVERS: " + this.countObservers());
    }

    /**
     * Must be extended. This is the function run to check if the event has
     * occurred. If the event has occured then this must run notify Observer
     */
    public abstract void checkForEvent();
    
    /**
     * Must be extended, gives an identifier to each tracker object
     * @return
     */
    public abstract String getName();
    
    /**
     * Must be extended, returns the frequency of a tracker object
     * @return
     */
    public abstract int getFreq();
    /**
     * Must be extended, sets the frequency of a tracker object
     * @param value
     */
    public abstract void setFreq(int value);

    /**
     * Alerts the handler to this event being triggered, calling
     * MineEventHandler's .update() method with this Observable and the object
     * arg
     */
    public void notifyHandler(Object arg) {
        this.setChanged();
        super.notifyObservers(arg);
    }

    /**
     * Calls MineEventHandler's .update method() with this observable and null.
     */
    public void notifyHandler() {
        this.setChanged();
        super.notifyObservers();
    }

    /**
     * Should only be used by the MineEventHandler to register a watcher to this
     * event
     */
    protected void registerWatcher(Watcher w) {
        if (watcherList.contains(w)) return;
        watcherList.add(w);
    }

    /**
     * Should only be called by MineEventHandler to return the list of watchers
     * for this event
     */
    protected List<Watcher> getWatcherList() {
        return new ArrayList<>(watcherList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTracker that = (EventTracker) o;
        return Objects.equals(watcherList, that.watcherList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(watcherList);
    }


}
