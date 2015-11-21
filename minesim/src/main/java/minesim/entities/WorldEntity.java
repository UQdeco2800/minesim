package minesim.entities;

import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import minesim.World;
import minesim.inputhandlers.MouseHandler;
import minesim.tasks.ClimbTransportation;
import minesim.tasks.Task;
import minesim.buffs.*;
import minesim.entities.items.Transportation;


public class WorldEntity {

    // Position (height) up the world
    public int ypos = 0;
    // Geometric sizes
    public int height = 0;
    public int width = 0;
    public Optional<String> image = Optional.empty();
    public ArrayList<Image> animation = new ArrayList<Image>();
    public Boolean alive = Boolean.TRUE;
    int gravity; //The speed for the entity to fall, the default number is 0
    int currentGravity;
    private Logger logger = Logger.getLogger(WorldEntity.class);
    // Position along the world
    private int xpos = 0;
    private ConcurrentLinkedDeque<Class> collisionIgnored = new ConcurrentLinkedDeque<Class>();
    private Color objectcolor = Color.BLACK;
    private Boolean shouldhavegravity = Boolean.TRUE;
    private Optional<Task> currentTask = Optional.empty();
    private ArrayList<Buff> buffList = new ArrayList<Buff>();
    private Boolean animationPresent = false;


    /**
     * Constructor for WorldEntity
     *
     * @param xpos   xpos
     * @param ypos   xpos
     * @param height height of object
     * @param width  width of object
     */
    public WorldEntity(int xpos, int ypos, int height, int width) {
        this.setXpos(xpos);
        this.ypos = ypos;
        this.height = height;
        this.width = width;
        this.gravity = 0;
        this.currentGravity = this.gravity;
    }

    /**
     * Returns the colour of this entity
     *
     * @return Color
     */
    public Color getEntityColor() {
        return this.objectcolor;
    }


    public void setEntityColor(Color color) {
        this.objectcolor = color;
    }

    /**
     * Set the graphics image of this entity
     *
     * @param image Image object (for caching)
     */
    public void setImageurl(String image) {
        this.image = Optional.of(image);
    }

    /**
     * Sets the sprite animation for this entity
     *
     * @param animation the animation where we are getting the specific sprite from
     */
    public void setAnimation(ArrayList<Animation> animationRecieved) {
    	for(int i = 0; i < animationRecieved.size(); ++i){
    		this.animation.clear();
    		this.animation.add(animationRecieved.get(i).getCurrentSprite());
    	}
    	this.animationPresent = true;
    }
    /**
     * Checks if an animation is present
     * 
     * @return The animationPresent Boolean
     */
    public boolean animationIsPresent(){
    	return this.animationPresent;
    }
    
    

    /**
     * Checks if the coordinate given is contained within the boundries of this object
     *
     * @param x xcoord
     * @param y ycoord
     * @return true if coordinate within this entity, false otherwise
     */
    public Boolean containsCoordinate(double x, double y) {
        if (x >= this.getXpos() && x < this.getXpos() + width && y >= this.ypos && y < this.ypos + height) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public int getXpos() {
        return xpos;
    }

    /**
     * @param xpos the xpos to set
     */
    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    /**
     * @param ypos the xpos to set
     */
    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    /**
     * Returns the parent world
     *
     * @return World parentworld
     * @deprecated
     */
    public World getParentWorld() {
        return World.getInstance();
    }

    /**
     * get the gravity of the entity
     */
    public int getGravity() {
        return this.gravity;
    }

    /**
     * set the gravity of the entity the default gravty is 0 if the grave increas, the fail speed of
     * entity will become slower for example if the gravity is 8, the gravity fall 1 every second
     *
     * @param gravity - the gravity you want set to the entity
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
        currentGravity = gravity;
    }

    /**
     * A mouse click occurred on this entity
     */
    public void notifyOfClickActionOnEntity(MouseHandler sender) {
    }

    /**
     * irements This object registered to be notified about the last click event and this is the
     * click event returning
     */
    public void notifyOfRegisteredClickAction(MouseHandler sender, MouseEvent event, int actionMode) {
    }
    /**
     * For drag event
     **/
    public void notifyOfRegisteredClickAction(int startX, int startY, MouseHandler sender,
                                              MouseEvent event, int actionMode) {
    }

    /**
     * Enable or disables gravity
     *
     * @param gravity true or false
     */
    public void setEntityGravity(Boolean gravity) {
        this.shouldhavegravity = gravity;
    }
    
    public ArrayList<Buff> getBuffs() {
    	return this.buffList;
    }
    
    public boolean hasBuff() {
    	if (this.buffList.size() == 0) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    public void addBuff(Buff b) {
    	this.buffList.add(b);
    }
    
    public boolean removeBuffByName(Buff b) {
    	if (this.buffList.contains(b)) {
    		this.buffList.remove(b);
    		return true;
    	}
    	return false;
    }
    
    public boolean removeBuffByType(Class<?> b) {
    	
    	for(Buff c : this.buffList) {
    		if (c.getClass().equals(b)) {
    			this.buffList.remove(c);
    			return true;
    		}
    	}
    	
    	return false;
    }

    /**
     * Called every game tick
     */
    public void onTick() {
    	
    	ArrayList<Buff> buffstoremove = new ArrayList<Buff>();
    	for (Buff e: this.buffList) {
        	e.buffTick(this);

        	if (e.getCooldown() <= 0) {
        		buffstoremove.add(e);
//        		System.out.println("Buff gone");
//        		image goes aswell.. quite nice!
        	}
        }
    	
    	//Remove buffs
    	for (Buff e: buffstoremove) {
    		this.buffList.remove(e);
    	}
    	
        // What should this entity do on a game tick       
        for (int i = 0; i < 25; i++) {
            if (shouldhavegravity) {
                if ((getParentWorld().getEntityForCoordinates(this.getXpos() + width / 2, this.ypos + height).isPresent())
                        && (!(collisionIgnored.contains(getParentWorld().getEntityForCoordinates(this.getXpos() + width / 2, this.ypos + height).get().getClass())))) {
                    setGravity(0);//if fall to the ground,reset the gravity
                    return;
                } else if (!World.getInstance().getTileManager().shouldEntityCollideAtPos(this, this.xpos, this.ypos + 1)) {
                    if (currentGravity > 0) {
                        currentGravity--;
                    } else {
                        this.ypos += 1;
                        currentGravity = gravity;
                    }
                } else {
                    return;
                }
            }
        }
        
    }

    /**
     * A simple moving alg with very basic (but long) collision detection
     *
     * @param waypoint  the xpos to travel to
     * @param waypointY the ypos to travel to
     * @param speed     the distance per ticks
     * @return true if move was valid, false otherwise
     */
    public boolean moveTowards(int waypoint, int waypointY, int speed) {
        for (int i = 0; i < speed; i++) {
            WorldEntity collision = null;
            CollisionMode mode = CollisionMode.NONE;

            // Tile collision checking
            if (World.getInstance().getTileManager().shouldEntityCollideAtPos(this, xpos - 1, ypos - 5)) {
                if (!World.getInstance().getTileManager().shouldEntityCollideAtPos(this, xpos - 1, ypos - 22)) {
                    ypos = ypos - 16;
                } else {
                    mode = CollisionMode.LEFT_COLLIDE_TILE;
                }
            } else if (World.getInstance().getTileManager().shouldEntityCollideAtPos(this, xpos + 1, ypos - 5)) {
                if (!World.getInstance().getTileManager().shouldEntityCollideAtPos(this, xpos + 1, ypos - 22)) {
                    ypos = ypos - 16;
                } else {
                    mode = CollisionMode.RIGHT_COLLIDE_TILE;
                }
            }
            // Entity collision checking
            if (getParentWorld().getEntityForCoordinates(xpos, ypos + height + 2).isPresent()) {
                collision = getParentWorld().getEntityForCoordinates(xpos, ypos + height + 2).get();
                if (collision instanceof Transportation) {
                    mode = CollisionMode.BOTTOM_COLLIDE;
                }
            } else if (getParentWorld().getEntityForCoordinates(xpos - 1, ypos + height - 1).isPresent()) {
                collision = getParentWorld().getEntityForCoordinates(xpos - 1, ypos + height - 1).get();
                mode = CollisionMode.LEFT_COLLIDE;
            } else if (getParentWorld().getEntityForCoordinates(getXpos() + width + 1, ypos + height - 1).isPresent()) {
                collision = getParentWorld().getEntityForCoordinates(getXpos() + width + 1, ypos + height - 1).get();
                mode = CollisionMode.RIGHT_COLLIDE;
            }
            
            //The destination waypoint is left of the peon's current position
            if (waypoint < this.getXpos()) {
            	if (mode != CollisionMode.LEFT_COLLIDE_TILE) {
            		/*If the peon is colliding with a rope/ladder/elevator, and the collision 
                		is not ignored, then engage. Collisions are ignored during climbing only.*/
            		if (collision instanceof Transportation
            				&& !(collisionIgnored.contains(Transportation.class))) {
            			//zombies seem to throw around lots of errors when they hit ladders, so ignore them
            			if (this.getClass().equals(ZombieMob.class)){
            				this.xpos-=40;
            				return false;
            			}
            			/*If your destination point is on the same level, don't climb the ladder
                    		will need to fix this later so you can walk through to a tunnel on the other side*/
            			if (waypointY >= this.getYpos() && waypointY <= (this.getYpos()+31)){
            				addCollisionIgnoredClass(Transportation.class);
            				this.xpos--;
            			}
            			/* The peon needs to climb to reach its destination, so climb! Also store the current 
            			 * WalkTowards task so it can be resumed after climbing. */
            			else {
            				addCollisionIgnoredClass(Transportation.class);
            				shouldhavegravity = Boolean.FALSE;
            			}
            			return true;
            		} 
            		//Otherwise, simply move the peon left of their current position
            		else if (mode != CollisionMode.LEFT_COLLIDE || collisionIgnored.contains(collision.getClass())) {
            			this.xpos--;
            		} else {
            			return false;
            		}
            	} else {
            		return false;
            	}
            } 
            //The destination waypoint is right of the peon's current position
            else if (waypoint > this.xpos) {
            	if (mode != CollisionMode.RIGHT_COLLIDE_TILE) {
            		if (collision instanceof Transportation
            				&& !(collisionIgnored.contains(Transportation.class))) {
            			if (this.getClass().equals(ZombieMob.class)){
            				this.xpos+=40;
            				return false;
            			}
            			if (waypointY >= this.getYpos() && waypointY <= (this.getYpos()+31)){
            				addCollisionIgnoredClass(Transportation.class);
            				this.xpos++;
            			}
            			else {
            				addCollisionIgnoredClass(Transportation.class);
            				shouldhavegravity = Boolean.FALSE;
            			}
            			return true;
            		} else if (mode != CollisionMode.RIGHT_COLLIDE || collisionIgnored.contains(collision.getClass())) {
            			this.xpos++;
            		} else {
            			return false;
            		}
            	} else {
            		return false;
            	}
            } else {
            	return false;
            }
        }
        return true;
    }

    /**
     * Allows peons to move upwards or downwards based on a waypoint and given speed
     *
     * @param waypoint The ypos for the peon to travel to
     * @param speed    The movement distance per ticks
     * @return true Return true if move was valid, false otherwise
     */
    public boolean moveVertical(int waypoint, int speed) {
        for (int i = 0; i < speed; i++) {
            WorldEntity collision = null;
            CollisionMode mode = CollisionMode.NONE;

            // Tile collision checking
            if (World.getInstance().getTileManager().shouldEntityCollideAtPos(this, xpos, ypos - 1)) {
                mode = CollisionMode.UP_COLLIDE_TITLE;
                System.out.println("upcollision");
            } else if (World.getInstance().getTileManager().shouldEntityCollideAtPos(this, xpos, ypos + 1)) {
                mode = CollisionMode.DOWN_COLLIDE_TITLE;
            }

            // Entity collision checking
            if (getParentWorld().getEntityForCoordinates(xpos, ypos + height - 1).isPresent()) {
                collision = getParentWorld().getEntityForCoordinates(xpos, ypos + height - 1).get();
                mode = CollisionMode.UP_COLLIDE;
            } else if (this.getParentWorld().getEntityForCoordinates(xpos, ypos + 1).isPresent()) {
                collision = this.getParentWorld().getEntityForCoordinates(xpos, ypos + 1).get();
                mode = CollisionMode.DOWN_COLLIDE;
            }

            //move up
            if (this.ypos > waypoint) {
                if (mode != CollisionMode.UP_COLLIDE_TITLE) {
                    if (mode != CollisionMode.UP_COLLIDE || collisionIgnored.contains(collision.getClass())) {
                        this.ypos--;
                        if (this.ypos == waypoint) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
                //move down
            } else {
                if (this.ypos == waypoint) {
                    return false;
                }
                if (mode != CollisionMode.DOWN_COLLIDE_TITLE) {
                    if (mode != CollisionMode.DOWN_COLLIDE || collisionIgnored.contains(collision.getClass())) {
                        this.ypos++;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * The interaction handler. Will be called when another entity wants to interact with this
     * entity.
     *
     * @param sender the sending objects
     */
    public Boolean interact(WorldEntity sender) {
        // Do nothing by deafult
        return Boolean.TRUE;
    }

    public void updateTask(Optional<Task> newtask) {
        this.currentTask = newtask;
    }

    public Optional<Task> getCurrentTask() {
        return currentTask;
    }

    public void addCollisionIgnoredClass(Class x) {
        collisionIgnored.add(x);
    }

    public void removeCollisionIgnoredClass(Class x) {
        collisionIgnored.remove(x);
    }

    enum CollisionMode {
        LEFT_COLLIDE,
        RIGHT_COLLIDE,
        LEFT_COLLIDE_TILE,
        RIGHT_COLLIDE_TILE,
        UP_COLLIDE,
        DOWN_COLLIDE,
        BOTTOM_COLLIDE,
        UP_COLLIDE_TITLE,
        DOWN_COLLIDE_TITLE,
        NONE
    }

}
