package minesim.functions;

import minesim.ContextAreaHandler;
import org.apache.log4j.Logger;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import minesim.World;
import minesim.entities.Building;
import minesim.entities.WorldEntity;
import minesim.inputhandlers.MouseHandler;

public class DisplayBuildingInstructions {

	private static final Logger LOGGER = Logger.getLogger(MouseHandler.class);
	private static World gameworld = World.getInstance();
	private static Pane statContainer;

	private DisplayBuildingInstructions() {

	}

	/**
	 * Display the selected Building information in the context manager
	 *
	 * @require Entity to be instance of Building
	 * @ensure Context Manager displays Building information
	 */
	public static void showBuildingInstr() {

		try {

			statContainer = new VBox();
			statContainer.setStyle("-fx-background-color: rgba(0,112,32,0.2); -fx-border-color: rgba(0,112,32,0.2);");
			Label nameLabel = new Label("Left Click to place building" + "\n\n" + "Right Click to cancel build");
			nameLabel.setWrapText(true);
			nameLabel.setTextFill(Paint.valueOf("white"));
			nameLabel.setMaxWidth(160);

			statContainer.getChildren().add(nameLabel);

			ContextAreaHandler.getInstance().addContext("buildingStats", statContainer).setContext("buildingStats");
		} catch (NoClassDefFoundError | ExceptionInInitializerError e) {
			LOGGER.debug("JavaFX not running - are you running tests?", e);
		}
		
	}

	

	/**
	 * Clear the context manager box
	 */
	public static void clearContainer() {
		Pane statContainer = new Pane();
		ContextAreaHandler.getInstance().addContext("clear", statContainer).setContext("clear");
	}

}