package minesim.entities;

import javafx.scene.paint.Color;
import minesim.World;

public class Hadouken extends WorldEntity {

    private int expressionDuration;
    private Boolean fadeOut;
    private float alphaValue;
    private int xpos;
    private int ypos;
    private int dir;

    /**
     *
     * @param xpos
     * @param ypos
     * @param height
     * @param width
     * @param duration
     * @see 	image
     */
    public Hadouken(int xpos, int ypos, int height, int width, int duration, int direction) {
        super(xpos, ypos, height, width);
        this.xpos = xpos;
        this.ypos = ypos;
        this.dir = direction;
        if (dir == -1) {
        	setImageurl("/hadoukenL.png");
        } else {
        	setImageurl("/hadouken.png");
        }
        expressionDuration = duration;
        alphaValue = 1;
        fadeOut = Boolean.FALSE;
        setEntityGravity(Boolean.FALSE);
        addCollisionIgnoredClass(Peon.class);
        addCollisionIgnoredClass(Grave.class);
    }
    /**
     * Moves the Projectile entity to the left or right of 
     * the screen depending on the direction of the Peon sender
     * <p>
     * Detects collision with Zombie mob entity, calls the interact 
     * function to deal damage if collision occurs
     * 
     * @see 	Image
     */
    public void onTick() {
        super.onTick();
        //this.ypos--;
        if (dir == -1) {
        	this.moveTowards(xpos - 1000, ypos, 5);
        } else {
        	this.moveTowards(xpos + 1000, ypos, 5);
        }
        for (WorldEntity entity : World.getInstance().getWorldentities()) {
        	if (entity instanceof ZombieMob 
        			&& (entity.getXpos() > this.xpos - 40 
        			&& entity.getXpos() < this.xpos + 40
        			&& entity.getYpos() < this.ypos + 50
        			&& entity.getYpos() > this.ypos - 50)) {
        		entity.interact(this);
        	}
        }
        if (expressionDuration == 0) {
            fadeOut = Boolean.TRUE;
        }
        if (fadeOut == Boolean.TRUE && alphaValue >= 0) {
            this.setEntityColor(Color.rgb(0, 0, 0, alphaValue));
            alphaValue -= 0.01;
        }
        if (alphaValue <= 0.1) {
            this.alive = Boolean.FALSE;
        }
        expressionDuration--;
    }

}
