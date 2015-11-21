package minesim.entities;

import java.io.InvalidClassException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Deque;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import minesim.Sound;
import minesim.events.tracker.MineEventHandler;
import minesim.events.tracker.PeonDamagedTracker;
import minesim.events.tracker.PeonDeathTracker;
import minesim.events.tracker.PeonDigTracker;
import minesim.events.tracker.PeonMineTracker;
import minesim.events.tracker.Watcher;
import minesim.World;
import minesim.inputhandlers.MouseHandler;
import minesim.tasks.AttackMob;
import minesim.tasks.DigArea;
import minesim.tasks.DigTile;
import minesim.tasks.EnterBuilding;
import minesim.tasks.PeonJump;
import minesim.tasks.PeonEscapeRope;
import minesim.tasks.PeonPiggyBack;
import minesim.tasks.PickUpItem;
import minesim.tasks.RunAway;
import minesim.tasks.Task;
import minesim.tasks.UseStore;
import minesim.tasks.WalkTowards;
import notifications.NotificationManager;
import minesim.entities.disease.Illness;
import minesim.contexts.PeonStatusContextControllerHandler;
import minesim.entities.items.*;
import minesim.entities.disease.Disease;
import minesim.entities.disease.Medicine;
import minesim.buffs.Infection;
import minesim.entities.disease.Flu;

import java.util.Random;

public class Peon extends Stats implements HasName {

	Sound oof = new Sound("oof.mp3");

	boolean damagedFlag = false;
	boolean attackingFlag = false;

	
	SpriteLoader peonSheet = new SpriteLoader();
	SpriteLoader diggingPeon = new SpriteLoader();
	SpriteLoader climbSheet = new SpriteLoader();

	/**
	 * The tick delay of a peon. Can only perform  an action after this many ticks have
     * passed, effective number can be reduced by speed stat, and increased by
     * tiredness
	 */
	private int tickDelay = 7;

	/**A string of "Idle", use as condition in tasks*/
	public String strOfTask = "Idle";
	
	/**The delay at which it takes for a peon to recover a tick of health*/
	private int recoveryDelay = 100; 
	
	/**The name of a peon, this is overwritten!*/
	private String name = "";
	
	/** A list of all notifications relevent to the peon*/
	private List<String> notificationList;
	
	/**
	 * A list of all "thoughts" the peon might have, which are event-related
	 * helpful hints the peon may give
	 */
	private List<String> thoughtsList;
	
	/**The direction the peon is moving, used in conditional statement*/
	private int movingDirection = 1;
	
	/**A boolean for mining check. If true, play mining animation*/
	private boolean isMining = false;
	
	/**A boolean for checking digging. If true, play digging animation*/
	private boolean isDigging = false;
	Tool shovel = new Tool(0, 0, 20, 20, "shovel", 0, 0, "all", "equip");
	
	/**A boolean for checking climbing. If true, play climbing animation*/
	private boolean isClimbing = false;
	
	/**A boolean to check if maxHP is found*/
	public boolean maxHPFound = false;
	
	/**A boolean to check if maxStamina is found*/
	public boolean maxStaminaFound = false;
	
	/**The annoyance level of a peon*/
	private int annoyanceFg = 0;
	
	/**If rescueSet true, enable EscapeRope function*/
	private boolean rescueSet = false;
	
	/**The number of building buffs allowed on a single peon*/
	private int buildingBonus = 2;

	/**An array to see if a buff is active*/
	private int[] buildingBonusArray = { 0, 0, 0, 0, 0, 0, 0, 0 };
	private int[] buildingBonusArrayTimer = { 0, 0, 0, 0, 0, 0, 0, 0 };

	/**
	 * the list of tasks the peon is currently executing (ie, digTile is
	 * actually 2 tasks: walkTowards and digTile)
	 */
	private Deque<Task> taskList = new ArrayDeque<Task>();
	
	/**
	 * bonus
	 * ArrayList<Inventory.InventoryItem> inv = new
	 * ArrayList<Inventory.InventoryItem>();
	 */
	public Inventory peonInventory = new Inventory(9, getAnnoyance(), null,
			null, null, null, null, new ArrayList<Inventory.InventoryItem>(),
			this);

	/**Disease status, a variable which identity which disease it has*/
	private Illness disease = null;
	
	/**The number of diseases a peon has*/
	private static int totalInfections = 0;

	/**
	 * Images from sprite sheet for all animations. There are a lot!
	 */
	private ArrayList<Image> standingRight = new ArrayList<Image>();
	private ArrayList<Image> standingLeft = new ArrayList<Image>();
	private ArrayList<Image> standingRightShovel = new ArrayList<Image>();
	private ArrayList<Image> standingLeftShovel = new ArrayList<Image>();
	private ArrayList<Image> standingRightPick = new ArrayList<Image>();
	private ArrayList<Image> standingLeftPick = new ArrayList<Image>();
	private ArrayList<Image> walkingLeft = new ArrayList<Image>();
	private ArrayList<Image> walkingRight = new ArrayList<Image>();
	private ArrayList<Image> walkingLeftShovel = new ArrayList<Image>();
	private ArrayList<Image> walkingRightShovel = new ArrayList<Image>();
	private ArrayList<Image> walkingLeftPick = new ArrayList<Image>();
	private ArrayList<Image> walkingRightPick = new ArrayList<Image>();
	private ArrayList<Image> mineRight = new ArrayList<Image>();
	private ArrayList<Image> mineLeft = new ArrayList<Image>();
	private ArrayList<Image> digRight = new ArrayList<Image>();
	private ArrayList<Image> digLeft = new ArrayList<Image>();
	private ArrayList<Image> climb = new ArrayList<Image>();

	/**
	 * Animations
	 */
	private Animation walkLeft;
	private Animation walkRight;
	private Animation walkLeftPick;
	private Animation walkRightPick;
	private Animation walkLeftShovel;
	private Animation walkRightShovel;
	private Animation standRight;
	private Animation standLeft;
	private Animation standRightPick;
	private Animation standLeftPick;
	private Animation standRightShovel;
	private Animation standLeftShovel;
	private Animation miningRight;
	private Animation miningLeft;
	private Animation diggingRight;
	private Animation diggingLeft;
	private Animation climbing;
	private ArrayList<Animation> animation = new ArrayList<Animation>();

	/**Whether the action that the peon jump*/
	private Optional<Task> jump = Optional.empty();
	private int buildingbonus = 1;

	public Peon(int xpos, int ypos, int height, int width, String name) {
		super(xpos, ypos, height, width);
		this.name = name;
		peonSheet.loadSpriteSheet("peonSheet", 32, 32);
		diggingPeon.loadSpriteSheet("diggingPeon", 32, 32);
		climbSheet.loadSpriteSheet("climb_all", 32, 32);
		setUpAnimation();
		setAnimation(animation);
		this.setStrength((int) (((Math.random() % .5) + .1) * 10));
		addCollisionIgnoredClass(Peon.class);
		addCollisionIgnoredClass(BuildingGym.class);
		addCollisionIgnoredClass(BuildingWell.class);
		addCollisionIgnoredClass(BuildingBar.class);
		addCollisionIgnoredClass(BuildingBlacksmith.class);
		addCollisionIgnoredClass(BuildingNoodlehaus.class);
		addCollisionIgnoredClass(BuildingTeleporterIn.class);
		addCollisionIgnoredClass(BuildingTeleporterOut.class);
		addCollisionIgnoredClass(BuildingHospital.class);
		addCollisionIgnoredClass(Mob.class);
		addCollisionIgnoredClass(ZombieMob.class);
		addCollisionIgnoredClass(Grave.class);
		addCollisionIgnoredClass(PeonGather.class);
		addCollisionIgnoredClass(PeonGuard.class);
		addCollisionIgnoredClass(PeonMiner.class);
		addCollisionIgnoredClass(Item.class);
		addCollisionIgnoredClass(Apparel.class);
		addCollisionIgnoredClass(MinedEntity.class);
		addCollisionIgnoredClass(Food.class);
		addCollisionIgnoredClass(Weapon.class);
		addCollisionIgnoredClass(Tool.class);
		addCollisionIgnoredClass(Ladder.class);
		addCollisionIgnoredClass(Rope.class);
		addCollisionIgnoredClass(Elevator.class);
		addCollisionIgnoredClass(RequisitionStore.class);
	}

	/**
	 * added by Team-Dahl -->
	 **/
	public Inventory getInventory() {
		return this.peonInventory;
	}

	/**
	 * Set the peon inventory
	 * @param inventory
	 */
	public void setInventory(Inventory inventory) {
		this.peonInventory = inventory;
	}

	/**
	 * Set up the animation of the peon sprite
	 */
	private void setUpAnimation() {
		standingRight.add(peonSheet.getSprite(3, 1));
		standingLeft.add(peonSheet.getSprite(3, 0));
		standingRightShovel.add(peonSheet.getSprite(3, 3));
		standingLeftShovel.add(peonSheet.getSprite(3, 2));
		standingRightPick.add(peonSheet.getSprite(3, 7));
		standingLeftPick.add(peonSheet.getSprite(3, 6));
		for (int a = 0; a < 3; a++) {
			walkingRight.add(peonSheet.getSprite(a, 1));
			walkingLeft.add(peonSheet.getSprite(a, 0));
			walkingLeftShovel.add(peonSheet.getSprite(a, 2));
			walkingRightShovel.add(peonSheet.getSprite(a, 3));
			walkingLeftPick.add(peonSheet.getSprite(a, 6));
			walkingRightPick.add(peonSheet.getSprite(a, 7));
		}
		for (int b = 0; b < 4; b++) {
			digLeft.add(diggingPeon.getSprite(b, 0));
			digRight.add(diggingPeon.getSprite(b, 1));
			mineLeft.add(peonSheet.getSprite(b, 8));
			mineRight.add(peonSheet.getSprite(b, 9));

		}
		for (int c = 1; c < 4; c++) {
			climb.add(climbSheet.getSprite(c, 0));
			climb.add(climbSheet.getSprite(c, 1));
		}

		walkLeft = new Animation(walkingLeft, 10);
		walkRight = new Animation(walkingRight, 10);
		standRight = new Animation(standingRight, 10);
		standLeft = new Animation(standingLeft, 10);

		walkLeftPick = new Animation(walkingLeftPick, 10);
		walkRightPick = new Animation(walkingRightPick, 10);
		standLeftPick = new Animation(standingLeftPick, 10);
		standRightPick = new Animation(standingRightPick, 10);

		walkLeftShovel = new Animation(walkingLeftShovel, 10);
		walkRightShovel = new Animation(walkingRightShovel, 10);
		standLeftShovel = new Animation(standingLeftShovel, 10);
		standRightShovel = new Animation(standingRightShovel, 10);

		miningRight = new Animation(mineRight, 10);
		miningLeft = new Animation(mineLeft, 10);

		diggingRight = new Animation(digRight, 10);
		diggingLeft = new Animation(digLeft, 10);

		climbing = new Animation(climb, 20);

		animation.add(standRight);
	}

	/**
	 * attempt the current task, or increment the delay counter can result in
	 * failure (eg, peon being annoyed) returns 1 for success, 0 for failure
	 * (task cancelled/is not present)
	 * 
	 * Peons won't do a task a certain number of ticks have passed the
     * number of ticks to wait is 7, but this number is influenced by
     * tiredness and speed
	 */
	private int attemptTask() {
		if (this.annoyanceFg > 0) {
			this.annoyanceFg -= 1;
			if (getCurrentTask().isPresent()) {
				this.updateTask(Optional.<Task> empty());
				NotificationManager.notify(this.getName()
						+ " is annoyed and won't!");
				this.notificationList.add(this.getName()
						+ " is annoyed and won't!");
			}
		}
		if (this.tickDelay + (getTiredness() / 100) > 0) {
			this.tickDelay -= (this.getSpeed());
			return 1;
		} else if (getCurrentTask().isPresent()) {
			// This Peon has a task
			if ((this.getAnnoyance() - this.getHappiness()) > (((int) (Math
					.random() * 1000)) + 250)) {
				this.getCurrentTask().get().switchActiveFlag();
				this.updateTask(Optional.<Task> empty());
				NotificationManager.notify(this.getName()
						+ " is annoyed and stopped working!");
				this.notificationList.add(this.getName()
						+ " is annoyed and stopped working!");

				annoyanceFg = 1000;
			} else {
				getCurrentTask().get().doTask();

				this.tickDelay = 5;
			}
			return 1;
		} else {
			strOfTask = "Idle";
			return 0;
		}
	}

	/**
	 * Recover tiredness, annoyance and health by standard amounts
	 */
	private void recoverStats() {
		if (recoveryDelay <= 0) {
			if (getTiredness() > 0) {
				this.addTiredness(-5);
			}
			if (getAnnoyance() > 0) {
				this.addAnnoyance(-1);
			}
			if (getHealth() < 100) {
				this.subtractHealth(-1);
			}
			this.recoveryDelay = getRecoveryRate();
		} else {
			recoveryDelay--;
		}

		if (getHealth() <= 0) {
			this.alive = Boolean.FALSE;
		}
	}

	/** attempt to assign this peon a task from the list of tasks in world*/
	private void autoTaskAssign() {
		if (!this.getParentWorld().getWorldTasks(this).isEmpty()) {
			Task newtask = this.getParentWorld().getWorldTasks(this).getFirst();
			this.getParentWorld().removeTask(newtask);

			newtask.setPeon(this);
			this.updateTask(Optional.of(newtask));
			newtask.switchActiveFlag();

			NotificationManager.notify(this.getName()
					+ " was auto-assigned a task.");
			this.notificationList.add(this.getName()
					+ " was auto-assigned a task.");
			this.updateTask(Optional.of(newtask));

			if (newtask.getDest() + getParentWorld().getXOffset() > this
					.getXpos()) {
				animateRight();
			}
			if (newtask.getDest() + getParentWorld().getXOffset() < this
					.getXpos()) {
				animateLeft();
			}
			if (newtask.getDest() == this.getXpos()) {
				standStill();
			}
		}
	}

	@Override
	public void onTick() {
		super.onTick();
		if (getTiredness() >= 1000) {
			setCollapsed(true);
		}

		if (!getCollapse()) {
			if (attemptTask() == 0) {
				recoverStats();
				autoTaskAssign();
			}
			customAIFuntion();
		}

		tickBuildingBuffs();

		if (jump.isPresent()) {
			jump.get().doTask();
		}
		for (int i = 0; i < animation.size(); ++i) {
			animation.get(i).update();
		}
		setAnimation(this.animation);

		if (getExperience() >= (experienceRequired())) {
			levelUp(getExperience());
		}
		customAIFuntion();
	}

	

	/**
	 * A mouse click registered on this entity and we should deal with it
	 */
	@Override
	public void notifyOfClickActionOnEntity(MouseHandler sender) {

	    /** For the peon we are using a 'second click to action' system
	     * so we must register to be notified about the next click action
	     * regardless of who is clicked on
	     * See notifyOfRegisteredClickAction below
	     * InventoryPopUp popup = new InventoryPopUp(this.peonInventory, this);
	     * sender.registerForNextClick(this);
	     * This is part of invetory but I have no idea where it is suppose to
	     * live......
	     * */
		sender.registerForNextClick(this);
	}

	/**
	 * Called when you want to turn this peon into a grave
	 */
	public void zombified() {
		World.getInstance().addEntityToWorld(
				new ZombieMob(this.getXpos(), this.getYpos() + 5, 100, 42, 42));
		World.getInstance().removeEntityFromWorld(this);
	}

	/**
	 * This object registered to be notified about the last click event and this
	 * is the click event returning
	 */
	@Override
	public void notifyOfRegisteredClickAction(MouseHandler sender,
			MouseEvent event, int actionMode) {

		System.out.println(event.getButton());
		Optional<WorldEntity> entityclickedopt = this.getParentWorld()
				.getEntityForCoordinates(
						event.getX() + getParentWorld().getXOffset(),
						event.getY() + getParentWorld().getYOffset());
		if (event.getX() + getParentWorld().getXOffset() > this.getXpos()) {
			animateRight();
		}
		if (event.getX() + getParentWorld().getXOffset() < this.getXpos()) {
			animateLeft();
		}
		if (event.getX() == this.getXpos()) {
			standStill();
		}
		if (entityclickedopt.isPresent()) {
			WorldEntity entity = entityclickedopt.get();
			if (entity instanceof Building) {
				if (this.getCurrentTask().isPresent()) {
					this.getCurrentTask().get().switchActiveFlag();
				}
				Task newtask = new EnterBuilding(this, (Building) entity);
				this.clearTasks();
				this.updateTask(Optional.of(newtask));
				newtask.switchActiveFlag();
				this.attackingFlag = false;
				strOfTask = "Entering building";
			}
			if (entity instanceof Transportation) {
				if (this.getCurrentTask().isPresent()) {
					this.getCurrentTask().get().switchActiveFlag();
				}

				Task newtask = new WalkTowards(this, entity.getXpos(),
						entity.getYpos(), getParentWorld().getPathfinder());
				this.updateTask(Optional.of(newtask));
				newtask.switchActiveFlag();
				this.attackingFlag = false;
				strOfTask = "Walking";
			}
			if (entity instanceof Mob) {
				if (this.getCurrentTask().isPresent()) {
					this.getCurrentTask().get().switchActiveFlag();
				}
				Task newtask = new AttackMob(this, (Mob) entity);
				this.clearTasks();
				this.updateTask(Optional.of(newtask));
				newtask.switchActiveFlag();
				strOfTask = "Attacking";
				this.attackingFlag = true;
			}

			if (entity instanceof RequisitionStore) {
				if (this.getCurrentTask().isPresent()) {
					this.getCurrentTask().get().switchActiveFlag();
				}
				Task newTask = new UseStore(this, (RequisitionStore) entity);
				this.updateTask(Optional.of(newTask));
				newTask.switchActiveFlag();
			}

			if (entity instanceof Item && !(entity instanceof Transportation)) {
				if (this.getCurrentTask().isPresent()) {
					this.getCurrentTask().get().switchActiveFlag();
				}
				Task newTask = new PickUpItem(this, (Item) entity);
				this.updateTask(Optional.of(newTask));
				newTask.switchActiveFlag();
			}
		} else {
			Task newtask = null;
			switch (actionMode) {
			case 0:
				newtask = new WalkTowards(this, (int) event.getX()
						+ getParentWorld().getXOffset(), (int) event.getY()
						+ getParentWorld().getYOffset(), getParentWorld()
						.getPathfinder());
				strOfTask = "Walking";
				break;
			case 1:
				/**
				 * If Peon tries to dig, but they don't have a digging tool in
				 * their inventory (shovel), add shovel item into inventory and
				 * ensure its health is reset to 0.
				 */
				if (!(getInventory().doesInventoryContainItemWithName("shovel"))) {
					getInventory().addItem(shovel);
					getInventory().getDiggingTool("shovel").setHealth(100);
				}
				/**
				 * If peon tries to dig, but their digging tool has a health
				 * less than or equal to zero, reset health to 100
				 */
				if (getInventory().getDiggingTool("shovel").getHealth() <= 0) {
					getInventory().getDiggingTool("shovel").setHealth(100);
				}
				newtask = new DigTile(this, (int) event.getX()
						+ getParentWorld().getXOffset(), (int) event.getY()
						+ getParentWorld().getYOffset(), this.getParentWorld()
						.getTileManager(), 10);
				strOfTask = "Digging";
				break;
			}
			if (this.getCurrentTask().isPresent()) {
				this.getCurrentTask().get().switchActiveFlag();
			}
			this.clearTasks();
			this.updateTask(Optional.of(newtask));
			this.attackingFlag = false;
			this.getCurrentTask().get().switchActiveFlag();
			if (this.getCurrentTask().isPresent()
					&& this.getCurrentTask().get() instanceof WalkTowards) {
				this.getCurrentTask().get().switchActiveFlag();
			}
		}
		if (!this.getCurrentTask().isPresent()) {
			this.attackingFlag = false;
			standStill();
		}
	}

	/**
	 * For drag event This object registered to be notified about the last click
	 * event and this is the click event returning
	 **/
	@Override
	public void notifyOfRegisteredClickAction(int startX, int startY,
			MouseHandler sender, MouseEvent event, int actionMode) {
		switch (actionMode) {
		case 0:
			Task newtask = new WalkTowards(this, (int) event.getX()
					+ getParentWorld().getXOffset(), (int) event.getY()
					+ getParentWorld().getYOffset(), getParentWorld()
					.getPathfinder());
			this.updateTask(Optional.of(newtask));
			newtask.switchActiveFlag();
			break;
		case 1:
			if (!(getInventory().doesInventoryContainItemWithName("shovel"))) {
				getInventory().addItem(shovel);
				getInventory().getDiggingTool("shovel").setHealth(1000000);
			}
			DigArea newtask1 = new DigArea(this, startX, (int) event.getX()
					+ getParentWorld().getXOffset(), startY, (int) event.getY()
					+ getParentWorld().getYOffset(), this.getParentWorld()
					.getTileManager());
			newtask1.switchActiveFlag();
			break;
		}

	}

	/**
	 * attempt to increase Tiredness and Annoyance be the supplied amounts will
	 * not increment over stat maximums
	 */
	public void taskTiredness(int tired, int annoyed) {
		this.addTiredness(tired);
		this.addAnnoyance(annoyed);
	}

	/**
	 * Returns the status of a peon disease
	 * @return disease
	 */
	public boolean isDiseased() {
		return (disease != null);
	}

	/**
	 * Returns the status of disease
	 */
	public String statusDiseased() {
		if (disease != null) {
			return "Yes";
		} else {
			return "No";
		}
	}

	/**
	 * Setter for diseased state of agent
	 */
	public void setDiseased(Illness newDisease) {
		if (!isDiseased() && newDisease.canInfect(this)) {
			disease = newDisease;
			/**
			 * animation = (orientation, this.disease);
			 */
			++totalInfections;
		}
	}

	/**
	 * Set the disease affect
	 * @param currentDisease
	 */
	public void setDiseasedEffect(Illness currentDisease) {
		if (this.disease.getName() == "Flu") {
			this.setSpeed(5);
			this.setAnnoyance(90);
		}
	}

	/**
	 * Apply treatment, fixing disease
	 * @param med
	 */
	public void applyTreatment(Medicine med) {
		if (isDiseased()) {
			this.subtractHealth(med.getPotency());
		}
	}
	
	/**
	 * Return the attack amount of a peon
	 */
	public int getAttack() {
		return (int) ((this.getStrength() / 2) + getFightSkill());
	}

	/**
	 * collapsed is a state where the peon cannot take any actions a collapsed
	 * peon requires the assistance of a gatherer peon to be taken to a hospital
	 * to be revived
	 * 
	 * when this is set to 'true', a task to assist the collapsed peon is
	 * automatically generated
	 * 
	 * @param state
	 *            set collapsed to true or false
	 */
	public void setCollapsed(boolean state) {
		setCollapse(state);
		if (getCollapse() == true) {
			this.getParentWorld().addTask(
					new PeonPiggyBack(new Peon(16, 16, 0, 0, "tempPeon"), this,
							getParentWorld().getPathfinder()));
		}
	}

	@Override
	public Boolean isDead() {
		return (getHealth() <= 0);
	}

	@Override
	public void subtractHealth(int factor) {
		this.setHealth(this.getHealth() - factor);
		this.damagedFlag = true;
		if (this.isDead() && this.deadFlag == false) {
			PeonDeathTracker.peonDied();
			this.deadFlag = true;
		} else {
			if (!(this.deadFlag) && this.damagedFlag && (!this.attackingFlag)) {
				PeonDamagedTracker.peonDamaged();
				this.damagedFlag = false;
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;

	}

	/**
	 * Changes the current animation to the walkingLeft animation and records
	 * which way the peon should face when they stop walking.
	 */
	public void animateLeft() {
		this.movingDirection = -1;
		this.animation.set(0, walkLeft);
		this.animation.get(0).start();
	}

	/**
	 * Changes the current animation to the walkingLeft animation and records
	 * which way the peon should face when they stop walking.
	 */
	public void animateRight() {
		this.movingDirection = 1;
		this.animation.set(0, walkRight);
		this.animation.get(0).start();
	}

	/**
	 * Changes the peon animation to the idle standing animation. Depending on
	 * which way the peon is facing the corresponding animation is played
	 */
	public void standStill() {
		if (movingDirection == 1) {
			this.animation.set(0, standRight);
			this.animation.get(0).start();
		}
		if (movingDirection == -1) {
			this.animation.set(0, standLeft);
			this.animation.get(0).start();
		}
	}

	/**
	 * Used when we want to make a peon known it is mining so that the correct
	 * animations can be played.
	 * 
	 * @param if true the peon will play mining animation if false will play
	 *        standing animation;
	 */
	public void isMining(boolean mining) {
		this.isMining = mining;
		if (isMining) {
			if (movingDirection == 1) {
				this.animation.set(0, miningRight);
				this.animation.get(0).start();
			}
			if (movingDirection == -1) {
				this.animation.set(0, miningLeft);
				this.animation.get(0).start();
			}
			PeonMineTracker.setMine();
		} else {
			PeonMineTracker.mineDone();
			if (movingDirection == 1) {
				this.animation.set(0, standRightPick);
				this.animation.get(0).start();
			}
			if (movingDirection == -1) {
				this.animation.set(0, standLeftPick);
				this.animation.get(0).start();
			}
		}
	}

	/**
	 * Simply returns the boolean isMinning
	 * 
	 * @return isMinning
	 */
	public boolean getMinning() {
		return this.isMining;
	}

	/**
	 * Used when we want to make a peon known it is digging so that the correct
	 * animations can be played.
	 * 
	 * @param if true the peon will play digging animation if false will play
	 *        standing animation;
	 */
	public void isDigging(boolean dig) {
		this.isDigging = dig;
		if (isDigging) {
			if (movingDirection == 1) {
				this.animation.set(0, diggingRight);
				this.animation.get(0).start();
				PeonDigTracker.setDig();
			}
			if (movingDirection == -1) {
				this.animation.set(0, diggingLeft);
				this.animation.get(0).start();
				PeonDigTracker.setDig();
			}
		} else {
			PeonDigTracker.digDone();
			standStill();
		}

	}

	/**
	 * Used when we want to make a peon know it is climbing so that the correct
	 * animation can be played
	 * @param climb, if true play climbing animation
	 */
	public void isClimbing(boolean climb) {
		this.isClimbing = climb;
		if (isClimbing) {
			if (movingDirection == 1) {
				this.animation.set(0, climbing);
				this.animation.get(0).start();
			}
			if (movingDirection == -1) {
				this.animation.set(0, climbing);
				this.animation.get(0).start();
			}
		} else {
			standStill();
		}

	}

	/**
	 * Return building bonus
	 */
	public int getBuildingBonus() {
		return buildingBonus;
	}

	/**
	 * Add building bonus factor to stat
	 * @param factor
	 */
	public void addBuildingBonusStack(int factor) {
		buildingBonus += factor;
	}

	/**
	 * This implements the buff on the peon depending on the building
	 *
	 * @param buildingtype
	 *            to implement the correct building
	 */
	public void addBuildingBuff(int buildingtype) {
		buildingBonusArray[buildingtype] = 1;

		buildingBonusArrayTimer[buildingtype] = 6000;
	}

	/**
	 * Keep checking the timer is above 0. if i have a 1 in the buff array and
	 * the timer is 0 then remove the buff and increase building buffs available
	 * by 1
	 */
	public void checkBuildingBuffTimer() {

		for (int i = 0; i < buildingBonusArray.length; i++) {
			if (buildingBonusArray[i] == 1) {

				if (buildingBonusArrayTimer[i] == 0) {
					buildingBonusArray[i] = 0;
					addBuildingBonusStack(1);
				}
			}

		}
	}

	/**
	 * Tick through all timers in the buff array
	 */
	public void tickBuildingBuffs() {

		for (int i = 0; i < buildingBonusArrayTimer.length; i++) {

			if (buildingBonusArrayTimer[i] > 0) {
				buildingBonusArrayTimer[i] -= 1;
			}
		}

		checkBuildingBuffTimer();

	}

	/**
	 * Return the building bonus array
	 */
	public int[] getBuildingBonusArray() {
		return buildingBonusArray;
	}

	/**
	 * Return the bonus array timer
	 */
	public int[] getBuildingBonusArrayTimer() {
		return buildingBonusArrayTimer;
	}

	/**
	 * Function to check if Peon is eligible for level up
	 *
	 * @require currentLevel != 100 && currentLevel > 0
	 * @ensure currentExperience >= 0 level > 0 && level <= 100
	 */
	public void levelUp(int currentExperience) {
		int experienceBuffer = 0;
		if (currentExperience > experienceRequired()) {
			experienceBuffer = currentExperience - experienceRequired();
		}
		setLevel(getLevel() + 1);
		NotificationManager.notify(getName() + " leveled up! - lvl: "
				+ getLevel());
		this.notificationList.add(getName() + " leveled up! - lvl: "
				+ getLevel());
		setExperience(0);
		addExperience(experienceBuffer);
		setTiredness(0);
		setAnnoyance(0);
		setHealth(100);

		addStrength(1);
		addSpeed(1);
		addLuck(1);
	}

	/**
	 * Returns and removes the next task to be executed
	 * 
	 * @return Task nextTask
	 */
	public Optional<Task> getNextTask() {
		if (taskList.peek() == null) {
			return Optional.<Task> empty();
		} else {
			return Optional.of(taskList.remove());
		}
	}

	/**
	 * Returns the next task without removing it
	 * 
	 * @return Task nextTask
	 */
	public Optional<Task> lookNextTask() {
		if (taskList.peek() == null) {
			return Optional.<Task> empty();
		} else {
			return Optional.of(taskList.peek());
		}
	}

	/**
	 * clears the queue of tasks
	 */
	public void clearTasks() {
		while (taskList.peek() != null) {
			taskList.remove();
		}
	}

	/**
	 * adds the specified task to the queue (added task will be executed before
	 * pre-existing tasks)
	 * 
	 * @param newTask
	 */
	public void addTask(Task newTask) {
		taskList.addFirst(newTask);
	}

	/**
	 * Make the Peon Jump
	 */
	public void makePeonJump() {
		if (!jump.isPresent()) {
			PeonJump newTask = new PeonJump(this, (int) this.getXpos() + 30,
					(int) this.getYpos() - 100);
			this.setEntityGravity(false);
			updateJump(Optional.of(newTask));
			strOfTask = "Jumping";
		}
	}

	/**
	 * update the jump
	 *
	 * @param newJump
	 *            - the different of the jump
	 */
	public void updateJump(Optional<Task> newJump) {
		this.jump = newJump;
	}

	/**
	 *
	 * @return the class the peon is - Miner by default
	 */
	public String getPeonClass() {
		return "Miner";
	}

	/**
	 * Drops an item into the world based on the peons coordinates.
	 * 
	 * @param item
	 *            the item being dropped into the world
	 * @param removeItem
	 *            is whether the item should be removed from the peon inventory
	 *            (This is false when an item is trying to be added to a full
	 *            inventory)
	 */
	public void dropItem(Item item, boolean removeItem) {
		if (removeItem) {
			this.getInventory().removeItem(item);
		}
		item.setXpos(this.getXpos());
		item.setYpos(this.getYpos());
		World.getInstance().addEntityToWorld(item);
	}

	public void pickUpItem(Item item) {
		this.peonInventory.addItem(item);
		item.deleteItem();
	}

	/**
	 * Returns a list of all notifications applied to this peon instance
	 * 
	 * @return notificationList
	 * @author JamesG
	 */
	public List<String> getNotificationList() {
		return this.notificationList;
	}

	/**
	 * Gets the string array of helpful peon 'thoughts' from this instance
	 *
	 * @return thoughtsList
	 * @authour JamesG
	 */
	public List<String> getThoughtsList() {
		return this.thoughtsList;
	}

	public String getStrOfTask() {
		return strOfTask;
	}

	/**
	 * Activate Peon Escape Rope Teleports this Peon to a safe location above
	 * ground
	 */
	public void escapeRope() {
		PeonEscapeRope newTask = new PeonEscapeRope(this, 90, 300);
		newTask.doTask();
	}

    /** 
     * Custom AI function, allows to add AI to onTick through subclasses
     */
    public void customAIFuntion() {
        if (this.getCurrentTask().isPresent()) {
            return;
        } else {
            Optional<?> optionalMob = this.getParentWorld().getNearest(Mob.class, this);
            if (optionalMob.isPresent()) {
                WorldEntity enemy = (WorldEntity) optionalMob.get();
                int enemyXPOS = ((Mob) enemy).getXpos();
                int myXPOS = this.getXpos();
                if (Math.abs(myXPOS - enemyXPOS) < 40) {
                    Task newtask = new RunAway(this);
                    this.updateTask(Optional.of(newtask));
                    newtask.switchActiveFlag();
                }
            }
        }
    }

    /**
     * Return a random number within the range of min and max
     * 
     * @param min value of random number
     * @param max value of random number
     * @return randomnumber
     */
	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * Get direction of peon
	 * @return
	 */
	public int getDirection() {
		return this.movingDirection;
	}

}
