package minesim.events.tracker;

/**
 * Meant to be anonymously created and passed to MineEventHandler
 * Created by Michael on 14/10/2015.
 */
public interface Watcher {
    public void handle(Object arg);
}
