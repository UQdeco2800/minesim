package minesim;

import org.junit.Test;
import static org.junit.Assert.*;

import minesim.entities.Achievement;
public class AchievementTest {
	
	@Test
	public void achievementCallTest(){
		Achievement newAchievement = new Achievement(100, 100, 60, 480, "rock1");
		assertNotNull(newAchievement);
		assertTrue(newAchievement.alive);
		assertTrue(newAchievement.getAchievementType() == "rock1");
		assertTrue(newAchievement.height == 60);
		assertTrue(newAchievement.width == 480);
		assertTrue(newAchievement.getXpos() == 100);
		assertTrue(newAchievement.getYpos() == 100);
	}
	
	@Test
	public void achievementSetImage(){
		Achievement newAchievement = new Achievement(100, 100, 60, 480, "rock1");
		assertTrue(newAchievement.getImage() == "/AchievementImages/1rock.png");
		newAchievement.setImage("test1");
		assertTrue(newAchievement.getImage() == "test1");
	}
}
