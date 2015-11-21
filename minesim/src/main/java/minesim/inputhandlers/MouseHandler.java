package minesim.inputhandlers;

//import apple.laf.JRSUIConstants;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import minesim.World;
import minesim.contexts.PeonStatusContextControllerHandler;
import minesim.controllers.PeonSelectorController;
import minesim.entities.*;
import minesim.entities.items.Transportation;
import minesim.functions.AddEntityOnMouseClick;
import minesim.functions.DisplayBuildingInformation;
import minesim.functions.DisplayPeonInformation;
import minesim.tasks.DigTile;
import notifications.NotificationManager;

import org.apache.log4j.Logger;

import javax.swing.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Optional;

//import java.awt.Point;


/**
 * This is a basic mouse click handler. Detects single clicks and processes and throws coordinates
 * to the game system
 */
public class MouseHandler implements EventHandler<MouseEvent> {

    public static final ArrayList<WorldEntity> registeredEntities = new ArrayList<WorldEntity>();
    static final int PANNING_SPEED = 10; // placeholder speed for panning
    public static double eventX;
    public static double eventY;
    World gameworld;
    boolean north, south, east, west;
    Timer mouseResting;
    boolean dragInitiated = false;
    int dragStartX;
    int dragStartY;
    int dragEndX;
    int dragEndY;
    int selectSecond;
    SelectionBox area;
    int actionMode = 0;
    private boolean running = true;
    public boolean stickyCam;
    private WorldEntity stickyPeon = null;
    private double restingDelay = 50;   // number of milliseconds for thread to wait
    private Logger logger = Logger.getLogger(MouseHandler.class);
            /*
             * actionMode = 0 -> move
             * 				1 -> dig
             * 				2 -> set ladder size
             */

    /**
     * the constructor of the class MousHandler
     *
     * @param World world
     */

    public MouseHandler(World world) {
        this.gameworld = world;
        area = new SelectionBox(0, 0, 0, 0);
        initPanningThread();
    }

    /**
     * Indicates which Peon(s) will complete the next action set.
     *
     * @param WorldEntity sender
     */
    public void registerForNextClick(WorldEntity sender) {
        registeredEntities.add(sender);
    }

    /**
     * Override the handle function handle the different types of event
     *
     * @param MouseEvent event
     */
    @Override
    public void handle(MouseEvent event) {
        double x = event.getX() + gameworld.getXOffset();
        double y = event.getY() + gameworld.getYOffset();
        eventX = x;
        eventY = y;
        
        Transportation.buildTransportEntity(x, y);
        AddEntityOnMouseClick.moveEntity(x, y, gameworld);

        /**Mouse Pressed*/
        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && !dragInitiated) {
            dragStartX = (int) x;
            dragStartY = (int) y;

            area.setSelectedArea(dragStartX, dragStartY, dragStartX, dragStartY);
            gameworld.setArea(area.getRect());

            // If we left clicked
            if (event.getButton().equals(MouseButton.PRIMARY)) {

                // Point and Click API Stuff
                AddEntityOnMouseClick.addEntity(x, y, gameworld);

                //Clear the container when button press is not on peon
                DisplayPeonInformation.clearContainer();

                // remove previously registered entities
                registeredEntities.clear();

                // Check if we got an entity
                Optional<WorldEntity> entityClicked = gameworld.getEntityForCoordinates(x, y);
                if (entityClicked.isPresent()) {
                    logger.debug("Clicked on "+ entityClicked.get().toString());

                    if (entityClicked.get() instanceof Peon) {
                    	//DisplayPeonInformation.showPeonStat(entityClicked);
                        PeonStatusContextControllerHandler.getInstance().showPeonStatus(entityClicked.get());

                    } else if (entityClicked.get() instanceof Building) {
                        DisplayBuildingInformation.showBuildingStat(entityClicked);
                    }

                    // Start the to click process on click then do entities
                    entityClicked.get().notifyOfClickActionOnEntity(this);

                // We're actually digging at the moment
                } else if (actionMode == 1) {
                    gameworld.addTask(new DigTile(new Peon(0, 0, 0, 0, "tempPeon"), (int) x ,
                            (int) y , this.gameworld.getTileManager(), 10));
                }
                //In the process of placing a ladder
                if (Transportation.transportMode == 1 || Transportation.transportMode == 2) {
                	Transportation.primaryClick = Boolean.TRUE;
                }
            }
        }

        /* Mouse Middle Button Pressed */
        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && !dragInitiated) {
        	initStickyCamThread();
        	if (event.getButton().equals(MouseButton.MIDDLE)) {
                AddEntityOnMouseClick.addEntity(x, y, gameworld);
                DisplayPeonInformation.clearContainer();
                registeredEntities.clear();
                Optional<WorldEntity> entityClicked = gameworld.getEntityForCoordinates(x, y);
                if (entityClicked.isPresent() && entityClicked.get() instanceof Peon) {
                	stickyPeon = entityClicked.get();
                	toggleStickyCam();
                } 
            }
        }

        /** Mouse drag - during */
        if (event instanceof MouseDragEvent && !dragInitiated) {
            MouseDragEvent dragEvent = (MouseDragEvent) event;
            dragInitiated = true;
            area.setSelectedArea(dragStartX, dragStartY, event.getX() + gameworld.getXOffset(), event.getY() + gameworld.getYOffset());
            if (event.getButton().equals(MouseButton.PRIMARY)) {
            	gameworld.setAreaColor(Color.rgb(255, 128, 128, 0.25));
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            	gameworld.setAreaColor(Color.rgb(128, 255, 255, 0.25));
            }
            gameworld.setArea(area.getRect());
        }


        if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
        	if (event.getButton().equals(MouseButton.PRIMARY)) {
	        	PeonSelectorController control = PeonSelectorController.getInstance();
	        	control.clearPeonInterface("all");
	        	dragInitiated = true;
	            area.setSelectedArea(dragStartX, dragStartY, event.getX() + gameworld.getXOffset(), event.getY() + gameworld.getYOffset());
	            for (WorldEntity entity : gameworld.getWorldentities()) {
	                if ((entity.getClass().equals(Peon.class)) || (entity.getClass().equals(PeonMiner.class)) || (entity.getClass().equals(PeonGuard.class)) || (entity.getClass().equals(PeonGather.class))) {
	                    Point2D p = new Point2D.Double(entity.getXpos(), entity.getYpos());
	                    if (gameworld.getArea().contains(p) && entity.getClass().equals(Peon.class) || (entity.getClass().equals(PeonMiner.class)) || (entity.getClass().equals(PeonGuard.class)) || (entity.getClass().equals(PeonGather.class))) {
	                        this.choosePeon(entity);
	                    }
	                }
	            }
	            control.applyPeonInterface();
	            gameworld.setArea(area.getRect());
        	} else if (event.getButton().equals(MouseButton.SECONDARY)) {
        		dragInitiated = true;
        		area.setSelectedArea(dragStartX, dragStartY, event.getX() + gameworld.getXOffset(), event.getY() + gameworld.getYOffset());
        		gameworld.setAreaColor(Color.rgb(255, 255, 0, 0.25));
        		gameworld.setArea(area.getRect());
        	}
        }        
        /** mouse release event */
        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
        	
        	if (dragInitiated) {
        		dragEndX = (int) x;
                dragEndY = (int) y;
                area.clearArea();
                gameworld.setArea(area.getRect());
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                	for (WorldEntity entity : registeredEntities) {
    	                entity.notifyOfRegisteredClickAction(dragStartX, 
    	                		dragStartY, this, event, actionMode);
    	            }
                }
        	} else if (event.getButton().equals(MouseButton.SECONDARY)) {
	        	AddEntityOnMouseClick.removeEntity(gameworld);
	        	if (Transportation.transportMode != 0) {
	        		Transportation.transportCancel = Boolean.TRUE;
	        	}
	            for (WorldEntity entity : registeredEntities) {
	                entity.notifyOfRegisteredClickAction(this, event, actionMode);
	            }
        	}
        	resetDragPoints();
        }

        /** start mouse panning */
        if (event.getEventType().equals(MouseEvent.MOUSE_MOVED)) {
            pollMousePosition(event);
        }

        /** stops panning if mouse moved out of screen bounds */
        if (event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
            north = south = east = west = false;
        }
    }

    /**
     * Calculates an imaginary margin to be 8% of the game screens height. Returns double
     * representing the y-position of the top margin.
     *
     * @return y-position of top margin.
     */
    private double getTopMargin() {
        return 0.08 * getHeight();
    }

    /**
     * Calculates an imaginary margin to be 8% of the game screens width. Returns double
     * representing the x-position of the right margin.
     *
     * @return x-position of right margin.
     */
    private double getRightMargin() {
        return 0.92 * getWidth();
    }

    /**
     * Calculates an imaginary margin to be 8% of the game screens height. Returns double
     * representing the y-position of the left margin.
     *
     * @return y-position of bottom margin.
     */
    private double getBottomMargin() {
        return 0.92 * getHeight();
    }

    /**
     * Calculates an imaginary margin to be 8% of the game screens width. Returns double
     * representing the x-position of the left margin.
     *
     * @return x-position of left margin.
     */
    private double getLeftMargin() {
        return 0.08 * getWidth();
    }

    /**
     * If panning is required, checks which direction to pan. Else, does not pan.
     *
     * @param MouseEvent event - the event that causes this function to be called
     */
    private void pollMousePosition(MouseEvent event) {
        //update panning directions
        if (panningRequired(event)) {
            double pointX = event.getX();
            double pointY = event.getY();

            west = pointX < getLeftMargin();
            east = pointX > getRightMargin();
            north = pointY < getTopMargin();
            south = pointY > getBottomMargin();

        } else {
            west = false;
            east = false;
            north = false;
            south = false;
        }

    }

    /**
     * Checks if panning is required or not.
     *
     * @param MouseEvent event - required to check where the mouse is positioned after the
     *                   MouseEvent takes place.
     * @return bool - returns true if panning is required, else returns false.
     */
    private boolean panningRequired(MouseEvent event) {
        double pointX = event.getX();
        double pointY = event.getY();
        /** check if mouse in margin */
        return pointX < getLeftMargin() || pointX > getRightMargin() || pointY < getTopMargin() || pointY > getBottomMargin();
    }

    /**
     * Initialize the thread for panning.
     */
    private void initPanningThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    if (north) {
                        gameworld.moveScreenUp(PANNING_SPEED);
                    }
                    if (east) {
                        gameworld.moveScreenRight(PANNING_SPEED);
                    }
                    if (south) {
                        gameworld.moveScreenDown(PANNING_SPEED);
                    }
                    if (west) {
                        gameworld.moveScreenLeft(PANNING_SPEED);
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        logger.debug("Mouse Pan Interrupted!", e);
                    }
                }
            }
        });
        t.start();
    }
    
    /**
     * Initialize the thread for sticky camera.
     */
    private void initStickyCamThread() {
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (stickyCam) {
                	if (!stickyPeon.alive) {
                		stickyCam = false;
                	}
                	
                	int xpos = stickyPeon.getXpos();
                    int ypos = stickyPeon.getYpos();
                	moveCamera(xpos, ypos);
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        logger.debug("StickyCam Interrupted!", e);
                    }
                    
                }
            }
        });
        t2.start();
    }
    /**
     * 
     */
    public void toggleStickyCam() {
    	if(!stickyCam) {
    		gameworld.setContextMode(2);
    		stickyCam = true;
    		NotificationManager.notify("StickyCam enabled");
    	} else {
    		gameworld.setContextMode(0);
    		stickyCam = false;
    		NotificationManager.notify("StickyCam disabled");
    	}
    }
    
    /**
     * Setter method for stickyPeon
     * Sets the Peon to be targeted by the sticky camera
     * 
     * @param entity the Peon to be targeted
     */
    public void setStickyPeon(WorldEntity entity) {
    	stickyPeon = entity;
    }
    
    /**
     * Moves camera to the provided XYpos
     * Used for centering on Peons for the StickyCamera
     * 
     * @param xpos the x axis position the entity to be focused on
     * @param ypos the y axis position the entity to be focused on
     * */
    public void moveCamera(int xpos, int ypos) {
        // move camera
        int currentX = gameworld.getXOffset();
        int currentY = World.getInstance().getYOffset();
        int centerX = (int) World.getInstance().getCanvas().getWidth() / 2;
        int centerY = (int) World.getInstance().getCanvas().getHeight() / 2;
        // X position
        if(xpos > currentX + centerX + 40) {
            World.getInstance().moveScreenRight(5);
        } else if(xpos < currentX + centerX - 40) {
            World.getInstance().moveScreenLeft(5);
        }
        // Y postion
        if(ypos < currentY + centerY - 40) {
            World.getInstance().moveScreenUp(5);
        } else if(ypos > currentY + centerY + 40) {
            World.getInstance().moveScreenDown(5);
        } 
        
    }
    
    /**
     * Used to switch between right click functions. If the active action mode is toggled again, the
     * MouseHandler returns to its default action state (right click move)
     *
     * @param actionMode - the action mode you want to set
     */
    public void toggleActionMode(int actionMode) {
        //TODO: Get an ENUM happening
        if (this.actionMode == actionMode) {
        	
        }
    }
    
    public int getToggleActionMode() {
    	return this.actionMode;
    }

    public void setActionMode(int actionMode) {
        if (this.actionMode == actionMode) {
            this.actionMode = 0;
        } else {
            this.actionMode = actionMode;
        }
        gameworld.setContextMode(this.actionMode);
    }

    /**
     * used to reset the DragPoints
     */
    private void resetDragPoints() {
        dragStartX = 0;
        dragStartY = 0;
        dragEndX = 0;
        dragEndY = 0;
        dragInitiated = false;
    }

    /**
     * put the WorldEntity into the registeredEntities, which indicates which Peons are selected
     *
     * @param WorldEntity entity - the WorldEntity to be chosen
     */
    private void choosePeon(WorldEntity entity) {
        for (WorldEntity entry : registeredEntities) {
            if (entity == entry)
                return;
        }
        PeonSelectorController control = PeonSelectorController.getInstance();
        control.buildPeonButtons(entity);
        entity.notifyOfClickActionOnEntity(this);
    }

    /**
     * get the width of the object attached to MouseHandler
     *
     * @return double width
     */
    public double getWidth() {
        return gameworld.getWidth();
    }

    /**
     * get the height of the object attached to MouseHandler
     *
     * @return double height
     */
    public double getHeight() {
        return gameworld.getHeight();
    }
}