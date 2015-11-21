package minesim.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import minesim.World;
import minesim.entities.BuildingBar;
import minesim.entities.BuildingBlacksmith;
import minesim.entities.BuildingGym;
import minesim.entities.BuildingHospital;
import minesim.entities.BuildingNoodlehaus;
import minesim.entities.BuildingTeleporterIn;
import minesim.entities.BuildingTeleporterOut;
import minesim.entities.BuildingWell;
import minesim.functions.AddEntityOnMouseClick;
import minesim.functions.DisplayBuildingInstructions;
import minesim.menu.BuildingMenu;

/**
 * Created by Team Cook
 */
public class BuildingMenuController implements Initializable{
    /*
     * Add gym building type
     */
    @FXML
    public void addGym(ActionEvent event) {
    	new BuildingMenu().hideMenu();
    	DisplayBuildingInstructions.showBuildingInstr();
    	AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
    	AddEntityOnMouseClick.entityList(new BuildingGym(0, 0));
    	/*
        int xpos = (int) (Math.random() * 600) + 100;
        World.getInstance().addEntityToWorld(new BuildingGym(xpos, 30));
        */
    }

    /*
     * Add well building type
     */
    @FXML
    public void addWell(ActionEvent event) {
    	new BuildingMenu().hideMenu();
    	DisplayBuildingInstructions.showBuildingInstr();
    	AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
    	AddEntityOnMouseClick.entityList(new BuildingWell(0, 0));
        
    }

    /*
     * Add bar building type
     */
    @FXML
    public void addBar(ActionEvent event) {
    	new BuildingMenu().hideMenu();
    	DisplayBuildingInstructions.showBuildingInstr();
    	AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
    	AddEntityOnMouseClick.entityList(new BuildingBar(0, 0));
    }

    /*
     * Add blacksmith building type
     */
    @FXML
    public void addBlackSmith(ActionEvent event) {
    	new BuildingMenu().hideMenu();
    	DisplayBuildingInstructions.showBuildingInstr();
    	AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
    	AddEntityOnMouseClick.entityList(new BuildingBlacksmith(0, 0));
    }

    /*
     * Add noodle house building type
     */
    @FXML
    public void addNoodleHaus(ActionEvent event) {
    	new BuildingMenu().hideMenu();
    	DisplayBuildingInstructions.showBuildingInstr();
    	AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
    	AddEntityOnMouseClick.entityList(new BuildingNoodlehaus(0, 0));
    }

    /*
     * Add teleporter building type
     */
    @FXML
    public void addTeleporter(ActionEvent event) {
        if (!World.getInstance().worldContainsTeleporterIn()) {
        	new BuildingMenu().hideMenu();
        	DisplayBuildingInstructions.showBuildingInstr();
        	AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
        	AddEntityOnMouseClick.entityList(new BuildingTeleporterIn(0, 0));
        }
        if (!World.getInstance().worldContainsTeleporterOut()) {
        	new BuildingMenu().hideMenu();
        	DisplayBuildingInstructions.showBuildingInstr();
        	AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
        	AddEntityOnMouseClick.entityList(new BuildingTeleporterOut(0, 0));
        	
        }

    }
    
    /*
     * Add Hospital building type
     */
    @FXML
    public void addHospital(ActionEvent event) {
    	new BuildingMenu().hideMenu();
    	DisplayBuildingInstructions.showBuildingInstr();
    	AddEntityOnMouseClick.setBuildMode(Boolean.TRUE);
    	AddEntityOnMouseClick.entityList(new BuildingHospital(0, 0));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Nothing needed here
    }
}
