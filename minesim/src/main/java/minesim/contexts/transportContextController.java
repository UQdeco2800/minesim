package minesim.contexts;

import minesim.ContextAreaHandler;
import minesim.entities.items.Transportation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 * Handles the context pane for generating Transportation entities,
 * includes buttons for each transportation type which when clicked
 * will begin the dynamic generating process.
 *
 * @author astridf
 */

public class transportContextController implements Initializable{
    
    private TransportContextControllerHandler handler = null;
    
    @FXML
    private VBox buttonBox;
    @FXML
    private GridPane container;
    @FXML
    private Button ropeButton;
    @FXML
    private Button ladderButton;
    @FXML
    private Button elevatorButton;
    @FXML
    private VBox containerBox;
    @FXML
    private VBox ropeBox;
    @FXML
    private VBox ladderBox;
    @FXML
    private VBox elevatorBox;
    @FXML
    private Label ropeLabel;
    @FXML
    private Label ropeLabel2;
    @FXML
    private Label ladderLabel;
    @FXML
    private Label ladderLabel2;
    @FXML
    private Label elevatorLabel;
    @FXML
    private Label elevatorLabel2;
    @FXML
    private Text instructionsTitle;
    @FXML
    private Label instructionsLine1;
    @FXML
    private Label instructionsLine2;
    @FXML
    private Label instructionsLine3;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = new TransportContextControllerHandler(this);
    }
    /**
     * This method creates all gui elements to be added to the context
     * pane, and sets up the transportation buttons as well as their
     * setOnAction functions.
     */
    public void constructTransportMenu() {
        buttonBox = new VBox();
        container = new GridPane();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        container.setAlignment(Pos.TOP_CENTER);
        ropeButton = new Button("Rope");        
        ladderButton = new Button("Ladder");
        elevatorButton = new Button("Elevator");
        ropeButton.getStyleClass().add("otherbutton");
        ropeButton.setContentDisplay(ContentDisplay.CENTER);
        ropeButton.setAlignment(Pos.CENTER);
        ladderButton.getStyleClass().add("otherbutton");
        ladderButton.setContentDisplay(ContentDisplay.CENTER);
        ladderButton.setAlignment(Pos.CENTER);
        elevatorButton.getStyleClass().add("otherbutton");
        elevatorButton.setContentDisplay(ContentDisplay.CENTER);
        elevatorButton.setAlignment(Pos.CENTER);
        
        ropeButton.setOnAction(event -> {
            Transportation.transportMode = 1;
            Transportation.transportType = "rope";
            ropeBox = new VBox(5);
            ropeBox.setPadding(new Insets(10, 10, 10, 10));
            ropeLabel = new Label("Generating Rope");
            ropeLabel2 = new Label("Click the start point...");
            ropeLabel.setTextFill(Paint.valueOf("white"));
            ropeLabel2.setTextFill(Paint.valueOf("white"));
            ropeBox.getChildren().add(ropeLabel);
            ropeBox.getChildren().add(ropeLabel2);
            ContextAreaHandler.getInstance().addContext("rope", ropeBox).setContext("rope");
        });
        
        ladderButton.setOnAction(event -> {
            Transportation.transportMode = 1;
            Transportation.transportType = "ladder";
            ladderBox = new VBox(5);
            ladderBox.setPadding(new Insets(10, 10, 10, 10));
            ladderLabel = new Label("Generating Ladder");
            ladderLabel2 = new Label("Click the start point...");
            ladderLabel.setTextFill(Paint.valueOf("white"));
            ladderLabel2.setTextFill(Paint.valueOf("white"));
            ladderBox.getChildren().add(ladderLabel);
            ladderBox.getChildren().add(ladderLabel2);
            ContextAreaHandler.getInstance().addContext("ladder", ladderBox).setContext("ladder");
        });
        
        elevatorButton.setOnAction(event -> {
            Transportation.transportMode = 1;
            Transportation.transportType = "elevator";
            elevatorBox = new VBox(5);
            elevatorBox.setPadding(new Insets(10, 10, 10, 10));
            elevatorLabel = new Label("Generating Elevator");
            elevatorLabel2 = new Label("Click the start point...");
            elevatorLabel.setTextFill(Paint.valueOf("white"));
            elevatorLabel2.setTextFill(Paint.valueOf("white"));
            elevatorBox.getChildren().add(elevatorLabel);
            elevatorBox.getChildren().add(elevatorLabel2);
            ContextAreaHandler.getInstance().addContext("elevator", elevatorBox).setContext("elevator");
        });

        buttonBox.getChildren().add(ropeButton);
        buttonBox.getChildren().add(ladderButton);
        buttonBox.getChildren().add(elevatorButton);
        container.getChildren().add(buttonBox);
        ContextAreaHandler.getInstance().addContext("Transport", container).setContext("Transport");

        containerBox = new VBox(10);
        containerBox.setAlignment(Pos.TOP_CENTER);
        containerBox.setMaxSize(150,200);
        containerBox.setPadding(new Insets(10,10,10,10));
        containerBox.setStyle("-fx-border-color: white;");
        instructionsTitle = new Text("---Instructions!---");
        instructionsLine1 = new Label("(i) Select type of tranportation");
        instructionsLine2 = new Label("(ii) Click the start point & end point on map");
        instructionsLine3 = new Label("** Tip: Right click to cancel generating");
        instructionsTitle.setFill(Paint.valueOf("white"));
        instructionsLine1.setTextFill(Paint.valueOf("white"));
        instructionsLine2.setTextFill(Paint.valueOf("white"));
        instructionsLine3.setTextFill(Paint.valueOf("white"));  
        instructionsLine1.setWrapText(true);
        instructionsLine2.setWrapText(true);
        instructionsLine3.setWrapText(true);
        containerBox.getChildren().addAll(instructionsTitle,instructionsLine1,instructionsLine2,instructionsLine3);
        buttonBox.getChildren().add(containerBox);
    }
}
