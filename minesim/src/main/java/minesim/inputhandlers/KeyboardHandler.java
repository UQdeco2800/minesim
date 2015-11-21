package minesim.inputhandlers;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import minesim.World;
import minesim.controllers.PeonSelectorController;
import minesim.entities.Hadouken;
import minesim.entities.Peon;
import minesim.entities.WorldEntity;
import minesim.menu.eMenuHandler;
import notifications.NotificationManager;
import org.apache.log4j.Logger;

/**
 * Singleton Class KeyboardHandler handles all keyboard events in the system
 */
public class KeyboardHandler implements EventHandler<KeyEvent> {

    private static KeyboardHandler instance = null;
    Logger logger = Logger.getLogger(KeyboardHandler.class);
    private World gameworld = null;
    private MouseHandler mouse = null;
    private PeonSelectorController peonSelect = null;

    public static KeyboardHandler getInstance() {
        if (instance == null) {
            instance = new KeyboardHandler();
        }
        return instance;
    }


    /**
     * This must be called to ensure the KeyboardEvents happen on the right world Update where
     * necessary!
     *
     * @param gameworld the current game world
     */
    public void setGameWorld(World gameworld) {
        this.gameworld = gameworld;
    }

    /**
     * set the MouseHandler of the game
     *
     * @param mouse - the mouseHandler of the game
     */
    public void setMouseHandler(MouseHandler mouse) {
        this.mouse = mouse;
    }
    
    /**
     * set the PeonSelectorController of the game
     *
     * @param peonSelect - the PeonSelectorController of the game
     */
    public void setPeonSelectHandler(PeonSelectorController peonSelect) {
        this.peonSelect = peonSelect;
    }


    /**
     * Key press handling method
     *
     * @param event - key event to be handled
     */
    @Override
    public void handle(KeyEvent event) {
        if (gameworld != null) {
            //If you want to keep doing something while holding down a key, dump it in this switch
            if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                switch (event.getCode()) {
                    case D:
                    	/**cursor remain as default when the dig function is not activated.**/
                    	if (mouse.getToggleActionMode() == 1) {
                    		gameworld.getCanvas().setCursor(Cursor.DEFAULT);

                    	} else {
                    		/**cursor change to an shovel icon when the dig function is activated.**/
                    		Image image = new Image("cursor/Dig.png");
                    		
                            gameworld.getCanvas().setCursor(new ImageCursor(image));
                    	}
                        break;
                    case W:
                        PeonsJump();
                        break;
                    case ESCAPE:
                        toggleMenu();
                        break;
                    case UP:
                        gameworld.moveScreenUp(10);
                        break;
                    case LEFT:
                        gameworld.moveScreenLeft(10);
                        break;
                    case DOWN:
                        gameworld.moveScreenDown(10);
                        break;
                    case RIGHT:
                        gameworld.moveScreenRight(10);
                        break;
                    case H:
                        hadouken();
                        break;
                    default:
                        break;
                }
                switch (event.getCode()) {
                    case RIGHT:
                        gameworld.moveScreenRight(20);

                        break;
                    case LEFT:
                        gameworld.moveScreenLeft(20);
                        break;
                    case D:
                    	/**when the dig function is deactivated set (1) and activate (0)**/
                        mouse.setActionMode(1);
                        break;
                    case W:
                        PeonsJump();
                        break;
                    case E:
                    	EscapeRope();
                        break;
                    case S:
                    	stickyCam();
                        break;
                    case DIGIT1:
                    	if(!peonSelect.toggleAllPeons.isSelected()){
                    		peonSelect.toggleAllPeons.setSelected(true);
                    		peonSelect.selectAllPeons();
                    	} else {
                    		peonSelect.toggleAllPeons.setSelected(false);
                    		peonSelect.clearPeonInterface("all");
                    	}
                    	break;
                    case DIGIT2:
                    	if(!peonSelect.toggleAllIdle.isSelected()){
                    		peonSelect.toggleAllIdle.setSelected(true);
                    		peonSelect.selectIdle();
                    	} else {
                    		peonSelect.toggleAllIdle.setSelected(false);
                    		peonSelect.clearPeonInterface("idle");
                    	}
                    	break;
                    case DIGIT3:
                    	if(!peonSelect.toggleAllMiners.isSelected()){
                    		peonSelect.toggleAllMiners.setSelected(true);
                    		peonSelect.selectMiners();
                    	} else {
                    		peonSelect.toggleAllMiners.setSelected(false);
                    		peonSelect.clearPeonInterface("Miner");
                    	}
                    	break;
                    case DIGIT4:
                    	if(!peonSelect.toggleAllGuards.isSelected()){
                    		peonSelect.toggleAllGuards.setSelected(true);
                    		peonSelect.selectGuards();
                    	} else {
                    		peonSelect.toggleAllGuards.setSelected(false);
                    		peonSelect.clearPeonInterface("Guard");
                    	}
                    	break;
                    case DIGIT5:
                    	if(!peonSelect.toggleAllGatherers.isSelected()){
                    		peonSelect.toggleAllGatherers.setSelected(true);
                    		peonSelect.selectGatherers();
                    	} else {
                    		peonSelect.toggleAllGatherers.setSelected(false);
                    		peonSelect.clearPeonInterface("Gatherer");
                    	}
                    	break;
                    default:
                        break;
                }
            }
        }
    }    
    private void toggleMenu() {
        eMenuHandler m = new eMenuHandler();
        if (eMenuHandler.isVisible()) eMenuHandler.hideMenu();
        else eMenuHandler.showMenu();
    }

    /**
     * The function to make the peon jump when press w invoke the MakePeonJump function in each
     * peon
     */
    public void PeonsJump() {
        if (MouseHandler.registeredEntities != null) {
            for (WorldEntity entity : MouseHandler.registeredEntities) {
                if (entity.getClass().equals(Peon.class)) {
                    Peon jumpPeon = (Peon) entity;
                    jumpPeon.makePeonJump();
                    System.out.println(jumpPeon.getName() + "jump");
                }
            }
        }
    }
    
    /**
     * Activates the Peon Escape Rope
     * Teleports the selected Peon(s) to a safe location above ground
     * 
     * @ensure Peon entity is selected
     */
    public void EscapeRope() {
        if (MouseHandler.registeredEntities != null) {
            for (WorldEntity entity : MouseHandler.registeredEntities) {
                if (entity instanceof Peon) {
                    Peon beamMeUpScotty = (Peon) entity;
                    beamMeUpScotty.escapeRope();
                }
            }
        }
    }
    /**
     * Activates the Sticky Camera in the MouseHandler class
     * 
     * @ensure Peon entity is selected
     */
    public void stickyCam() {
    	if (MouseHandler.registeredEntities.isEmpty()) {
    		NotificationManager.notify("No Peon Selected for StickyCam");
    	} else {
	    	WorldEntity entitySelected = MouseHandler.registeredEntities.get(0);
	        if (entitySelected instanceof Peon) {
	        	mouse.setStickyPeon(entitySelected);
	        	mouse.toggleStickyCam();
	        } else {
	        	NotificationManager.notify("No Peon Selected for StickyCam");
	        }
    	}
    }
    /**
     * Activates the Hadouken Projectile
     * The selected Peon will perform Hadouken
     * <p>
     * Checks the direction the Peon is facing to determine
     * the projectile image and direction
     * 
     * @ensure Peon entity is selected
     */
    public void hadouken() {
    	if (!MouseHandler.registeredEntities.isEmpty()) {
	    	WorldEntity entitySelected = MouseHandler.registeredEntities.get(0);
	        if (entitySelected instanceof Peon) {
	        	int dir = ((Peon) entitySelected).getDirection();
	        	if (dir == -1) {
	        		World.getInstance().addEntityToWorld(new Hadouken(entitySelected.getXpos(), (entitySelected
		    				.getYpos()), 16, 16, 100, dir));
	        	} else {
	        		World.getInstance().addEntityToWorld(new Hadouken(entitySelected.getXpos() + 30, (entitySelected
		    				.getYpos()), 16, 16, 100, dir));
	        	}
	        } 
    	}
    	
    }
}