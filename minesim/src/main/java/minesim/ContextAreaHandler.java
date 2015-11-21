package minesim;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;



/**
 * Created by Michael on 3/09/2015.
 * <p>
 * This class manages the ui element with the fxid 'contextArea'. It's aim is to
 * easily allow multiple people/entites access to a single UI area. Check out
 * the very simple examples in the addRocks and openGame methods of the
 * WindowHandler class. A slightly more complicated example can be found below
 * in the createDefaultContext method.
 */
public class ContextAreaHandler {

    private static final Logger LOGGER =
            Logger.getLogger(ContextAreaHandler.class);

    private static ContextAreaHandler instance = new ContextAreaHandler();

    // The ContextArea loaded in through WindowHandler
    private Pane area;

    // The context shown at start and when nothing else is
    private Pane defaultContext;

    // Storage of the contexts
    protected HashMap<String, Pane> contexts = new HashMap<String, Pane>();

    /**
     * Private and empty constructor
     */
    private ContextAreaHandler() {
    }

    public static ContextAreaHandler getInstance() {
        return instance;
    }

    /**
     * Stores the area used for the Context Area internally
     */
    public void setContextArea(Pane pane) {
        this.area = pane;
        this.defaultContext = createDefaultContext();
        this.area.getChildren().add(defaultContext);
    }

    public void setContextArea(Pane pane, Pane defaultContext) {
        this.area = pane;
        this.defaultContext = defaultContext;
        this.area.getChildren().add(defaultContext);
    }

    /**
     * Returns the default Context
     */
    private Pane createDefaultContext() {
        Pane defaultP = new Pane();

        try {
            defaultP = FXMLLoader.load(getClass().getResource("/layouts/contexts/defaultContext.fxml"));
        } catch (IOException e) {
            LOGGER.error("Couldn't load the default context D:", e);
            return null;
        }

        return defaultP;
    }

    /**
     * Adds a context to the Context area.
     * This function must be run before a context can be displayed in the context area and should use a unique name
     * string.
     *
     * @param name    Should be a unique name used to identify the context
     * @param newPane The area element to be displayed when showing this
     *                context
     * @returns this object for method chaining.
     */
    public ContextAreaHandler addContext(String name, Pane newPane) {
        contexts.put(name, newPane);
        return this;
    }

    /**
     * Adds a context to the Context area based off an fxml layout. This
     * function requires that there be a fxml file of name fxmlFile found in
     * /layouts/contexts and a controller specified for said fxml file found in
     * minesim/contexts.
     *
     * @param name
     * @param fxmlFile
     * @return this object method chaining.
     */
    public ContextAreaHandler addContext(String name, String fxmlFile) {
        Pane newPane = new Pane();
        try {
            newPane = FXMLLoader.load(getClass().getResource("/layouts/contexts/" + fxmlFile));
        } catch (IOException e) {
            LOGGER.error("Couldn't load context fxml file: " + fxmlFile, e);
        } catch (Exception e) {
            LOGGER.error("Couldn't load context fxml file: " + fxmlFile, e);
        }
        contexts.put(name, newPane);
        return this;
    }

    /**
     * Sets the current context of the context area to the context identified by
     * the name String.
     */
    public void setContext(final String name) {
        if (area != null && area.getChildren() != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    area.getChildren().clear();
                    area.getChildren().add(contexts.get(name));
                }
            });
        }
    }

    /**
     * Returns the currently displaying context
     */
    public Pane getCurrentContext() {

        // There should maybe be a check in here that there's only
        // One context in the area?

        return area.getChildren().size() > 0 ? (Pane) area.getChildren().get(0) : null;
    }

    /**
     * Returns the context from the internal hashmap with name 'name'
     */
    public Pane getContext(String name) {
        return contexts.get(name);
    }

    /**
     * Clears the current context and shows the defaultContext
     */
    public void clearContext() {
        if (area != null && area.getChildren() != null) {
            area.getChildren().clear();
            area.getChildren().add(defaultContext);
        }
    }

    /**
     * Removes the context attached to the name string
     */
    public void removeContext(String name) {
        contexts.remove(name);
    }

    /**
     * Allows access to the defaultContext for programmatic editing
     *
     * @return defaultContext
     */
    public Pane getDefaultContext() {
        return defaultContext;
    }

    /**
     * Useful for testing and allows the resetting of the singleton.
     */
    public static void resetInstance() {
        instance = new ContextAreaHandler();
    }
}
