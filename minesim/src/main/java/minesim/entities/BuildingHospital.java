package minesim.entities;

/**
 * The Blacksmith that extends the building abstract class, it should hold all the specialty
 * functionality for this type
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class BuildingHospital extends Building {

    private static int buildingtype = 7;

    /**
     * Generate a Blacksmith Class based off the Building class. Make sure the Sprite sheet is
     * correct, with the size width and height relevant to one spot on the sheet
     *
     * @param xpos the x coordinate used in generation
     * @param ypos the y coordinate used in generation
     */
    public BuildingHospital(int xpos, int ypos) {
        super(xpos, ypos, 120, 120, buildingtype);

        buildingSheet.loadSpriteSheet("BuildingImages/HospitalSprite"
                , 400, 400);
        constructionSheet.loadSpriteSheet("BuildingImages/ConstructionSprite",
				128, 108);

        setName("Hospital");
        setDesc("Hospital");
        setUpAnimation();
        setAnimation(animation);
        animation.get(0).start();
    }

    /**
     * Build the sprite sheet. Locate the block in the sprite sheet that is relevant and add them in
     * order of animation
     */
    private void setUpAnimation() {
    	
    	buildingScaf.add(constructionSheet.getSprite(0, 0));

        buildingHealthy.add(buildingSheet.getSprite(0, 0));
        buildingHealthy.add(buildingSheet.getSprite(1, 0));
        buildingDamaged.add(buildingSheet.getSprite(0, 1));
        buildingDamaged.add(buildingSheet.getSprite(1, 1));

        buildingConstructing = new Animation(buildingScaf, 40);
        
        buildingLookGood = new Animation(buildingHealthy, 50);
        buildingLookBad = new Animation(buildingDamaged, 50);

        animation.add(buildingConstructing);

    }

    /**
     * Run the animations making sure to check if the health is below 100 and animating a damaged
     * building instead.
     */
    @Override
    public void onTick() {
        super.onTick();
        Animation tempAnimation;
        
        if (!constructed){
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
}
