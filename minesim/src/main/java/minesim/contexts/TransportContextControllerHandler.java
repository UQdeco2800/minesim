package minesim.contexts;

import minesim.ContextAreaHandler;
import minesim.entities.Peon;
import minesim.entities.WorldEntity;
import minesim.WorldTimer;

/**
 * The class for setting the context pane to be transportation generation.
 * transportContextController adds the gui elements to the screen, while
 * this class simply creates the instance of the context pane, and calls 
 * the method in transportContextController.
 */
public class TransportContextControllerHandler {
    //The controller where information is conveyed and built
    private transportContextController controller;
    //The current instance of the context pane
    private static TransportContextControllerHandler instance;

    //Boolean for whether an instance of the pane has been made yet
    private static boolean transportContextMade;

    /**
     * Returns the instance of the context pane's handler, creating a new
     * instance if the transportation pane had not been created yet.
     * @return the current instance of the transportation status context pane
     */
    public static TransportContextControllerHandler getInstance(){
        if(instance == null) {
            instance = new TransportContextControllerHandler(
                    new transportContextController());
        }
        return instance;
    }

    public TransportContextControllerHandler(
                                transportContextController tCC) {
        controller = tCC;
        instance = this;
    }
    
    /**
     * Checks if the context pane has been assigned already, and if not
     * sets the pane to show transportation buttons, which is set
     * by the controller.
     */
    public void showTransportStatus() {
        if (transportContextMade != true) {
            ContextAreaHandler.getInstance().setContext("transportContext");
            transportContextMade = Boolean.FALSE;
        } else {
            ContextAreaHandler.getInstance().setContext("transportContext");
        }
        controller.constructTransportMenu();
    }

    /**
     * Removes the transportation pane from the context area.
     */
    public void removeTransportStatusPane() {
        ContextAreaHandler.getInstance().clearContext();
    }
}