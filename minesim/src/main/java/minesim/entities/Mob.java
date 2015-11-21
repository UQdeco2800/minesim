package minesim.entities;

import java.util.ArrayList;
import java.util.Random;

import minesim.AchievementDatabaseHandler;
import minesim.entities.items.properties.HasHealth;
import javafx.scene.image.Image;

public class Mob extends WorldEntity implements HasHealth {

	// Create ENUM states for unique values to be used when creating an AI
	public enum MobState {
		MOB_IDLE, MOB_HOSTILE, MOB_ATTACK
	}

	public enum MobDirection {
		LEFT, RIGHT, ACTION
	}

	// Mob Sprite Sheet
	SpriteLoader mobSheet = new SpriteLoader();
	SpriteLoader mobAttackSheet = new SpriteLoader();

	// Images from sprite sheet for all(6) animations
	private ArrayList<Image> walkingRight = new ArrayList<Image>();
	private ArrayList<Image> walkingLeft = new ArrayList<Image>();
	private ArrayList<Image> attackingRight = new ArrayList<Image>();
	private ArrayList<Image> attackingLeft = new ArrayList<Image>();
	private ArrayList<Image> idle = new ArrayList<Image>();

	// Animations for sprite sheet
	public Animation walkRight;
	public Animation walkLeft;
	public Animation attackRight;
	public Animation attackLeft;
	public Animation idleAnimation;

	public ArrayList<Animation> animation = new ArrayList<Animation>();

	// Active Flag Var
	public static Boolean activeFlag = Boolean.FALSE;

	// Mob statistics
	private int health;
	private Boolean dead = Boolean.FALSE;
	private int mobSpeed;
	private int mobSpeedDelay;
	private int mobRange;

	// Mob movement
	private MobState state = MobState.MOB_IDLE;
	private MobDirection direction;
	private int idleCoord = randInt(1, 2000);
	private int currentMove = 0;
	private int lastMove = 0;
	private int moveDelay = 100;
	static final int distance_thresh = 150;
	private WorldEntity mobFocus;
	Boolean hasAttacked = Boolean.FALSE;

	/**
	 * Mob is a class that extends entities and provide a hazzard within the
	 * game. this class is able to be implemented to create different types of
	 * mobs.
	 * 
	 * @param xpos
	 *            the x position of where the mob is being created
	 * @param ypos
	 *            the y position of where the mob is being created
	 * @param height
	 *            the height of the mob and the size of the height of the sprite
	 *            sheet
	 * @param width
	 *            the width of the mob and the size of the width of the sprite
	 *            sheet
	 * @param spriteSheet
	 *            the NAME of the sprite sheet resource/"NAME".png.
	 * @param mobHealth
	 *            the amount of health to defeate the mob
	 * @param speed
	 *            the speed at which the mob is traveling towards a way point
	 * @param range
	 *            the range in which the mob is able to do damage to
	 * @param delay
	 *            the modular delay of how long it takes for the mob to take a
	 *            step
	 */
	public Mob(int xpos, int ypos, int height, int width,
			String spriteMovements, String attackSprite, int attackFrames,
			int spriteX, int spriteY, int mobHealth, int speed, int range,
			int delay) {
		super(xpos, ypos, height, width);

		// load sprite sheet

		mobSheet.loadSpriteSheet(spriteMovements, spriteX, spriteY);
		mobAttackSheet.loadSpriteSheet(attackSprite, spriteX, spriteY);

		// set up animation
		setUpAnimation(attackFrames);
		setAnimation(animation);

		// Create Mob state
		this.health = mobHealth;
		this.state = MobState.MOB_IDLE;
		this.direction = MobDirection.ACTION;
		this.mobRange = range;

		// speed is how fast the mob moves to that waypoint
		this.mobSpeed = speed;
		// Delay is the modular of how many ticks the mob can move
		this.mobSpeedDelay = delay;

		// set ignored collisions
		// for (class classes: this.getParentWorld().)
		addCollisionIgnoredClass(Mob.class);
		addCollisionIgnoredClass(this.getClass());
		addCollisionIgnoredClass(ZombieMob.class);
		addCollisionIgnoredClass(Grave.class);
		// start animation
		this.animation.set(0, walkLeft);
	}

	/**
	 * This Sets up the animation used within this Mob
	 * 
	 * @walkingRight : is the animation used when walking right
	 * 
	 * @walkingLeft : is the animation used when walking left
	 * 
	 * @idleAnimation : is the animation used when doing nothing
	 */
	private void setUpAnimation(int attackFrames) {
		// add images into the ArrayList<Image>
		idle.add(mobSheet.getSprite(0, 0));

		// add images for zombie spawning

		for (int a = 0; a < 3; a++) {
			walkingRight.add(mobSheet.getSprite(a, 0));
			walkingLeft.add(mobSheet.getSprite(a + 3, 0));
		}
		walkingRight.add(mobSheet.getSprite(2, 0));
		walkingLeft.add(mobSheet.getSprite(3, 0));

		attackingRight.add(mobAttackSheet.getSprite(0, 0));
		attackingRight.add(mobAttackSheet.getSprite(1, 0));
		attackingLeft.add(mobAttackSheet.getSprite(2, 0));
		attackingLeft.add(mobAttackSheet.getSprite(3, 0));

		idleAnimation = new Animation(idle, 10);
		walkLeft = new Animation(walkingLeft, 10);
		walkRight = new Animation(walkingRight, 10);
		attackRight = new Animation(attackingRight, 30);
		attackLeft = new Animation(attackingLeft, 30);

		animation.add(idleAnimation);
	}

	/**
	 * The function used to interact with the mob. Assuming no other
	 * interactions are needed, this interaction is simply reducing the mobs
	 * health until it dies.
	 * 
	 * @param sender
	 *            the parameter of the entity that is interacting with the mob.
	 */
	@Override
	public Boolean interact(WorldEntity sender) {
		if (sender instanceof Peon) {
			// reduce health at a rate equal to Peon strength rounded down
			this.mobFocus = (Peon) sender;
			this.state = MobState.MOB_HOSTILE;
			if (((Peon) sender).getAttack() > 4) {
				this.subtractHealth((int) ((int) ((Peon) sender).getAttack() * 0.25));
			} else {
				this.subtractHealth(1);
			}

			if (this.health < 0) {
				this.alive = Boolean.FALSE;
				AchievementDatabaseHandler achievement = AchievementDatabaseHandler.getInstance();
				achievement.counterIncrement("zombieKilled");
				// lvl up the peons strength stat
				((Peon) sender).setStrength(((Peon) sender).getStrength() + 1);
				return Boolean.TRUE;
			}
		} else if (sender instanceof Hadouken) {
			this.subtractHealth(5);
			if (this.health < 0) {
				this.alive = Boolean.FALSE;
				AchievementDatabaseHandler achievement = AchievementDatabaseHandler.getInstance();
				achievement.counterIncrement("zombieKilled");
			} // Hadouken hit a zombie, deals 10 damage
		}
		return Boolean.FALSE;
	}

	/**
	 * A function that returns a random number between the minimum and maximum
	 * given.
	 * 
	 * @param min
	 *            The Minimum that the random number returned wond go under
	 * @param max
	 *            The Maximum that the random number returned wont go over
	 * @return Returns an integer given between the minimum and maximum
	 */
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	/**
	 * This is the main core within how the mob acts within the world All mobs
	 * have 3 initial states (shown bellow). While being animated with the
	 * requird animations for that state
	 * 
	 * @MOB_IDLE : The Mob is roaming and moving do random locations
	 * 
	 * @MOB_HOSTILE :The Mob is angry at seeing an entity not of its own kind
	 *              and is moving forward to attack it
	 * 
	 * @MOB_ATTACK : The Mob is takig its time to deal some damage to that
	 *             entity
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onTick() {
		super.onTick();
		this.currentMove++;
		// while mob has nothing to do walk around
		if (this.state == MobState.MOB_IDLE) {
			// move towards that current coord
			if (this.currentMove % this.mobSpeedDelay == 1) {
				moveTowards(this.idleCoord, 0, this.mobSpeed);
			}
			if (currentMove >= lastMove + moveDelay) {
				// find the current waypoint
				this.lastMove = this.currentMove;
				this.idleCoord = randInt(1, 2000);
				// animate the mobs direction
				
			}
			if ((this.getXpos() - idleCoord) < 0) {
				animateRight();

			} else {
				animateLeft();
			}
			// however if the mob is hostile
		}
		if (this.state == MobState.MOB_HOSTILE) {
			// check to see if the entity is close
		    int dist = (int) (Math.pow(this.getXpos() - this.mobFocus.getXpos(), 2) + Math.pow(this.getYpos() - this.mobFocus.getYpos(), 2));
			if (dist <= Math.pow(this.mobRange, 2)) {
				//ATTACK IF CLOSE
				this.state = MobState.MOB_ATTACK;
			} else {
				// if its not close to the peon, then walk towards it with a
				// third of the delay
				if (this.mobFocus.alive && this.mobFocus != null) {
					if (this.currentMove % this.mobSpeedDelay <= 1) {
						moveTowards(this.mobFocus.getXpos(), 0, mobSpeed);
					}
					if ((this.getXpos() - this.mobFocus.getXpos()) < 0) {
						animateRight();
					} else {
						animateLeft();
					}
					// however if the peon is too far away, then start idling
					if (this.getXpos() - this.mobFocus.getXpos() > 200
							|| this.getXpos() - this.mobFocus.getXpos() < -200) {
						this.state = MobState.MOB_IDLE;
					}
					if (this.currentMove % this.mobSpeedDelay <= 1) {
						moveTowards(this.mobFocus.getXpos(), 0, mobSpeed);
					}
					if ((this.getXpos() - this.mobFocus.getXpos()) < 0) {
						animateRight();
					} else {
						this.state = MobState.MOB_IDLE;
					}
				}
			}
		}

		if (this.state == MobState.MOB_ATTACK) {
			// if so every frame take away entities health
			if ((animation.get(0).getCurrentFrame() + 1) == animation.get(0).getTotalFrames()) {				
				if (this.hasAttacked == Boolean.FALSE) {
					HasHealth entityClose = (HasHealth) mobFocus;
					this.hasAttacked = Boolean.TRUE;
					entityClose.subtractHealth(5);
					// check if dead
					if (entityClose.isDead()) {
						this.state = MobState.MOB_IDLE;
						return;
					}
				}
			} else {
				this.hasAttacked = Boolean.FALSE;
			}
			if (this.direction == MobDirection.LEFT) {
				animateAttackLeft();
			} else {
				animateAttackRight();
			}
			if ((this.getXpos() - this.mobFocus.getXpos() <= mobRange && this
					.getXpos() - this.mobFocus.getXpos() >= -mobRange)
					&& this.mobFocus.alive) {
				this.state = MobState.MOB_HOSTILE;
			}
		}

		// check to see if there are any enitities that are close
		if (this.state == MobState.MOB_IDLE) {
			for (WorldEntity entity : this.getParentWorld().getWorldentities()) {
				if (!(Mob.class.isAssignableFrom(entity.getClass()))
						&& !Grave.class.isAssignableFrom(entity.getClass())
						&& HasHealth.class.isAssignableFrom(entity.getClass())) {
					// check to see if the mob is facing the close entities

					if(this.getXpos() - entity.getXpos() <= 200){
						if(this.getXpos() - entity.getXpos() >= 0){
							if(this.direction == MobDirection.LEFT){
								addCollisionIgnoredClass(entity.getClass());
								this.mobFocus = entity;
								this.state = MobState.MOB_HOSTILE;
							}
						}
					}
					else if(this.getXpos() - entity.getXpos() >= -200){
						if(this.getXpos() - entity.getXpos() <= 0){
							if(this.direction == MobDirection.RIGHT){
								addCollisionIgnoredClass(entity.getClass());
								this.mobFocus = entity;
								this.state = MobState.MOB_HOSTILE;
							}
						}
					}
					
/*					if (((this.getXpos() - entity.getXpos() <= 200 && this
							.getXpos() - entity.getXpos() >= 0) && this.direction == MobDirection.LEFT)
							|| ((this.getXpos() - entity.getXpos() >= -200 && this
									.getXpos() - entity.getXpos() <= 0) && this.direction == MobDirection.RIGHT)) {
						addCollisionIgnoredClass(entity.getClass());
						this.mobFocus = entity;
						this.state = MobState.MOB_HOSTILE;
					}*/
				}
			}
		}
		// maintain animation
		animation.get(0).update();
		setAnimation(animation);
	}

	/**
	 * The function used to animate this mob to face the left of the game
	 */
	public void animateLeft() {
		animation.set(0, walkLeft);
		this.direction = MobDirection.LEFT;
		animation.get(0).start();
	}

	/**
	 * The function used to animate this mob to face the right of the game
	 */
	public void animateRight() {
		animation.set(0, walkRight);
		this.direction = MobDirection.RIGHT;
		animation.get(0).start();
	}

	/**
	 * The function used to animate this mob to face the left of the game
	 */
	public void animateAttackLeft() {
		animation.set(0, attackLeft);
		animation.get(0).start();
	}

	/**
	 * The function used to animate this mob to face the right of the game
	 */
	public void animateAttackRight() {
		animation.set(0, attackRight);
		animation.get(0).start();
	}

	/**
	 * Simply used to return this mobs given direction
	 * 
	 * @return the direction this mob is facing, either LEFT or RIGHT
	 */
	public MobDirection getMobDirection() {
		return this.direction;
	}

	/**
	 * This function simply returns the mobs given state
	 * 
	 * @return the mobs state, MOB_(IDLE/HOSTILE/ATTACK)
	 */
	public MobState getMobState() {
		return this.state;
	}

	/**
	 * Returns this mobs current animation
	 * 
	 * @return this mobs current animation
	 */
	public Animation getAnimation() {
		return this.animation.get(0);
	}

	/**
	 * Returns this mobs current health
	 * 
	 * @return this mobs current health
	 */
	@Override
	public int getHealth() {
		return this.health;
	}

	/**
	 * returns a Boolean to show if this mob is dead or not
	 * 
	 * @return returns True if the mob is dead, False otherwise
	 */
	@Override
	public Boolean isDead() {
		return this.dead;
	}

	/**
	 * Subtract a factor from the mobs current health
	 * 
	 * @param factor
	 *            the number being subtracted from the mobs health
	 */
	@Override
	public void subtractHealth(int factor) {
		this.health -= factor;
	}

	/**
	 * Sets the mobs health towards a given factor
	 * 
	 * @param health
	 *            an integer representing the set number towards the current
	 *            mobs health
	 */
	@Override
	public void setHealth(int health) {
		this.health = health;

	}

}
