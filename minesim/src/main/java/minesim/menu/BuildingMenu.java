package minesim.menu;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import org.apache.log4j.Logger;


public class BuildingMenu {
	private static final Logger LOG = Logger.getLogger(BuildingMenu.class);
    private static Pane backing = createBacking();
    private static StackPane outerPane = new StackPane();
    private static Pane menu;
    
    /*
     * Empty constructor
     */
    public BuildingMenu() {
    	
    }
    
    private static Pane createBacking() {
        Pane p = new Pane();
        p.setStyle("-fx-background-color: #191d22; -fx-border-color: darkred; -fx-opacity: 0.8;");
        p.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new BuildingMenu().hideMenu();
            }
        });
        return p;
    }
    
    public static BuildingMenu init(StackPane parent) {
    	outerPane.getChildren().add(backing);
    	try {
			menu = FXMLLoader.load(BuildingMenu.class.getResource("/layouts/menus/BuildingMenu.fxml" ));
		} catch (IOException e) {
			 LOG.error("Couldn't load fxml", e);
		}
		menu.getStylesheets().add(BuildingMenu.class.getResource("/css/BuildingMenuStyle.css").toExternalForm());
		double width = 550;
		double height = 300;
		menu.setMaxHeight(height);
		menu.setMaxWidth(width);
		menu.resize(width, height);
		outerPane.getChildren().add(menu);
		
    	Platform.runLater(new Runnable() {
    		@Override
    		public void run() {
				parent.getChildren().add(outerPane);
    		}
    	});
    	outerPane.setVisible(false);
    	return new BuildingMenu();
    }

	/**
     * Sets the menu's visible property to false.
     */
	public static void hideMenu() {
		outerPane.setVisible(false);
	}
	
	/**
     * Sets the menu's visible property to true.
     */
	public static void showMenu() {
		outerPane.setVisible(true);
	}
	
	/**
     * Returns a Boolean indicating whether or not the menu is visible.
     */
    public static Boolean isVisible() {
        return outerPane.isVisible();
    }
    
    /**
     * For testing
     */
    protected static StackPane getPane() {
        return outerPane;
    }
    
    /**
     * For testing
     */
    protected static void resetAll() {
    	outerPane = new StackPane();
        menu = new StackPane();
    }
}