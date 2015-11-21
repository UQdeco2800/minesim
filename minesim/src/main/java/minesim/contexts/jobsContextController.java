package minesim.contexts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import minesim.World;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Handles all the actual data that is put into the peon status pane, by
 * preparing all the data to be put into the fxml file. Here, the elements
 * from the View (fxml made in scene builder) are grabbed to have values put
 * into them.
 *
 * If this were a MVC, this would be the Model.
 *
 * @author James
 */
public class jobsContextController implements Initializable {

    private static final Logger LOG = Logger.getLogger(defaultContextController.class);

    private JobsContextControllerHandler handler = null;
    
    private String[] allTasks;
    private String[] genericTasks;
    private String[] minerTasks;
    private String[] guardTasks;
    private String[] gathererTasks;
    
    @FXML
    private ListView genericJobsList;
    @FXML
    private ListView minerJobsList;
    @FXML    
    private ListView guardJobsList;
    @FXML    
    private ListView gathererJobsList;
    

    /**
     * Empty constructor class for controller. Does nothing.
     * @author Abicith
     */
    public jobsContextController() {
    }

    /**
     * Sets the controller handler to be this class's handler
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = new JobsContextControllerHandler(this);
    }

    /**
     * Initialises tasks
     */
    public void initializeTasks() {
        allTasks = World.getInstance().worldTasksToString().split("]");
        genericTasks = allTasks[0].split("-");
        minerTasks = allTasks[1].split("-");
        guardTasks = allTasks[2].split("-");
        gathererTasks = allTasks[3].split("-");
    }

    /**
     * Set tasks
     */
    public void setTasks() {
        initializeTasks();
        setGenericTasks();
        setMinerTasks();
        setGuardTasks();
        setGathererTasks();
    }

    /**
     * Even more setting of tasks
     */
    public void setGenericTasks() {
        if((genericTasks == null) || (genericTasks.length < 1)) {
            return;
        }
        ObservableList data = FXCollections.observableArrayList();
        for(int i = 0; i < genericTasks.length; i++) {
            data.add(genericTasks[i]);
        }
        genericJobsList.setItems(data);
    }

    /**
     * Set miner tasks
     */
    public void setMinerTasks() {
        if((minerTasks == null) || (minerTasks.length < 1)) {
            return;
        }
        ObservableList data = FXCollections.observableArrayList();
        for(int i = 0; i < minerTasks.length; i++) {
            data.add(minerTasks[i]);
        }
        minerJobsList.setItems(data);
    }

    /**
     * Set guard tasks
     */
    public void setGuardTasks() {
        if((guardTasks == null) || (guardTasks.length < 1)) {
            return;
        }
        ObservableList data = FXCollections.observableArrayList();
        for(int i = 0; i < guardTasks.length; i++) {
            data.add(guardTasks[i]);
        }
        guardJobsList.setItems(data);
    }

    /**
     * Set gatherer tasks
     */
    public void setGathererTasks() {
        if((gathererTasks == null) || (gathererTasks.length < 1)) {
            return;
        }
        ObservableList data = FXCollections.observableArrayList();
        for(int i = 0; i < gathererTasks.length; i++) {
            data.add(gathererTasks[i]);
        }
        gathererJobsList.setItems(data);
    }

}
