package minesim.tasks;

import minesim.entities.Peon;
import minesim.entities.PeonMiner;

public class Task {
    private Peon parent;
    private PeonMiner parentMiner;
    private int xpos, ypos;

    /**
     * Construct variables for the Task
     *
     * @param parent   The peon completing the task
     * @param destxpos The peon's destination x position
     * @param destypos The peon's destination y position
     */
    public Task(Peon parent, int destxpos, int destypos) {
        this.parent = parent;
        this.xpos = destxpos;
        this.ypos = destypos;
    }

    /**
     * As this is a 2D game we only have the x coordinate ie the scrolling sideways coordinate
     */
    public void doTask() {
        // Do nothing
    }

    /**
     * Gets the parent peon
     *
     * @return parent
     */
    public Peon getPeon() {
        return parent;
    }

    /**
     * Sets the parent peon
     */
    public void setPeon(Peon newPeon) {
        this.parent = newPeon;
    }

    /**
     * Gets the parent peon miner
     *
     * @return parentMiner
     */
    public PeonMiner getPeonMiner() {
        return parentMiner;
    }

    /**
     * Gets the peon's x position destination
     *
     * @return xpos
     */
    public int getDest() {
        return xpos;
    }

    /**
     * Gets the peon's y position destination
     *
     * @return ypos
     */
    public int getDestYPos() {
        return ypos;
    }

    public void onCompletion() {
        System.out.println(this);
        System.out.println(getPeon().lookNextTask());
        getPeon().updateTask(getPeon().getNextTask());
        if (getPeon().getCurrentTask().isPresent()) {
        	getPeon().getCurrentTask().get().switchActiveFlag();
        }
    }

    //this is here so it can be generically called whenever a task changes, methods that use it overwrite this
    public void switchActiveFlag() {
        //do nothing in the general case
    }
}