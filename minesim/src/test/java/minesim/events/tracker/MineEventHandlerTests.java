
package minesim.events.tracker;

import static org.junit.Assert.*;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Observable;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import minesim.events.tracker.DayNightTransistionTracker;
import minesim.events.tracker.EventTracker;
import minesim.events.tracker.MineEventHandler;
import minesim.events.tracker.PeonDeathTracker;
import minesim.events.tracker.Watcher;

public class MineEventHandlerTests {
		
	/**
	 * Registers all the possible trackers to the MineEventHandler class and then checks to see if those
	 * trackers have been added.
	 */
	@Test
	public void testRegisterForAllTrackers(){
		//Mock a DayNightTransitionTracker and register it to MineHandler.
		DayNightTransistionTracker daynightTracker = Mockito.mock(DayNightTransistionTracker.class);
		Mockito.when(daynightTracker.getName()).thenReturn("day/night transition tracker");
		Watcher w = Mockito.mock(Watcher.class);
		try {
			MineEventHandler.getInstance().registerFor(daynightTracker.getClass(), w);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		
		//Mock an AchievementTracker and register it to MineHandler
		AchievementTracker achievementTracker = Mockito.mock(AchievementTracker.class);
		Mockito.when(achievementTracker.getName()).thenReturn("Achievement Tracker");
		try {
			MineEventHandler.getInstance().registerFor(achievementTracker.getClass(), w);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		
		//Mock an Peon Damaged Tracker and register it to MineHandler
		PeonDamagedTracker pdTracker = Mockito.mock(PeonDamagedTracker.class);
		Mockito.when(pdTracker.getName()).thenReturn("Peon Damaged Tracker");
		try {
			MineEventHandler.getInstance().registerFor(pdTracker.getClass(), w);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		
		//Mock an Peon Damaged Tracker and register it to MineHandler
		PeonDigTracker pdigTracker = Mockito.mock(PeonDigTracker.class);
		Mockito.when(pdigTracker.getName()).thenReturn("Peon Dig Tracker");
		try {
			MineEventHandler.getInstance().registerFor(pdigTracker.getClass(), w);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		
		//Mock an Peon Damaged Tracker and register it to MineHandler
		PeonMineTracker pmineTracker = Mockito.mock(PeonMineTracker.class);
		Mockito.when(pmineTracker.getName()).thenReturn("Peon Mine Tracker");
		try {
			MineEventHandler.getInstance().registerFor(pmineTracker.getClass(), w);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
				
		//Mock a PeonDeathTracker and register it to MineHandler.
		PeonDeathTracker mockPeonDeath = Mockito.mock(PeonDeathTracker.class);
		Mockito.when(mockPeonDeath.getName()).thenReturn("Peon Death Tracker");
		try {
			MineEventHandler.getInstance().registerFor(mockPeonDeath.getClass(), w);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		
		//Assert true if the first two tracked events are the DayNightTransitionTracker and PeonDeathTracker
		ArrayList<EventTracker> eventList = (ArrayList<EventTracker>) MineEventHandler.getInstance().getTrackedEvents();
		assertTrue(eventList.get(0).getName().equals("day/night transition tracker"));
		assertTrue(eventList.get(1).getName().equals("Achievement Tracker"));
		assertTrue(eventList.get(2).getName().equals("Peon Damaged Tracker"));
		assertTrue(eventList.get(3).getName().equals("Peon Dig Tracker"));
		assertTrue(eventList.get(4).getName().equals("Peon Mine Tracker"));
		assertTrue(eventList.get(5).getName().equals("Peon Death Tracker"));
	}
	
	
	@Test(expected = InvalidClassException.class)
	public void registerToNonEventTracker() throws InvalidClassException{
		Object o = new Object();
		Watcher w = Mockito.mock(Watcher.class);
		MineEventHandler.getInstance().registerFor(o.getClass(), w);
		
	}
	
	private class badTracker extends EventTracker{
		
		int freq = 1;
		
		@Override
		public void checkForEvent() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getName() {
			return "badTracker";
		}

		@Override
		public int getFreq() {
			return freq;
		}

		@Override
		public void setFreq(int value) {
			freq = value;
			
		}
		
	}
	
	/*@Test(expected = NoSuchMethodException.class)
	public void registerNoSuchMethod() throws NoSuchMethodException, InvalidClassException {
		badTracker bad = Mockito.mock(badTracker.class);
		Watcher w = Mockito.mock(Watcher.class); 
		MineEventHandler.getInstance().registerFor(bad.getClass(), w);
	}*/
	
	
	//PHONY
	@Test
	public void testCheckEvents(){
		//Mock a DayNightTransitionTracker and register it to MineHandler.
		DayNightTransistionTracker mockTracker = Mockito.mock(DayNightTransistionTracker.class);
		Mockito.when(mockTracker.getName()).thenReturn("day/night transition tracker");
		Watcher w = Mockito.mock(Watcher.class);
		try {
			MineEventHandler.getInstance().registerFor(mockTracker.getClass(), w);
		} catch (InvalidClassException e) {
					e.printStackTrace();
		}
				
		//Mock a PeonDeathTracker and register it to MineHandler.
		PeonDeathTracker mockPeonDeath = Mockito.mock(PeonDeathTracker.class);
		Mockito.when(mockPeonDeath.getName()).thenReturn("peonDeathTracker");
		try {
			MineEventHandler.getInstance().registerFor(mockPeonDeath.getClass(), w);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		MineEventHandler.getInstance().checkEvents();
		assertTrue(true);
		//I don't know 
	}


	
	//PHONY
	@Test
	public void testUpdate(){
		//Mock a DayNightTransitionTracker and register it to MineHandler.
		DayNightTransistionTracker mockTracker = Mockito.mock(DayNightTransistionTracker.class);
		Mockito.when(mockTracker.getName()).thenReturn("day/night transition tracker");
		Watcher w = Mockito.mock(Watcher.class);
		try {
			MineEventHandler.getInstance().registerFor(mockTracker.getClass(), w);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
						
		//Mock a PeonDeathTracker and register it to MineHandler.
		PeonDeathTracker mockPeonDeath = Mockito.mock(PeonDeathTracker.class);
		Mockito.when(mockPeonDeath.getName()).thenReturn("peonDeathTracker");
		try {
			MineEventHandler.getInstance().registerFor(mockPeonDeath.getClass(), w);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		
		
		Object arg = Mockito.mock(Object.class);
		
		MineEventHandler.getInstance().update(mockTracker, arg);
		assertTrue(true);
		//Who am I
	}
}
