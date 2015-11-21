package minesim.controllers;

import java.util.Random;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import minesim.ContextAreaHandler;
import minesim.DatabaseHandler;
import minesim.StockInfo;
import minesim.World;
import minesim.entities.BuildingBar;
import minesim.entities.Peon;
import minesim.entities.PeonGather;
import minesim.entities.PeonGuard;
import minesim.entities.PeonMiner;
import minesim.entities.RequisitionStore;
import minesim.entities.ZombieMob;
import minesim.entities.items.Transportation;
import minesim.functions.AddEntityOnMouseClick;
import minesim.menu.Button;
import minesim.menu.DebugBox;
import minesim.menu.eMenuHandler;
import minesim.menu.eSubMenu;
import minesim.RandomEvents;
import notifications.NotificationManager;
import minesim.RandomEvents;

import org.apache.log4j.Logger;

/**
 * This class is meant to be messy BUT should also not have any direct affect on
 * the game. Use this class to dump your needing to be tested buttons for
 * features etc
 * Created by Michael on 9/10/2015.
 */
public class DebugDumpController {
	
	private static boolean debug = false;
    private static final Logger LOG = Logger.getLogger(DebugDumpController.class);
    private String[] peonNames = {"Bob", "Urist", "Ukor"};
    private int randomIndex = new Random().nextInt(peonNames.length);

    /**
     * register all the buttons on init
     */
    public DebugDumpController() {
    		registerAllWithTheDebugBox();
    }

    /**
     * Dump your registering of things in here
     */
    public void registerAllWithTheDebugBox() {
        Button storeMenu = new Button("store Menu");
        storeMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                storeMenu();
            }
        });
        DebugBox.addNode(storeMenu);

        Button addPeon = new Button("Point & Click");
        addPeon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addPeon();
            }
        });
        DebugBox.addNode(addPeon);

        Button addStore = new Button("add Store");
        addStore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addStore();
            }
        });
        DebugBox.addNode(addStore);

        Button reqMenu = new Button("req Menu");
        reqMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ReqMenu();
            }
        });
        DebugBox.addNode(reqMenu);

        Button addZomb = new Button("Add Zombie");
        addZomb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addZombie();
            }
        });
        DebugBox.addNode(addZomb);

        Button addBuilding = new Button("Building Options");
        addBuilding.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new eSubMenu(new eMenuHandler()).with("BuildingMenu.fxml");
            }
        });
        DebugBox.addNode(addBuilding);
        
        
        Button randomEvents = new Button("Random Events");
        randomEvents.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent actionEvent) {
        		eventsMenu();
        	}
        });
        DebugBox.addNode(randomEvents);
    }

    public void storeMenu() {
        VBox menu = new VBox();
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setSpacing(5);
        GridPane buttons = new GridPane();
        StackPane context = new StackPane();
        context.setMaxHeight(400);
        context.setMaxWidth(300);
        //quick select buttons
        Button quickSet = new Button();
        quickSet.setText("quick Set");
        quickSet.setWrapText(true);
        quickSet.setTextAlignment(TextAlignment.JUSTIFY);
        quickSet.setContentDisplay(ContentDisplay.CENTER);
        buttons.add(quickSet, 0, 0);

        Button quickRemove = new Button("remove All");
        quickRemove.setWrapText(true);
        quickRemove.setTextAlignment(TextAlignment.JUSTIFY);
        quickRemove.setContentDisplay(ContentDisplay.CENTER);
        buttons.add(quickRemove, 0, 1);

        Button gearButton = new Button("Trade gear");
        gearButton.setWrapText(true);
        gearButton.setTextAlignment(TextAlignment.JUSTIFY);
        gearButton.setContentDisplay(ContentDisplay.CENTER);
        buttons.add(gearButton, 1, 0);

        Button setButton = new Button("Create Set");
        setButton.setWrapText(true);
        setButton.setTextAlignment(TextAlignment.JUSTIFY);
        setButton.setContentDisplay(ContentDisplay.CENTER);
        buttons.add(setButton, 1, 1);

        menu.getChildren().add(buttons);
        buttons.getStylesheets().add("css/ButtonStyles.css");
        context.getStylesheets().add("css/store/storeStyles.css");


        VBox gearMenu = new VBox();
        gearMenu.setAlignment(Pos.CENTER);
        gearMenu.setSpacing(10);

        Label title = new Label("Trade Gear");
        gearMenu.getChildren().add(title);

        ScrollPane storeGearScroll = new ScrollPane();
        storeGearScroll.fitToWidthProperty();
        storeGearScroll.setMaxHeight(150);
        storeGearScroll.setMaxWidth(120);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        GridPane storeGear = new GridPane();
        storeGear.prefHeight(150);
        storeGear.setAlignment(Pos.CENTER);
        int i = 0;
        for (StockInfo stock : databaseHandler.getAllItems()) {
            Button button = new Button();
            button.setPrefWidth(85);
            button.setText(stock.getName());
            storeGear.add(button, 0, i);
            Label label = new Label(String.format(" %02d ", stock.getStock()));
            label.minWidth(40);
            storeGear.add(label, 1, i);
            i++;
        }
        storeGear.getStylesheets().add("css/store/ItemButtons.css");

        storeGearScroll.setContent(storeGear);

        ScrollPane peonGearScroll = new ScrollPane();
        peonGearScroll.fitToWidthProperty();
        peonGearScroll.setMaxHeight(150);
        peonGearScroll.setMaxWidth(120);

        GridPane peonGear = new GridPane();
        peonGear.setAlignment(Pos.CENTER);

        int j = 0;
        while (j < 15) {
            Button button = new Button("FakeInv" + j);
            button.setPrefWidth(85);
            peonGear.add(button, 0, j);
            Label label = new Label(String.format(" %02d ", j));
            label.minWidth(40);
            peonGear.add(label, 1, j);
            j++;
        }
        peonGear.getStylesheets().add("css/store/ItemButtons.css");
        peonGearScroll.setContent(peonGear);

        gearMenu.getChildren().add(storeGearScroll);


        gearMenu.getChildren().add(peonGearScroll);
        context.getChildren().add(gearMenu);
        menu.getChildren().add(context);
        ContextAreaHandler.getInstance().addContext("menu", menu).setContext("menu");
        //gear menu
    }

    // For the debug menu peons will come through the market/store
    public void addPeon() {
        Button peonButton = new Button("Add debug peons to world");
        peonButton.setOnAction(event -> {
            int xpos = 0;
            randomIndex = new Random().nextInt(peonNames.length);
            World.getInstance().addEntityToWorld(new Peon(xpos, 0, 32, 32, peonNames[randomIndex]));
            World.getInstance().addEntityToWorld(new PeonMiner(50, 100, 32, 32, "Miner"));
            World.getInstance().addEntityToWorld(new PeonGather(100, 100, 32, 32, "Gather"));
            World.getInstance().addEntityToWorld(new PeonGuard(150, 100, 32, 32, "Guard"));
        });
       
        Pane menu = new Pane();
        VBox info = new VBox();
        Button button = new Button("Add Entity");

        //button.applyCss();
        ObservableList<String> list = javafx.collections.FXCollections.observableArrayList();
        list.add("Peon");
        list.add("Miner");
        list.add("Gather");
        list.add("Guard");
        list.add("Bar");
        ChoiceBox dropdown = new ChoiceBox(list);
        dropdown.getSelectionModel().selectFirst();
        button.setOnAction(event -> {
            boolean defaultOption = Boolean.TRUE;
            String string = (String) dropdown.getValue();
            // FOR THE CASE STATEMENT, SET 0,0 for the xpos & xpos. This is handled by the AddEntityOnMouseClick function!
            switch (string) {
                case "Peon":
                    AddEntityOnMouseClick.entityList(new Peon(0, 0, 32, 32, "Peon"));
                    break;
                case "Miner":
                    AddEntityOnMouseClick.entityList(new PeonMiner(0, 0, 32, 32, "Miner"));
                    break;
                case "Gather":
                    AddEntityOnMouseClick.entityList(new PeonGather(0, 0, 32, 32, "Gather"));
                    break;
                case "Guard":
                    AddEntityOnMouseClick.entityList(new PeonGuard(0, 0, 32, 32, "Guard"));
                    break;
                case "Bar":
                    AddEntityOnMouseClick.entityList(new BuildingBar(0, 30));
                    break;
                default:
                    defaultOption = Boolean.FALSE;
                    return;
            }
            if (defaultOption != Boolean.FALSE) {

                AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
                NotificationManager.notify("Build Mode Activated");
            }
        });
        info.getChildren().add(peonButton);
        info.getChildren().add(button);
        info.getChildren().add(dropdown);
        menu.getChildren().add(info);
        ContextAreaHandler.getInstance().addContext("req", menu).setContext("req");
    }

    public void addStore() {
        int xpos = (int) (Math.random() * 600) + 100;
        int buildingtype = (int) (Math.random() * 4);  //4 means max of 3
        World.getInstance().addEntityToWorld(new RequisitionStore(xpos, 30, 100, 100));
    }
    
    /**
     * Main method that generates the random events menu
     */
    public void eventsMenu() {
        Pane main = new Pane();
        VBox box = new VBox();
        box.setSpacing(10);
        //On off button
        Button randomToggle = new Button("Toggle Random Events");
        
        randomToggle.setOnAction(event -> {
        	RandomEvents.toggleEnabled();
            if (RandomEvents.isEnabled()){
    			NotificationManager.notify("Random Events ON");
    		} else {
    			NotificationManager.notify("Random Events OFF");
    		}
        });
        
        //Drop down to select event to spawn
        ObservableList<String> list = javafx.collections.FXCollections.observableArrayList();
        list.add("Peon Set Spawn");
        list.add("Peon Recovery");
        list.add("Earthquake");
        list.add("Random Peon Damage");
        list.add("Random Peon");
        list.add("Peon Genocide");
        list.add("Peon Speed Boost");
        list.add("Rope Upgrade");
        list.add("Ladder Upgrade");
        list.add("Luck Buff");
        list.add("Zombie Virus");
        list.add("Zombie Cleanse");
        list.add("Zombie Single Spawn");
        list.add("Zombie Multi Spawn");
        
        ChoiceBox events = new ChoiceBox(list);
        
        //Button to execute selected event
        Button randomExecute = new Button("Generate Event");
        
        randomExecute.setOnAction(event -> {
        	String selected = (String)events.getValue();
        	int i = (int)list.indexOf(selected);
        	if (i == -1) {
        		NotificationManager.notify("Please select an event to generate");
        	} else {
        		RandomEvents.executeDebugEvent(i);
        	}
        });
        
        box.getChildren().add(randomToggle);
        box.getChildren().add(events);
        box.getChildren().add(randomExecute);
        main.getChildren().add(box);
        ContextAreaHandler.getInstance().addContext("rando", main).setContext("rando");
    }

    public void ReqMenu() {
        Pane menu = new Pane();
        VBox info = new VBox();
        Button button = new Button("Add Entity");

        //button.applyCss();
        ObservableList<String> list = javafx.collections.FXCollections.observableArrayList();
        list.add("Peon");
        list.add("Bar");
        ChoiceBox dropdown = new ChoiceBox(list);
        dropdown.getSelectionModel().selectFirst();
        //	ComboBox combo = new ComboBox(list);
        button.setOnAction(event -> {
            boolean defaultOption = Boolean.TRUE;
            String string = (String) dropdown.getValue();
            switch (string) {
                case "Peon":
//                    this.mouseHandler.setType(0);
                    NotificationManager.notify("Adding Peon");
                    break;
                case "Bar":
//                    this.mouseHandler.setType(1);
                    NotificationManager.notify("Adding Bar");
                    break;
                default:
                    defaultOption = Boolean.FALSE;
                    return;
            }
            if (defaultOption != Boolean.FALSE) {

                AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
                NotificationManager.notify("Build Mode Activated");
            }
        });
        info.getChildren().add(button);
        info.getChildren().add(dropdown);
        menu.getChildren().add(info);
        ContextAreaHandler.getInstance().addContext("req", menu).setContext("req");
    }

    public void addZombie() {
        int xpos = (int) (Math.random() * 600);
        World.getInstance().addEntityToWorld(new ZombieMob(xpos, 100, 20, 42, 42));
    }

    public static void toggleDebug() {
    	if (debug == true) {
    		debug = false;
    	} else {
    		debug = true;
    	}
    }
    
    public static boolean getDebug() {
    	return debug;
    }
}

