package minesim.contexts;

import minesim.ContextAreaHandler;
import minesim.entities.Peon;
import minesim.entities.WorldEntity;

/**
 * Created by Michael on 15/10/15. The class for setting the context pane to be
 * the status pane for a peon. peonStatusContextController gives it all the data
 * and information, while this class simply creates the instance of the context
 * pane, which changes the data displayed for each peon picked, and each
 * instance of the peon's info is displayed whenever the peon is clicked on.
 * This is basically the main class for the peon context, which calls on
 * ContextController to handle the data.
 *
 * If the peon status gui were modelled as an MVC, this would be the Controller.
 */
public class PeonStatusContextControllerHandler {
	// The controller where information is conveyed and built
	private peonStatusContextController controller;
	// Boolean for whether an instance of the pane has been made yet
	private static boolean peonContextMade;
	// The current instance of the context pane
	private static PeonStatusContextControllerHandler instance;

	/**
	 * Returns the instance of the context pane's handler, creating a new
	 * instance if the peon status pane had not been created yet.
	 * 
	 * @return the current instance of a peon's status context pane
	 * @author JamesG
	 */
	public static PeonStatusContextControllerHandler getInstance() {
		if (instance == null) {
			instance = new PeonStatusContextControllerHandler(new peonStatusContextController());
		}
		return instance;
	}

	public PeonStatusContextControllerHandler(peonStatusContextController pSCC) {
		controller = pSCC;
		instance = this;
	}

	/**
	 * Checks if the context pane has been assigned already, and if not sets the
	 * pane to convey the peon's information, which is set by the controller.
	 * 
	 * @param peon
	 * @author JamesG
	 */
	public void showPeonStatus(WorldEntity peon) {
		if (peonContextMade != true) {
			ContextAreaHandler.getInstance().setContext("peonStatus");
			peonContextMade = Boolean.FALSE;
		}
		controller.setPeon((Peon) peon);
	}

	/**
	 * Removes the peon status pane from the context area.
	 * 
	 * @author JamesG
	 */
	public void removePeonStatusPane() {
		ContextAreaHandler.getInstance().clearContext();
	}
}
