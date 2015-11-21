package minesim.entities;

import java.util.ArrayList;

import minesim.entities.items.properties.HasHealth;
import javafx.scene.image.Image;

/**
 * Abstract Class that extends the World Entity, it should hold all the essential building functions
 * that are the same to all buildings.
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public abstract class Building extends WorldEntity
        implements HasHealth, HasName {

    /* building information */
    protected String name = "";
    protected String desc = "";

    /* treat health as a % please */
    protected int health = 100;
    protected Boolean dead = Boolean.FALSE;
    protected int buildingtype;

    /* Peon Sprite Sheet */
    protected SpriteLoader buildingSheet = new SpriteLoader();

    /* Images from sprite sheet for all animations */
    protected ArrayList<Image> buildingHealthy = new ArrayList<Image>();
    protected ArrayList<Image> buildingDamaged = new ArrayList<Image>();
    /*Images for damage level sprites*/
    protected ArrayList<Image> buildingPhase1 = new ArrayList<Image>();
    protected ArrayList<Image> buildingPhase2 = new ArrayList<Image>();
    protected ArrayList<Image> buildingPhase3 = new ArrayList<Image>();
    protected ArrayList<Image> buildingPhase4 = new ArrayList<Image>();
    protected ArrayList<Image> buildingPhase5 = new ArrayList<Image>();
    protected ArrayList<Image> buildingPhase6 = new ArrayList<Image>();
    // Animations
    protected Animation buildingAnimatePhase1;
    protected Animation buildingAnimatePhase2;
    protected Animation buildingAnimatePhase3;
    protected Animation buildingAnimatePhase4;
    protected Animation buildingAnimatePhase5;
    protected Animation buildingAnimatePhase6;
    protected Animation buildingLookGood;
    protected Animation buildingLookBad;
    protected ArrayList<Animation> animation = new ArrayList<Animation>();
    /* Construction */
    protected Boolean constructed = Boolean.FALSE;
    protected ArrayList<Image> buildingScaf = new ArrayList<Image>();
    protected Animation buildingConstructing;
    protected SpriteLoader constructionSheet = new SpriteLoader();
    
    /**
     * Generate a Building Class based off world Entity
     *
     * @param xpos   the x coordinate used in generation
     * @param ypos   the y coordinate used in generation
     * @param height the vertical pixel size of this entity
     * @param width  the horizontal pixel size of this entity
     * @param type   the building type
     */
    public Building(int xpos, int ypos, int height, int width, int type) {
        super(xpos, ypos, height, width);
        this.buildingtype = type;
    }

    /**
     * Interact checks to see if the sender is a peonand issues a cost of -10hp 
     * to this building for its use
     *
     * @param sender the entity calling this method
     * @return True or False based on whether the task succeeded
     */
    @Override
    public Boolean interact(WorldEntity sender) {

        if (sender instanceof Peon) {
        	if (!constructed){
        		constructed = Boolean.TRUE;
        		return Boolean.TRUE;
        	}else{

                this.health -= 10;

                return Boolean.TRUE;
        	}
            
        }
        return Boolean.FALSE;
    }

    /**
     * Check if the building is dead on each tick
     */
    @Override
    public void onTick() {
        super.onTick();

        if (health <= 0) {
            this.alive = Boolean.FALSE;
            this.dead = Boolean.TRUE;
        }

    }

    public int getBuildingType() {
        return this.buildingtype;
    }

    public void animateBuildingLookGood() {
        //animation = buildingLookGood;
        //animation.start();
    }

    public void animateBuildingLookBad() {
        //animation = buildingLookBad;
        //animation.start();
    }
    
    public void animateBuildingConstruction() {
       // animation = buildingConstructing;
        //animation.start();
    }

    /* has health interface */
    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Boolean isDead() {
        return this.dead;
    }

    public void subtractHealth(int factor) {
        this.health -= factor;
    }

    public void addHealth(int factor) {
        this.health += factor;
    }
    /* has health interface */

    /* has name interface */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /* has name interface */

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String name) {
        this.desc = name;
    }

    public void sellBuilding() {
        this.alive = Boolean.FALSE;
        this.dead = Boolean.TRUE;
    }
    
    public Boolean isConstructed() {
        return this.constructed;
    }
}
