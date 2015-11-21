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

public class DisplayBuildingInformation {

    private static final Logger LOGGER = Logger.getLogger(MouseHandler.class);
    private static World gameworld = World.getInstance();
    private static Pane statContainer;

    private DisplayBuildingInformation() {

    }

    /**
     * Display the selected Building information in the context manager
     *
     * @require Entity to be instance of Building
     * @ensure Context Manager displays Building information
     */
    public static void showBuildingStat(Optional<WorldEntity> entityClicked) {
        for (WorldEntity entity : gameworld.getWorldentities()) {
            if (entityClicked.isPresent() && (entity.toString().equals(entityClicked.get().toString()))) {
                try {

                    statContainer = new VBox();
                    statContainer.setStyle("-fx-background-color: rgba(0,112,32,0.2); -fx-border-color: rgba(0,112,32,0.2);");
                    Label nameLabel = new Label("Name: " + ((Building) entity).getName() + "\n"
                            + "Class: Building" + "\n"
                            + "Health: " + ((Building) entity).getHealth() + "/100" + "\n"
                            + "Description: " + ((Building) entity).getDesc() + "\n\n");
                    nameLabel.setWrapText(true);
                    nameLabel.setTextFill(Paint.valueOf("white"));
                    nameLabel.setMaxWidth(160);
                    Button sellButton = new Button("Sell");

                    sellButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            ((Building) entity).setHealth(-1);
                            clearContainer();
                        }
                    });


                    statContainer.getChildren().add(nameLabel);
                    statContainer.getChildren().add(sellButton);

                    ContextAreaHandler.getInstance().addContext("buildingStats", statContainer).setContext("buildingStats");
                } catch (NoClassDefFoundError | ExceptionInInitializerError e) {
                    LOGGER.debug("JavaFX not running - are you running tests?", e);
                }
                break;
            }
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
