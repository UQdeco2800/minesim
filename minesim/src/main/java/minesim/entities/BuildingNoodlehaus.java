package minesim.entities;

import java.util.ArrayList;

import javafx.scene.image.Image;
import minesim.buffs.Noodles;

/**
 * The Noodlehaus that extends the building abstract class, it should hold all
 * the specialty functionality for this type
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class BuildingNoodlehaus extends Building {

	private static int buildingtype = 4;

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
	 * Generate a Noodlehaus Class based off the Building class. Make sure the
	 * Sprite sheet is correct, with the size width and height relevant to one
	 * spot on the sheet
	 *
	 * @param xpos
	 *            the x coordinate used in generation
	 * @param ypos
	 *            the y coordinate used in generation
	 */
	public BuildingNoodlehaus(int xpos, int ypos) {
		super(xpos, ypos, 128, 128, buildingtype);

		buildingSheet.loadSpriteSheet("BuildingImages/NoodlehausSprite2", 128, 128);
		constructionSheet.loadSpriteSheet("BuildingImages/ConstructionSprite", 128, 108);

		setName("Noodle House");
		setDesc("To expand one's waist and hp");
		setUpAnimation();
		setAnimation(animation);
	}

	/**
	 * Build the sprite sheet. Locate the block in the sprite sheet that is
	 * relevant and add them in order of animation
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

		buildingConstructing = new Animation(buildingScaf, 40);

		buildingAnimatePhase1 = new Animation(buildingPhase1, 50);
		buildingAnimatePhase2 = new Animation(buildingPhase2, 50);
		buildingAnimatePhase3 = new Animation(buildingPhase3, 50);
		buildingAnimatePhase4 = new Animation(buildingPhase4, 50);
		buildingAnimatePhase5 = new Animation(buildingPhase5, 50);

		animation.add(buildingConstructing);
	}

	/**
	 * Run the animations making sure to check if the health is below 100 and
	 * animating a damaged building instead.
	 */
	@Override
	public void onTick() {
		super.onTick();

		Animation tempAnimation;
		if (!constructed) {
			tempAnimation = buildingConstructing;
		} else if (this.health > 80) {
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

		animation.set(0, tempAnimation);
		animation.get(0).start();

		animation.get(0).update();
		setAnimation(animation);
	}

	/**
	 * Interact checks to see if the peon is nearby and issues a cost of -10hp
	 * to this building for its use
	 *
	 * @param sender
	 *            the entity calling this method
	 * @return True or False based on whether the task succeeded
	 */
	@Override
	public Boolean interact(WorldEntity sender) {

		if (sender instanceof Peon) {
			if (!constructed) {
				constructed = Boolean.TRUE;
				return Boolean.TRUE;
			} else {
				// apply buff
				sender.addBuff(new Noodles());

				this.health -= 10;

				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
