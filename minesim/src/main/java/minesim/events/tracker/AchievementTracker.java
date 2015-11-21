package minesim.events.tracker;

public class AchievementTracker extends EventTracker{
	
	
	private int freq = 50;
	
	private static boolean AchievementHappened;
	
	public static void achievementHappened(){
		AchievementHappened=true;
	}
	
	@Override
	public void checkForEvent() {
		if(AchievementHappened){
			super.notifyHandler(); 
			AchievementHappened=false;
		}
	}

	@Override
	public String getName() {
		return "Achievement Tracker";
	}

	@Override
	public int getFreq() {
		return freq;
	}

	@Override
	public void setFreq(int value) {
		this.freq = value;
	}
	
}
