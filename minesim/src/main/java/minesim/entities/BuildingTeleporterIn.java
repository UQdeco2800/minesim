package minesim.entities;

import java.util.ArrayList;

import javafx.scene.image.Image;
import minesim.buffs.TeleporterSickness;

/**
 * The TeleporterIn that extends the building abstract class, it should hold all
 * the specialty functionality for this type
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class BuildingTeleporterIn extends Building {

	private static int buildingtype = 5;
	// Images from sprite sheet for all animations
	protected ArrayList<Image> buildingPhase1 = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase2 = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase3 = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase4 = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase5 = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase1c = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase2c = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase3c = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase4c = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase5c = new ArrayList<Image>();
	// Animations
	protected Animation buildingAnimatePhase1;
	protected Animation buildingAnimatePhase2;
	protected Animation buildingAnimatePhase3;
	protected Animation buildingAnimatePhase4;
	protected Animation buildingAnimatePhase5;
	protected Animation buildingAnimatePhase1c;
	protected Animation buildingAnimatePhase2c;
	protected Animation buildingAnimatePhase3c;
	protected Animation buildingAnimatePhase4c;
	protected Animation buildingAnimatePhase5c;

	/* Does the exit teleporter exist and its coords */
	boolean doesExitExist = false;
	private int exitx;
	private int exity;
	/* cooldown until can be used again */
	private int cooldown = 0;

	/**
	 * Generate a TeleporterIn Class based off the Building class. Make sure the
	 * Sprite sheet is correct, with the size width and height relevant to one
	 * spot on the sheet
	 *
	 * @param xpos
	 *            the x coordinate used in generation
	 * @param ypos
	 *            the y coordinate used in generation
	 */
	public BuildingTeleporterIn(int xpos, int ypos) {
		super(xpos, ypos, 96, 54, buildingtype);

		buildingSheet.loadSpriteSheet("BuildingImages/TeleporterSprite3", 54, 96);
		constructionSheet.loadSpriteSheet("BuildingImages/ConstructionSprite", 128, 108);

		setName("Teleporter In");
		setDesc("In");
		setUpAnimation();
		setAnimation(animation);
		constructed = true;

	}

	/**
	 * Build the sprite sheet. Locate the block in the sprite sheet that is
	 * relevant and add them in order of animation getSprite(x, y)
	 */
	private void setUpAnimation() {

		buildingScaf.add(constructionSheet.getSprite(0, 0));

		for (int i = 0; i < 4; i++) {
			buildingPhase1.add(buildingSheet.getSprite(i, 0));
			buildingPhase2.add(buildingSheet.getSprite(i, 2));
			buildingPhase3.add(buildingSheet.getSprite(i, 4));
			buildingPhase4.add(buildingSheet.getSprite(i, 6));
			buildingPhase5.add(buildingSheet.getSprite(i, 8));
			buildingPhase1c.add(buildingSheet.getSprite(i, 1));
			buildingPhase2c.add(buildingSheet.getSprite(i, 3));
			buildingPhase3c.add(buildingSheet.getSprite(i, 5));
			buildingPhase4c.add(buildingSheet.getSprite(i, 7));
			buildingPhase5c.add(buildingSheet.getSprite(i, 9));
		}

		for (int i = 2; i > 0; i--) {
			buildingPhase1.add(buildingSheet.getSprite(i, 0));
			buildingPhase2.add(buildingSheet.getSprite(i, 2));
			buildingPhase3.add(buildingSheet.getSprite(i, 4));
			buildingPhase4.add(buildingSheet.getSprite(i, 6));
			buildingPhase5.add(buildingSheet.getSprite(i, 8));
			buildingPhase1c.add(buildingSheet.getSprite(i, 1));
			buildingPhase2c.add(buildingSheet.getSprite(i, 3));
			buildingPhase3c.add(buildingSheet.getSprite(i, 5));
			buildingPhase4c.add(buildingSheet.getSprite(i, 7));
			buildingPhase5c.add(buildingSheet.getSprite(i, 9));
		}

		buildingConstructing = new Animation(buildingScaf, 40);

		buildingAnimatePhase1 = new Animation(buildingPhase1, 50);
		buildingAnimatePhase2 = new Animation(buildingPhase2, 50);
		buildingAnimatePhase3 = new Animation(buildingPhase3, 50);
		buildingAnimatePhase4 = new Animation(buildingPhase4, 50);
		buildingAnimatePhase5 = new Animation(buildingPhase5, 50);
		buildingAnimatePhase1c = new Animation(buildingPhase1c, 50);
		buildingAnimatePhase2c = new Animation(buildingPhase2c, 50);
		buildingAnimatePhase3c = new Animation(buildingPhase3c, 50);
		buildingAnimatePhase4c = new Animation(buildingPhase4c, 50);
		buildingAnimatePhase5c = new Animation(buildingPhase5c, 50);

		animation.add(buildingAnimatePhase1);
		animation.get(0).start();
	}

	/**
	 * Run the animations making sure to check if the health is below 100 and
	 * animating a damaged building instead. also check the cooldowns.
	 */
	@Override
	public void onTick() {
		super.onTick();
		Animation tempAnimation;
		if (this.health > 80) {
			tempAnimation = buildingAnimatePhase1;
		} else if (this.health > 60) {
			tempAnimation = buildingAnimatePhase2;
		} else if (this.health > 40) {
			tempAnimation = buildingAnimatePhase3;
		} else if (this.health > 20) {
			tempAnimation = buildingAnimatePhase4;
		} else {
			tempAnimation = buildingAnimatePhase5;
		}

		if (cooldown != 0) {

			cooldown -= 1;
			if (this.health > 80) {
				tempAnimation = buildingAnimatePhase1c;
			} else if (this.health > 60) {
				tempAnimation = buildingAnimatePhase2c;
			} else if (this.health > 40) {
				tempAnimation = buildingAnimatePhase3c;
			} else if (this.health > 20) {
				tempAnimation = buildingAnimatePhase4c;
			} else {
				tempAnimation = buildingAnimatePhase5c;
			}
		}

		// just put this here.. whatever i change i will have to animate
		animation.set(0, tempAnimation);
		animation.get(0).start();

		if (cooldown < 0) {
			cooldown = 0;
		}

		animation.get(0).update();
		setAnimation(animation);

		// this is super labour intensive :(
		// turning it off.. need a smarter way to run this when the out
		// teleporter dies. cant run it within the Teleporter class
		this.getParentWorld().worldSetUpTeleporter();
	}

	/**
	 * Return false if peon is not next to the building.. indicating he should
	 * keep moving. once this returns true and building has had an effect it
	 * should return True.peon will stop.
	 */
	@Override
	public Boolean interact(WorldEntity sender) {

		if (cooldown != 0) {
			return Boolean.FALSE;
		}

		if (sender instanceof Peon) {

				/* apply debuff */
				sender.addBuff(new TeleporterSickness());

				this.health -= 10;
				sender.setXpos(exitx);
				sender.setYpos(exity);
				cooldown += 250;

				return Boolean.TRUE;
			
		}
		return Boolean.FALSE;
	}

	/**
	 * Sets up the teleporter to be ready for use
	 *
	 * @param x
	 *            the x coordinate of the TeleporterOut
	 * @param y
	 *            the y coordinate of the TeleporterOut
	 */
	public void setExit(int x, int y) {
		exitx = x;
		exity = y;
		doesExitExist = true;
	}

	public void unsetExit() {
		doesExitExist = false;
	}

	public boolean exitExists() {
		return doesExitExist;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int factor) {
		cooldown = factor;
	}

}
