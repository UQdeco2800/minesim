package minesim.contexts;

import minesim.ContextAreaHandler;
import minesim.entities.Peon;
import minesim.entities.WorldEntity;

/**
 * Created by Michael on 15/10/15.
 * The class for setting the context pane to be the status pane for a peon.
 * peonStatusContextController gives it all the data and information, while
 * this class simply creates the instance of the context pane, which changes
 * the data displayed for each peon picked, and each instance of the peon's
 * info is displayed whenever the peon is clicked on. This is basically the
 * main class for the peon context, which calls on ContextController to handle
 * the data.
 *
 * If the peon status gui were modelled as an MVC, this would be the Controller.
 */
public class JobsContextControllerHandler {
    //The controller where information is conveyed and built
    private jobsContextController controller;
    //Boolean for whether an instance of the pane has been made yet
    private static boolean jobsContextMade;
    //The current instance of the context pane
    private static JobsContextControllerHandler instance;


    /**
     * Returns the instance of the context pane's handler, creating a new
     * instance if the jobs status pane had not been created yet.
     * @return the current instance of a peon's status context pane
     * @author Abicith
     */
    public static JobsContextControllerHandler getInstance(){
        if(instance == null) {
            instance = new JobsContextControllerHandler(
                    new jobsContextController());
        }
        return instance;
    }

    public JobsContextControllerHandler(jobsContextController jCC) {
        controller = jCC;
        instance = this;
    }

    /**
     * Checks if the context pane has been assigned already, and if not
     * sets the pane to convey the peon's information, which is set
     * by the controller.
     * @param peon
     * @author JamesG
     */
    public void showJobsStatus() {
        if (jobsContextMade != true) {
            ContextAreaHandler.getInstance().setContext("jobsContext");
            jobsContextMade = Boolean.FALSE;
        } else {
            ContextAreaHandler.getInstance().setContext("jobsContext");
        }
        controller.setTasks();
    }

    /**
     * Removes the peon status pane from the context area.
     * @author JamesG
     */
    public void removePeonStatusPane() {
        ContextAreaHandler.getInstance().clearContext();
    }
}
