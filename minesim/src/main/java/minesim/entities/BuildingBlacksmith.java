package minesim.entities;

/**
 * The Blacksmith that extends the building abstract class, it should hold all the specialty
 * functionality for this type
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public class BuildingBlacksmith extends Building {

    private static int buildingtype = 3;



    /**
     * Generate a Blacksmith Class based off the Building class. Make sure the Sprite sheet is
     * correct, with the size width and height relevant to one spot on the sheet
     *
     * @param xpos the x coordinate used in generation
     * @param ypos the y coordinate used in generation
     */
    public BuildingBlacksmith(int xpos, int ypos) {
        super(xpos, ypos, 108, 123, buildingtype);
        buildingSheet.loadSpriteSheet("BuildingImages/Blacksmith3"
                , 123, 108);
        constructionSheet.loadSpriteSheet("BuildingImages/ConstructionSprite",
				128, 108);

        setName("Blacksmith");
        setDesc("Blacksmith");
        setUpAnimation();
        setAnimation(animation);
        
    }

    /**
     * Build the sprite sheet. Locate the block in the sprite sheet that is relevant and add them in
     * order of animation
     */
    private void setUpAnimation() {
    	buildingScaf.add(constructionSheet.getSprite(0, 0));
        buildingPhase1.add(buildingSheet.getSprite(0, 0));
        buildingPhase1.add(buildingSheet.getSprite(1, 0));
        buildingPhase2.add(buildingSheet.getSprite(0, 1));
        buildingPhase2.add(buildingSheet.getSprite(1, 1));
        buildingPhase3.add(buildingSheet.getSprite(0, 2));
        buildingPhase3.add(buildingSheet.getSprite(1, 2));
        buildingPhase4.add(buildingSheet.getSprite(0, 3));
        buildingPhase4.add(buildingSheet.getSprite(1, 3));
        buildingPhase5.add(buildingSheet.getSprite(0, 4));
        buildingPhase5.add(buildingSheet.getSprite(1, 4));
        buildingPhase6.add(buildingSheet.getSprite(0, 5));
        buildingPhase6.add(buildingSheet.getSprite(1, 5));

        buildingConstructing = new Animation(buildingScaf, 40);
        
        buildingAnimatePhase1 = new Animation(buildingPhase1, 40);
        buildingAnimatePhase2 = new Animation(buildingPhase2, 50);
        buildingAnimatePhase3 = new Animation(buildingPhase3, 50);
        buildingAnimatePhase4 = new Animation(buildingPhase4, 50);
        buildingAnimatePhase5 = new Animation(buildingPhase5, 50);
        buildingAnimatePhase6 = new Animation(buildingPhase6, 50);

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
            tempAnimation = buildingAnimatePhase1;
        } else if (this.health > 80) {
            tempAnimation = buildingAnimatePhase2;
        } else if (this.health > 60) {
            tempAnimation = buildingAnimatePhase3;
        } else if (this.health > 40) {
            tempAnimation = buildingAnimatePhase4;
        } else if (this.health > 20) {
            tempAnimation = buildingAnimatePhase5;
        } else {
            tempAnimation = buildingAnimatePhase6;
        }


        animation.set(0, tempAnimation);
        animation.get(0).start();

        animation.get(0).update();
        setAnimation(animation);
    }
}
