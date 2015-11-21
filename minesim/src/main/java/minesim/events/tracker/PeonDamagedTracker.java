package minesim.events.tracker;

public class PeonDamagedTracker extends EventTracker {
    
    //Whether the peon has been damaged at all
    static boolean damaged;
    //Whether the been has been recently damaged
    static boolean recDamaged = false;
    //The update frequency
	public static int freq = 1;
	//The time the peon has spent undamaged
	int undamagedCount = 0;
	
	/**
	 * Notifies its observers if the peon was damaged in the last 400 
	 * ticks of the world 
	 */
    @Override
    public void checkForEvent() {
    	if(damaged && (undamagedCount > 400)){
    		//System.out.println("undamagedCount: " + undamagedCount);
    		super.notifyHandler();
    		damaged = false;
    		//recDamaged = true;
    		//freq = 300;
    		undamagedCount = 0;
    	}
    	
    	else {
    		undamagedCount++;
    	}
    }
    
    /**
     * Gets the name of the tracker
     * @return the name of the tracker object as a string
     */
	@Override
	public String getName() {
		return "Peon Damaged Tracker";
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
		freq = value;
	}
	
	
	/**
	 * Set the boolean "recDamaged" to true. 
	 * Boolean represents whether or not the peon was "recently damaged"
	 */
	public static void setDamaged() {
		recDamaged = true;
	}
	
	   /**
     * Sets the "damaged" boolean to be equal to true
     */
    public static void peonDamaged(){
        damaged = true;
    }

}