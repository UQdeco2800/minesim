package minesim.events.tracker;

public class PeonDigTracker extends EventTracker{
    
    //A boolean that dictates whether a peon is currently digging
	private static boolean dig = false;
	//Frequency of update
	private int freq = 1;
	//Time spent not digging
	private int notDigCount = 0;
	
	/**
	 * Sets dig to true
	 */
	public static void setDig(){
		dig = true;
	}
	/**
	 * Sets dig to false
	 */
	public static void digDone(){
		dig = false;
	}
	
	/**
	 * Checks if a peon has dug a new tile
	 */
	@Override
	public void checkForEvent() {
		if(dig && notDigCount>112){
			super.notifyHandler(); 
			dig = false;
			notDigCount = 0;
		}
		else{
			notDigCount++;
		}
	}
	
	/**
     * Gets the name of the tracker
     * @return the name of the tracker object as a string
     */
	@Override
	public String getName() {
		return "Peon Dig Tracker";
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
