package minesim.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import minesim.ContextAreaHandler;
import minesim.World;
import minesim.entities.Peon;
import minesim.entities.PeonGather;
import minesim.entities.PeonGuard;
import minesim.entities.PeonMiner;
import minesim.entities.WorldEntity;
import minesim.inputhandlers.KeyboardHandler;
import minesim.inputhandlers.MouseHandler;
import notifications.NotificationManager;

/**
 * Created by Michael on 9/10/2015.
 * 
 * Handles the Peon Selector ToggleGroup buttons and populates 
 * the Peon Interface in the Context Handler
 */
public class PeonSelectorController implements Initializable {
	
	private static PeonSelectorController instance = null;
	
    private TilePane peonTile;
    private ScrollPane peonScroll;
    
    final ToggleGroup group = new ToggleGroup();

    @FXML
    public ToggleButton toggleAllGatherers;
    @FXML
    public ToggleButton toggleAllGuards;
    @FXML
    public ToggleButton toggleAllMiners;
    @FXML
    public ToggleButton toggleAllIdle;
    @FXML
    public ToggleButton toggleAllPeons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	peonTile = new TilePane(8, 8);
    	peonScroll = new ScrollPane(peonTile);
        peonTile.setMaxHeight(500);
        peonTile.setMaxWidth(300);
    	peonTile.getStylesheets().add("css/PeonInterface.css");
    	peonTile.getStyleClass().add("Tilepane");
    	peonScroll.getStylesheets().add("css/PeonInterface.css");
    	peonScroll.getStyleClass().add("Scrollpane");
        peonScroll.setPrefViewportHeight(412);
        peonScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);    // Horizontal scroll bar
        peonScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        peonScroll.setFitToHeight(true);
        peonScroll.setFitToWidth(true);
        peonScroll.setContent(peonTile);
        toggleAllPeons.setToggleGroup(group);
        toggleAllIdle.setToggleGroup(group);
        toggleAllMiners.setToggleGroup(group);
        toggleAllGuards.setToggleGroup(group);
        toggleAllGatherers.setToggleGroup(group);
        KeyboardHandler.getInstance().setPeonSelectHandler(this);
        instance = this;
    }

    /**
     *  Select PeonGatherers present in WorldEntities
     *  <p>
     *  This function is linked to the FXML file, it captures
     *  the Gatherers button input and sends it to the handling
     *  function, selectPeonClass
     */
    @FXML
    public void selectGatherers() {
        selectPeonClass(toggleAllGatherers, PeonGather.class);
    }

    /**
     *  Select PeonGuards present in WorldEntities
     *  <p>
     *  This function is linked to the FXML file, it captures
     *  the Guards button input and sends it to the handling
     *  function, selectPeonClass
     */
    @FXML
    public void selectGuards() {
        selectPeonClass(toggleAllGuards, PeonGuard.class);
    }

    /**
     *  Select PeonMiners present in WorldEntities
     *  <p>
     *  This function is linked to the FXML file, it captures
     *  the Miners button input and sends it to the handling
     *  function, selectPeonClass
     */
    @FXML
    public void selectMiners() {
        selectPeonClass(toggleAllMiners, PeonMiner.class);
    }
    
    /**
	 * Selects all Idle Peons.
	 *
	 * Iterates through each peon in Worldentities, adds them to
	 * a TilePane which is generated in the ContextAreaHandler
	 * <p>
     * This function is linked to the FXML file, it captures
     * the Idle button input, finds Idle Peons in the World,
     * and sends them to the build function, buildPeonButtons
	 */
    @FXML
    public void selectIdle() {
        if (toggleAllIdle.isSelected()) {
        	clearPeonInterface("all");
            for (WorldEntity entity : World.getInstance().getWorldentities()) {
                if (entity instanceof Peon && isIdle(entity)) {
                	buildPeonButtons(entity);
                }
            }
            NotificationManager.notify("Selected all Idle Peons");
        } else {
            clearPeonInterface("Idle");
        }
        applyPeonInterface();
    }
    
    /**
     * Select *all* Peons present in WorldEntities.
	 *
	 * Iterates through each peon in Worldentities, adds them to
	 * a TilePane which is generated in the ContextAreaManager
	 * <p>
     * This function is linked to the FXML file, it captures
     * the All Peons button input, finds all Peons in the World,
     * and sends them to the build function, buildPeonButtons
	 */
    @FXML
    public void selectAllPeons() {
        if (toggleAllPeons.isSelected()) {
        	clearPeonInterface("all");
            for (WorldEntity entity : World.getInstance().getWorldentities()) {
                if (entity instanceof Peon) {
                	buildPeonButtons(entity);
                }
            }
            NotificationManager.notify("Selected all Peons");
        } else {
        	clearPeonInterface("all");
        }
        applyPeonInterface();
    }

    /**
	 * Helper function, selects all Peons by defined class.
	 *
	 * Iterates through each peon in Worldentities, adds them to
	 * a TilePane which is generated in the ContextAreaManager
	 * <p>
	 * This function handles the Toggle Button actions of
	 * Miners, Guards, and Gatherers
	 *
	 * @param  toggle ToggleButton of the 
	 * @param  peonClass  The class of Peon to be handled by the function
	 * 
	 */
    private void selectPeonClass(ToggleButton toggle, Class<?> peonClass) {
        if (toggle.isSelected()) {
        	clearPeonInterface("all");
            for (WorldEntity entity : World.getInstance().getWorldentities()) {
                if (entity.getClass() == peonClass) {
                	buildPeonButtons(entity);
                }
            }
            NotificationManager.notify("Selected all " + peonClass.getSimpleName() + "s");
        } else {
            clearPeonInterface(peonClass.getSimpleName());
        }
        applyPeonInterface();
    }
    
    /**
     * Checks if the Peon does not have a task (Idle)
     * <p>
     * Returns whether the Peon Entity sent to the function
     * has a task or not.
     * 
     * @param	entity The Peon WorldEntity
     * @return		Boolean 
     */
    public static boolean isIdle(WorldEntity entity) {
    	return !entity.getCurrentTask().isPresent();
    }
    
    /**
     * Helper function for Peon Selector Buttons
     * builds buttons to be inserted into the peonTile Tilepane
     * according to the type of Peon selected
     * <p>
     * Attaches handle Actionevent to the button, which calls the
     * moveCamera function to center the screen on the Peon and
     * makePeonJump to make the Peon more visible
     * 
     * @param  entity The Peon WorldEntity
	 * @see         Image
     */
    public void buildPeonButtons(WorldEntity entity) {
    	MouseHandler.registeredEntities.add(entity);
        ImageView playerImage = new ImageView(entity.getClass().getSimpleName() + ".png");
        playerImage.setFitHeight(peonTile.getPrefTileHeight());
        playerImage.setFitWidth(peonTile.getPrefTileWidth());
        Button peonButton = new Button("", playerImage);
        peonButton.getStylesheets().add("css/PeonInterface.css");
        peonButton.setId("gridButton");
        peonButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!entity.getCurrentTask().isPresent()) {
                    int xpos = ((Peon) entity).getXpos();
                    int ypos = ((Peon) entity).getYpos();
                    moveCamera(xpos, ypos);
                    NotificationManager.notify("Gave " + ((Peon) entity).getName() + " a shock. Back to work!");
                }
            }
        });
        peonTile.getChildren().add(peonButton);
    }
    
    /**
     * Applies the content of peonTile to the ContextAreaHandler
     * 
     * @see TilePane
     */
    public void applyPeonInterface() {
    	VBox peonBox = new VBox();
    	peonScroll.setContent(peonTile);
        peonBox.getChildren().add(peonScroll);
        ContextAreaHandler.getInstance().addContext("peonTile", peonBox).setContext("peonTile");
    }
    
    /**
     * Removes all Peons present in the peonTile Tilepane
     * 
     * @see TilePane
     */
    public void clearPeonInterface(String peonType) {
    	peonTile.getChildren().clear();
        World.getInstance().getWorldentities().stream().filter(entity -> entity instanceof Peon).forEach(entity ->
                MouseHandler.registeredEntities.remove(entity));
        NotificationManager.notify("De-selected " + peonType + " Peons");
    }
    
    /**
     * Moves camera to the provided XYpos
     * Used for centering on peons selected through the Peon Interface
     * 
     * @param xpos the x axis position the entity to be focused on
     * @param ypos the y axis position the entity to be focused on
     */
    public void moveCamera(int xpos, int ypos) {
        // move camera
    	World.getInstance().getCanvas().getHeight();
        int currentX = World.getInstance().getXOffset();
        int currentY = World.getInstance().getYOffset();
        int center;
        int centerX = (int) World.getInstance().getCanvas().getWidth() / 2;
        int centerY = (int) World.getInstance().getCanvas().getHeight() / 2;
        if(xpos > currentX) {
        	center = currentX + centerX;
            while(xpos > center) {
                World.getInstance().moveScreenRight(1);
                center++;
            }
        } else if(xpos < currentX) {
            center = currentX + centerX;
            while(xpos < center) {
                World.getInstance().moveScreenLeft(1);
                center--;
            }
        }
        if(ypos < currentY) {
            center = currentY + centerY;
            while(ypos < center) {
                World.getInstance().moveScreenUp(1);
                center--;
            }
        } else if(ypos > currentY) {
            center = currentY + centerY;
            while(ypos > center) {
                World.getInstance().moveScreenDown(1);
                center++;
            }
        }
    }
    public static PeonSelectorController getInstance() {
    	return instance;
    }
}
