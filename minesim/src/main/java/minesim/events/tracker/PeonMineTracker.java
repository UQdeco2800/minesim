package minesim.events.tracker;

public class PeonMineTracker extends EventTracker{
    
    //The boolean that represents whether or not the peon has mined
	private static boolean mine = false;
	//The frequency that this tracker should be checked
	private int freq = 1;
	//The time the peon has spent not mining
	private int notMineCount = 0;
	
	/**
	 * Sets the mine boolean to true
	 */
	public static void setMine(){
		mine = true;
	}
	/**
	 * Sets the mine boolean to false
	 */
	public static void mineDone(){
		mine = false;
	}
	
	/**
	 * Checks if a peon has recently mined
	 */
	@Override
	public void checkForEvent() {
		if(mine && notMineCount > 112){
			super.notifyHandler(); 
			mine = false;
			notMineCount = 0;
		}
		else{
			notMineCount++;
		}
	}

	/**
     * Gets the name of the tracker
     * @return the name of the tracker object as a string
     */
	@Override
	public String getName() {
		return "Peon Mine Tracker";
	}
	
	/**
     * Getter method for the update frequency of the tracker
     * @return freq returns the int freq
     */
	@Override
	public int getFreq() {
		return freq;
	}

	/**
     * Setter method for the update frequency of the tracker
     * @param value, an int which the variable freq is to be set to 
     */
	@Override
	public void setFreq(int value) {
		this.freq = value;
	}

}
