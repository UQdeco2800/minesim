package minesim.entities;

import java.util.ArrayList;

import javafx.scene.image.Image;
import minesim.Sound;
import minesim.World;
import minesim.WorldTimer;

public class EnragedZombieMob extends Mob {
	
	private Sound zombieSound = new Sound("zombie.wav");
	private Sound digSound = new Sound("dig.wav");
	
	private Boolean expressionBool;

	public enum ZombieMobState {
		ZOMBIE_GRAVE, ZOMBIE_ROAMING, NULL
	}

	private ZombieMobState zombieState;

	SpriteLoader mobSpawn = new SpriteLoader();
	SpriteLoader mobDig = new SpriteLoader();

	WorldTimer time = new WorldTimer();


	/**
	 * Zombie Mob simply is the core basic mob that might have things
	 * implemented that are different compaared with other mobs
	 * 
	 * @param xpos
	 *            the x position where the mob is being created
	 * @param ypos
	 *            the y position where the mob is being created
	 * @param health
	 *            the amount of health the zombie is given
	 * @param sizeX
	 *            the x size of the zombie in pixles
	 * @param sizeY
	 *            the y size of the zombie in pixles
	 */
	public EnragedZombieMob(int xpos, int ypos, int health, int sizeX, int sizeY) {
		super(xpos, ypos, sizeX, sizeY, "zombieBrainSpriteSheet",
					"zombieBrainAttackingSpriteSheet", 2, 42, 42, health, 3, 20, 4);
		addCollisionIgnoredClass(this.getClass());
		addCollisionIgnoredClass(ZombieMob.class);
		addCollisionIgnoredClass(Grave.class);
		this.expressionBool = Boolean.FALSE;
		zombieState = ZombieMobState.ZOMBIE_ROAMING;
		mobSpawn.loadSpriteSheet("graveWithZombieBrainSpriteSheet", 42, 42);
		mobDig.loadSpriteSheet("graveWithZombieSpriteSheetDig", 42, 42);

	}

	@Override
	public void onTick() {
		// if its night time go out and hunt!
		if (World.timer.isDay() == 0) {
			// /if the zombie has already spawned, do normal mob things
			if (zombieState == ZombieMobState.ZOMBIE_ROAMING) {
				super.onTick();
				// if a peon has been found BRAINS!!
				if (this.getMobState() == MobState.MOB_HOSTILE
						&& this.expressionBool == Boolean.FALSE) {

					World.getInstance().addEntityToWorld(
							new FloatingExpression(this.getXpos(), (this
									.getYpos() - this.height), 16, 16, 100));
					this.expressionBool = Boolean.TRUE;
				} else if (this.getMobState() == MobState.MOB_IDLE
						&& this.expressionBool == Boolean.TRUE) {
					this.expressionBool = Boolean.FALSE;
				}
			}
		} else { // if its day time
			if (zombieState == ZombieMobState.ZOMBIE_ROAMING) {
				World.getInstance().addEntityToWorld(
						new Grave(this.getXpos(), this.getYpos(), 42, 52));
				World.getInstance().removeEntityFromWorld(this);
			}
		}
	}

	public ZombieMobState getZombieMobState() {
		return this.zombieState;
	}
}
