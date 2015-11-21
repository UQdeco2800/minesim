package minesim;

import org.junit.Test;

import java.util.Arrays;

import minesim.entities.Mob;
import minesim.entities.Peon;

import static org.junit.Assert.assertEquals;

public class MobTests {

    // Create Testing Mob
    private Mob testMob = new Mob(0, 0, 42, 42, "zombieSpriteSheet", "zombieAttackingSpriteSheet", 2, 42, 42,
            100, 1, 40, 4);
    // Create Testing Peon
    private Peon testPeon = new Peon(0, 0, 50, 50, "TestPeon");

    @Test
    public void testMobInitial() {

        // Test Position
        assertEquals(0, testMob.getXpos());
        assertEquals(0, testMob.getYpos());

        // Test dimentions
        assertEquals(42, testMob.height);
        assertEquals(42, testMob.width);

        // Test health
        assertEquals(100, testMob.getHealth());

        // Test if the mob isnt dead
        assertEquals(Boolean.FALSE, testMob.isDead());

        // Check the mob state initialy
        assertEquals(testMob.getMobState(), Mob.MobState.MOB_IDLE);
    }

    @Test
    public void testMobAnimate() {
        // Test animationGet function
        assertEquals(testMob.animation.get(0), testMob.getAnimation());

        // Test Animate Left
        testMob.animateLeft();
        assertEquals(testMob.getMobDirection(), Mob.MobDirection.LEFT);

        // Test Animate Right
        testMob.animateRight();
        assertEquals(testMob.getMobDirection(), Mob.MobDirection.RIGHT);
    }

    @Test
    public void testMobHealthVars() {
        // Test initial health
        assertEquals(100, testMob.getHealth());

        // Test Subtraction
        testMob.subtractHealth(1);
        assertEquals(99, testMob.getHealth());
        testMob.subtractHealth(19);
        assertEquals(80, testMob.getHealth());

        // Test Set Health
        testMob.setHealth(100);
        assertEquals(100, testMob.getHealth());

        // Test Mob Death
        testMob.subtractHealth(100);
        assertEquals(0, testMob.getHealth());
        assertEquals(Boolean.FALSE, testMob.isDead());
    }

    @Test
    public void testMobInteractions() {
        // Interact with the Mob
        testMob.setHealth(100);
        testPeon.setStrength(40); // the health is reduced my strength * 0.25
        assertEquals(testMob.interact(testPeon), Boolean.FALSE);
        // Check for initial results
        assertEquals(testMob.getMobState(), Mob.MobState.MOB_HOSTILE);
        assertEquals(testMob.getHealth(), 99);
        // Peon should interact with the mob until the mobs health is 0
        for (int i = 99; i > 0; i -= 1) {
            testMob.interact(testPeon);
            assertEquals(testMob.getHealth(), (i - 1));
        }
        // at this point the mobs health is equal to 0 and is not dead
        assertEquals(Boolean.FALSE, testMob.isDead());
        // Interact should return True from now on when interacting with
        // testPeon
        assertEquals(testMob.interact(testPeon), Boolean.TRUE);
        // Now the mobs health is < 0, which makes the mob still not dead, but
        // isnt alive
        assertEquals(testMob.alive, Boolean.FALSE);
    }

    @Test
    public void testMobOnTickAI() {
        Peon testPeonFar = new Peon(40, 0, 50, 50, "TestPeon");
        Boolean tests[] = {Boolean.FALSE};
        do {
            testMob.onTick();
            if (testMob.getMobDirection() == Mob.MobDirection.LEFT) {
                tests[0] = Boolean.TRUE;
            }

			/*if (testMob.getMobState() == Mob.MobState.MOB_ATTACK
                    && tests[1] == Boolean.TRUE) {
				tests[2] = Boolean.TRUE;
			}*/
        } while (Arrays.asList(tests).contains(Boolean.FALSE) == true);
        assertEquals(1, 1);
        //TODO: IMPLEMENT TESTING FOR OTHER BRANCHED WITHIN ONTICK
    }
}
