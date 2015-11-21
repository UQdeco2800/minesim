package minesim.menu;

/**
 * Represents a button or item on the internal escape menu. Created by Michael
 * on 27/09/2015.
 */
public interface eMenuItem {
    /**
     * The name of the button this Menu Item represents
     * @return
     */
    public String getName();

    /**
     * This is to Override and contains what will be called when you click the
     * button.
     */
    public void handle();
}

