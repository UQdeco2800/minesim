package minesim.events.tracker;

import minesim.entities.Peon;

public class PeonDeathTracker extends EventTracker {
    
    //Frequency of checks
    public int freq = 1;
    //A counter of total peon deaths
    public int totalDeaths = 0;
    //Death boolean set to true when a peon dies
    static boolean death;
	
	/**
	 * Notifies its observers when a peon dies
	 */
    @Override
    public void checkForEvent() {
    	if(death){
    		super.notifyHandler();
    		death = false;
    		totalDeaths++;
    	}
    }
    /**
     * Returns a string that is the name of the tracker object
     * @return The name of the tracker object
     */
	@Override
	public String getName() {
		return "Peon Death Tracker";
	}
	/**
	 * Get the frequency
	 * @return int the frequency of the tracker
	 */
	@Override
	public int getFreq() {
		return freq;
	}
	
	/**
	 * Set the frequency
	 * @param Takes the int that the frequency is to be set to
	 */
	@Override
	public void setFreq(int value) {
		freq = value;
	}
	
	/**
	 * Set the peon death variable to true 
	 */
	static public void peonDied(){
        death = true;
    }

}