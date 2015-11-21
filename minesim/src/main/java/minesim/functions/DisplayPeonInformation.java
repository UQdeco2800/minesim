package minesim.functions;

import minesim.ContextAreaHandler;
import org.apache.log4j.Logger;

import java.util.Optional;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import minesim.World;
import minesim.entities.Peon;
import minesim.entities.WorldEntity;
import minesim.inputhandlers.MouseHandler;

public class DisplayPeonInformation {

    private static final Logger LOGGER = Logger.getLogger(MouseHandler.class);
    private static World gameworld = World.getInstance();

    private DisplayPeonInformation() {

    }

    /**
     * Display the selected Peon information in the context manager
     *
     * @require Entity to be instance of Peon
     * @ensure Context Manager displays Peon information
     */
    public static void showPeonStat_v2(Optional<WorldEntity> entityClicked) {

        for (WorldEntity entity : gameworld.getWorldentities()) {

            if (entityClicked.isPresent() && (entity.toString().equals(entityClicked.get().toString()))) {

                try {
                    Pane statContainer = new Pane();
                    statContainer.setStyle("-fx-background-color: rgba(50,130,200,0.3); -fx-border-color: black;");
                    
                    Label peonName = new Label(((Peon) entity).getName());
                    peonName.relocate(10, 10);
                    peonName.setTextFill(Paint.valueOf("white"));
                    peonName.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
                   
                    
                    Label healthLabel = new Label("Health:");
                    healthLabel.relocate(10, 35);
                    healthLabel.setTextFill(Paint.valueOf("white"));
                    healthLabel.setFont(Font.font("Verdana", 12));
                    Rectangle rectangleHPRED = new Rectangle(140,12,Color.RED);
                    rectangleHPRED.relocate(10,49);
                    double size = ((double)(((Peon) entity).getHealth()) / ((double)((Peon) entity).getMaxHP()));
                    Rectangle rectangleHPGreen = new Rectangle((int)140*size,12,Color.GREEN);
                    rectangleHPGreen.relocate(10,49);
                    Label numberHPLabel;
                    int currentPeonHP = (((Peon) entity).getHealth());
                    int currentPeonMax = ((Peon) entity).getMaxHP();
                    if (currentPeonHP >= 100) {
                        numberHPLabel = new Label(currentPeonHP + "/" + currentPeonMax);
                    } else if (currentPeonHP < 100 && currentPeonHP > 10) {
                        numberHPLabel = new Label("  "+ currentPeonHP + "/" + currentPeonMax);
                    } else {
                        numberHPLabel = new Label("    "+currentPeonHP + "/" + currentPeonMax);
                    }
                     
                    numberHPLabel.setTextFill(Paint.valueOf("white"));
                    numberHPLabel.setFont(Font.font("Verdana",FontPosture.ITALIC, 8));
                    numberHPLabel.relocate(60, 50);
                    
                    Label tiredLabel = new Label("Stamina:");
                    tiredLabel.relocate(10, 65);
                    tiredLabel.setTextFill(Paint.valueOf("white"));
                    tiredLabel.setFont(Font.font("Verdana", 12));
                    Rectangle rectangleStaRED = new Rectangle(140,12,Color.RED);
                    rectangleStaRED.relocate(10,79);
                    double sizeTired = (((double)1000 - ((Peon)entity).getTiredness()) / (double)1000);
                    Rectangle rectangleStaBLUE = new Rectangle((int)140*sizeTired,12,Color.BLUE);
                    rectangleStaBLUE.relocate(10,79);
                    Label numberTiredLabel;
                    int currentPeonTiredValue = (int) (((double)1000 - ((Peon)entity).getTiredness()));
                    if (currentPeonTiredValue == 1000) {
                        numberTiredLabel = new Label((1000 - ((Peon)entity).getTiredness()) + "/1000"); 
                    } else if (currentPeonTiredValue < 1000 && currentPeonTiredValue > 100) {
                        numberTiredLabel = new Label(" " +(1000 - ((Peon)entity).getTiredness()) + "/1000"); 
                    } else {
                        numberTiredLabel = new Label("    " +(1000 - ((Peon)entity).getTiredness()) + "/1000"); 
                    }
                    numberTiredLabel.setTextFill(Paint.valueOf("white"));
                    numberTiredLabel.setFont(Font.font("Verdana",FontPosture.ITALIC, 8));
                    numberTiredLabel.relocate(55, 80);

                    statContainer.getChildren().add(peonName);
                    statContainer.getChildren().add(healthLabel);
                    statContainer.getChildren().add(rectangleHPRED);
                    statContainer.getChildren().add(rectangleHPGreen);
                    statContainer.getChildren().add(numberHPLabel);
                   
                    statContainer.getChildren().add(tiredLabel);
                    statContainer.getChildren().add(rectangleStaRED);
                    statContainer.getChildren().add(rectangleStaBLUE);
                    statContainer.getChildren().add(numberTiredLabel);
                    
                    Label nameLabel = new Label("Level: "
                            + ((Peon) entity).getLevel() + "\n" + "Experience: " + ((Peon) entity).getExperience() + "/"
                            + ((Peon) entity).experienceRequired() + "\n" + "Class: Peon" + "\n\n"
                            + "Disease: " + ((Peon) entity).statusDiseased() +  "\n"
                            + "Stats" + "\n" + "Strength: " + ((Peon) entity).getStrength() + "\n" + "Speed: "
                            + ((Peon) entity).getSpeed() + "\n" + "Luck: " + ((Peon) entity).getLuck() + "\n\n"
                            + "Emotion" + "\n" + "Annoyance: " + ((Peon) entity).getAnnoyance() + "/1000" + "\n" + "Happiness: "
                            + ((Peon) entity).getHappiness());
                    nameLabel.setWrapText(true);
                    nameLabel.setTextFill(Paint.valueOf("white"));
                    nameLabel.setMaxWidth(160);
                    nameLabel.relocate(10, 100);
                    statContainer.getChildren().add(nameLabel);
                   
                    ContextAreaHandler.getInstance().addContext("peonStats", statContainer).setContext("peonStats");
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
