package minesim.events.tracker;

import minesim.World;

/**
 * A tracker object that triggers on the transition from day to night 
 */
public class DayNightTransistionTracker extends EventTracker {
    //Retrieving the instance of the world 
    World mainworld = World.getInstance();
    //The current day 0 indicating night, 1 indicating day
    Integer currentDay;
    //The previous day
    Integer oldDay = 1;
    //The frequency at which MineEventHandler checks
	public int freq = 1;

	/**
	 * Checks if day has turned to night or vice versa
	 */
    @Override
    public void checkForEvent() {
        //IF DAY HAS CHANGED
		currentDay = mainworld.getWorldTimer().isDay();
        if (currentDay != oldDay) {
			oldDay = currentDay;
			if (oldDay.equals(1)) {
				//System.out.println("daytonight");
				super.notifyHandler(states.NIGHTTODAY);
			} else {
				//System.out.println("nighttoday");
				super.notifyHandler(states.DAYTONIGHT);
			}
        }
    }

    /**
     * The two day and night states
     */
    public enum states {
        DAYTONIGHT, NIGHTTODAY
    }

    /**
     * Gets the name of the tracker
     * @return the name of the tracker object as a string
     */
	@Override
	public String getName() {
			
		return "day/night transition tracker";
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
    
}
