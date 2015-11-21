package minesim.entities;

import minesim.entities.Mob.MobDirection;
import minesim.entities.items.properties.HasHealth;
import minesim.FrameHandler;
import minesim.World;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.image.Image;

public class AvianMob extends WorldEntity implements HasHealth {

	SpriteLoader mobSheet = new SpriteLoader();

	// Spawning variables
	public Boolean hasSpawned = Boolean.FALSE;
	public int spawnX;
	
	// Animation variables
	private ArrayList<Image> flyingRight = new ArrayList<Image>();
	private ArrayList<Image> flyingLeft = new ArrayList<Image>();

	public Animation flyRight;
	public Animation flyLeft;

	public ArrayList<Animation> animation = new ArrayList<Animation>();
	
	// Avian Variables
	private int health;
	private Boolean isDead = Boolean.FALSE;
	
	// Avian Movement Variables
	private int avianYDestination;
	private int headTowardsScreen;
	private Boolean isFlapping = Boolean.FALSE;
	private int idleCoord;
	private int currentMove = 0;
	private int lastMove = 0;
	private int moveDelay = 100;
	
	
	public AvianMob(int height, int width, int mobHealth, String spriteMovements) {
		super(0, 0, height, width);
		this.health = mobHealth;
		this.setGravity(15);
		this.setEntityGravity(false);
		mobSheet.loadSpriteSheet(spriteMovements, 39, 22);

		setUpAnimation();
		setAnimation(animation);

		addCollisionIgnoredClass(this.getClass());

	}

	private int getRandomInt(int min, int max) {
		return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
	}

	private void setUpAnimation() {
		flyingRight.add(mobSheet.getSprite(0, 0));
		flyingRight.add(mobSheet.getSprite(1, 0));

		flyingLeft.add(mobSheet.getSprite(2, 0));
		flyingLeft.add(mobSheet.getSprite(3, 0));

		flyRight = new Animation(flyingRight, 10);
		flyLeft = new Animation(flyingLeft, 10);

		animation.add(flyRight);
		animation.get(0).start();
	}

	public void onTick() {
		super.onTick();
		this.currentMove++;
		
		// SPAWN THE BAT
		while (hasSpawned == Boolean.FALSE) {
			spawnX = getRandomInt(0, this.getParentWorld().getMaxWidth());
			if (spawnX < (100 + this.getParentWorld().getXOffset())) {
				this.setXpos((int) (this.getParentWorld().getXOffset() - 40));
				hasSpawned = Boolean.TRUE;
			} else { // Check and Spawn Right
				if (spawnX > (this.getParentWorld().getXOffset()
						+ this.getParentWorld().getWidth() + 100)) {
					this.setXpos((int) (this.getParentWorld().getXOffset() + this
							.getParentWorld().getWidth()) + 40);
					hasSpawned = Boolean.TRUE;
				}
			}
			this.setEntityGravity(true);
		}
		
		if (this.ypos > 150 && isFlapping == Boolean.FALSE) {
			isFlapping = Boolean.TRUE;
			avianYDestination = getRandomInt(0, 100);
			
		}
		if (isFlapping == Boolean.TRUE){
			flapMove(avianYDestination);
		}
		///////////////////////////////////////
		moveTowards(this.idleCoord, 0, 1);
		if (currentMove >= lastMove + moveDelay) {
			// find the current waypoint
			headTowardsScreen = getRandomInt(0, 100);
			if (headTowardsScreen < 50) {
				this.idleCoord = getRandomInt(1, this.getParentWorld().getMaxWidth());
			} else {
				this.idleCoord = getRandomInt(this.getParentWorld().getXOffset(), 
						this.getParentWorld().getXOffset()
						+ this.getParentWorld().getWidth());
			}
			this.lastMove = this.currentMove;
			// animate the mobs direction
			
		}
		if ((this.getXpos() - idleCoord) < 0) {
			flyRight();

		} else {
			flyLeft();
		}
		
		animation.get(0).update();
		setAnimation(animation);
	}

	private void flyLeft() {
		animation.set(0, flyLeft);
		animation.get(0).start();
	}

	private void flyRight() {
		animation.set(0, flyRight);
		animation.get(0).start();
	}

	public void flapMove(int destination) {
		this.setEntityGravity(false);
		isFlapping = Boolean.TRUE;
		if (!this.moveVertical(destination, 2)){
			this.setEntityGravity(true);
			isFlapping = Boolean.FALSE;
		}
	}

	@Override
	public int getHealth() {
		return this.health;
	}

	@Override
	public void setHealth(int health) {
		this.health = health;

	}

	@Override
	public Boolean isDead() {
		return this.isDead;
	}

	@Override
	public void subtractHealth(int factor) {
		// TODO Auto-generated method stub
		this.health -= factor;
	}

}
