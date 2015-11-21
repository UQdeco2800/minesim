package minesim.entities;

import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * The TeleporterOut that extends the building abstract class, it should hold
 * all the specialty functionality for this type
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class BuildingTeleporterOut extends Building {

	private static int buildingtype = 6;
	// Images from sprite sheet for all animations
	protected ArrayList<Image> buildingPhase1 = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase2 = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase3 = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase4 = new ArrayList<Image>();
	protected ArrayList<Image> buildingPhase5 = new ArrayList<Image>();
	// Animations
	protected Animation buildingAnimatePhase1;
	protected Animation buildingAnimatePhase2;
	protected Animation buildingAnimatePhase3;
	protected Animation buildingAnimatePhase4;
	protected Animation buildingAnimatePhase5;

	/**
	 * Generate a TeleporterOut Class based off the Building class. Make sure
	 * the Sprite sheet is correct, with the size width and height relevant to
	 * one spot on the sheet
	 *
	 * @param xpos
	 *            the x coordinate used in generation
	 * @param ypos
	 *            the y coordinate used in generation
	 */
	public BuildingTeleporterOut(int xpos, int ypos) {
		super(xpos, ypos, 96, 54, buildingtype);

		buildingSheet.loadSpriteSheet("BuildingImages/TeleporterSprite4", 54, 96);
		constructionSheet.loadSpriteSheet("BuildingImages/ConstructionSprite", 128, 108);

		setName("Teleporter Out");
		setDesc("out");
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
			buildingPhase2.add(buildingSheet.getSprite(i, 1));
			buildingPhase3.add(buildingSheet.getSprite(i, 2));
			buildingPhase4.add(buildingSheet.getSprite(i, 3));
			buildingPhase5.add(buildingSheet.getSprite(i, 4));
		}

		for (int i = 2; i > 0; i--) {
			buildingPhase1.add(buildingSheet.getSprite(i, 0));
			buildingPhase2.add(buildingSheet.getSprite(i, 1));
			buildingPhase3.add(buildingSheet.getSprite(i, 2));
			buildingPhase4.add(buildingSheet.getSprite(i, 3));
			buildingPhase5.add(buildingSheet.getSprite(i, 4));
		}

		buildingConstructing = new Animation(buildingScaf, 40);

		buildingAnimatePhase1 = new Animation(buildingPhase1, 50);
		buildingAnimatePhase2 = new Animation(buildingPhase2, 50);
		buildingAnimatePhase3 = new Animation(buildingPhase3, 50);
		buildingAnimatePhase4 = new Animation(buildingPhase4, 50);
		buildingAnimatePhase5 = new Animation(buildingPhase5, 50);

		animation.add(buildingAnimatePhase1);
		animation.get(0).start();
	}

	/**
	 * Run the animations making sure to check if the health is below 100 and
	 * animating a damaged building instead.
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

		// just put this here.. whatever i change i will have to animate
		animation.set(0, tempAnimation);
		animation.get(0).start();

		animation.get(0).update();
		setAnimation(animation);
	}

	@Override
	public Boolean interact(WorldEntity sender) {
		return Boolean.TRUE;
	}

}
