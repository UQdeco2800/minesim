package minesim;

import org.junit.Test;

import minesim.entities.Mob;
import minesim.entities.ZombieMob;

import static org.junit.Assert.assertEquals;

public class ZombieMobTest {
    private ZombieMob testZombieMob = new ZombieMob(0, 0, 100, 32, 32);

    @Test
    public void testZombieMobInitial() {
        // test initiall zombie mob stats
        assertEquals(0, testZombieMob.getXpos());
        assertEquals(0, testZombieMob.getYpos());

        assertEquals(100, testZombieMob.getHealth());
        assertEquals(testZombieMob.getMobState(), Mob.MobState.MOB_IDLE);
        assertEquals(testZombieMob.getZombieMobState(),
                ZombieMob.ZombieMobState.ZOMBIE_ROAMING);
    }
}
