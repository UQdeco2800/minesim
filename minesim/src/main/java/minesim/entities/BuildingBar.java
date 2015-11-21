package minesim.entities;

import minesim.buffs.Beer;

/**
 * The Bar that extends the building abstract class, it should hold all the
 * specialty functionality for this type
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class BuildingBar extends Building {
	
	static int buildingtype = 2;

	/**
	 * Generate a Bar Class based off the Building class. Make sure the Sprite
	 * sheet is correct, with the size width and height relevant to one spot on
	 * the sheet
	 *
	 * @param xpos
	 *            the x coordinate used in generation
	 * @param ypos
	 *            the y coordinate used in generation
	 */
    
	public BuildingBar(int xpos, int ypos) {
		super(xpos, ypos, 128, 108, buildingtype);

		buildingSheet.loadSpriteSheet("BuildingImages/BarSprite", 128, 108);
		constructionSheet.loadSpriteSheet("BuildingImages/ConstructionSprite", 128, 108);
		
		setName("Bar");
		setDesc("Cause who doesn't need a drink " + "while they're working (+50happy)");
		setUpAnimation();
		setAnimation(animation);
	}

	/**
	 * Build the sprite sheet. Locate the block in the sprite sheet that is
	 * relevant and add them in order of animation
	 */


	private void setUpAnimation() {

		buildingScaf.add(constructionSheet.getSprite(0, 0));
		buildingHealthy.add(buildingSheet.getSprite(0, 0));
		buildingHealthy.add(buildingSheet.getSprite(1, 0));
		buildingDamaged.add(buildingSheet.getSprite(0, 1));
		buildingDamaged.add(buildingSheet.getSprite(1, 1));

		buildingLookGood = new Animation(buildingHealthy, 40);
		buildingLookBad = new Animation(buildingDamaged, 50);
		buildingConstructing = new Animation(buildingScaf, 40);

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
		} else if (this.health > 90) {
			tempAnimation = buildingLookGood;
		} else {
			tempAnimation = buildingLookBad;
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
				sender.addBuff(new Beer());

				this.health -= 10;

				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

}
