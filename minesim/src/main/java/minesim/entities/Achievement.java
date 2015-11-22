package minesim.entities;

import minesim.World;

public class Achievement extends WorldEntity {
    //xpos and ypos will not change
    private String achievementType;
    private String achievementText;
    private String name;
    private String image;
    private int timer = 0;
    
    /**
     * An Achievement class that is called when the conditions in AchievementDatabaseHandler are met.
     * @param xpos - xposition of achievement banner
     * @param ypos - yposition of achievement banner
     * @param height - height of banner
     * @param width - width of banner
     * @param type - achievement to call
     */
    public Achievement(int xpos, int ypos, int height, int width, String type) {
    	super(xpos, ypos, height, width);
        this.achievementType = type;
        switch (type) {
            case "rock1": //achievement for mining 1 rock
                setName("rock1");
                setString("Well done! You mined 1 rock");
                setImage("/AchievementImages/1rock.png");
                setImageurl("/AchievementImages/1rock.png");
                setEntityGravity(Boolean.FALSE);
                break;

            case "rock20": //achievement for mining 20 rocks
                setName("rock20");
                setString("Well done! You mined 20 rocks");
                setImage("/AchievementImages/20rocks.png");
                setImageurl("/AchievementImages/20rocks.png");
                setEntityGravity(Boolean.FALSE);
                break;

            case "rock50": //achievement for mining 50 rocks
                setName("rock50");
                setString("Well done! You mined 50 rocks");
                setImage("/AchievementImages/50rocks.png");
                setImageurl("/AchievementImages/50rocks.png");
                setEntityGravity(Boolean.FALSE);
                break;
            
            case "zombie1": //achievement for killing 1 zombie
            	setName("zombie1");
                setString("You killed 1 zombie!");
                setImage("/AchievementImages/1zombie.png");
                setImageurl("/AchievementImages/1zombie.png");
                setEntityGravity(Boolean.FALSE);
                break;
                
            case "zombie20": //achievement for killing 20 zombies
            	setName("zombie20");
                setString("You killed 20 zombie!");
                setImage("/AchievementImages/20zombies.png");
                setImageurl("/AchievementImages/20zombies.png");
                setEntityGravity(Boolean.FALSE);
                break;
                
            case "zombie50": //achievement for killing 50 zombies
            	setName("zombie50");
                setString("You killed 50 zombie!");
                setImage("/AchievementImages/50zombies.png");
                setImageurl("/AchievementImages/50zombies.png");
                setEntityGravity(Boolean.FALSE);
                break;
        }
    }
    /**
     * sets the name of the achievement
     * @param aName
     */
    private void setName(String aName) {
        this.name = aName;
    }

    /**
     * returns the achievement type
     * @return
     */
    public String getAchievementType() {
        return this.achievementType;
    }

    /**
     * Sets a new achievement text
     * @param aString
     */
    public void setString(String aString) {
        this.achievementText = aString;
    }
    
    /**
     * Changes the image URL
     * @param imageString - Image URL
     */
    public void setImage(String imageString){
    	this.image = imageString;
    }
    
    /**
     * Returns the achievement image URL
     * @return the current image URL
     */
    public String getImage(){
    	return this.image;
    }
    
    @Override
    public void onTick(){ //removes the achievement from the world in 5 seconds.
    	if(timer == 300){
    		World.getInstance().achievments.remove(this);
    	}
    	timer += 1; 
    }

    public int getRemainingTime(){
        return timer;
    }
}