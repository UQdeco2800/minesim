package minesim.controllers;

import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import minesim.Music;
import minesim.Sound;
import minesim.util.Settings;

/**
 * Created by Michael on 18/10/2015.
 */
public class settingsMenuController implements Initializable {
    private static final Logger LOG =
            Logger.getLogger(settingsMenuController.class);
    
    

    private boolean muteMusicState = false;
    private boolean loopMusicState = true;
	
	private double volumeMusicValue = 1.0;
	private double volumeSFXValue = 1.0;
	
	private boolean muteSFXState = false;
	
	private ResolutionType desiredResolution = new ResolutionType(640,480);


    private HashMap<String, String> settings = new HashMap<>();

    private ArrayList<String> savedSettings = new ArrayList<String>();

    @FXML
    private CheckBox musicMuteCheck;
    @FXML
    private CheckBox musicLoopCheck;
    @FXML
    private CheckBox soundMuteCheck;
    @FXML
    private Slider musicSlider;
    @FXML
    private Slider soundSlider;
    @FXML
    private ChoiceBox<ResolutionType> resolutionBox;

    public void updateSettings(){
    	
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO Save positions of buttons/checks to static elements and recall them if this isn't the first time we've init'd
    	savedSettings.clear();
    	//Sets our settings
        persistentSettings();
        
    	musicMuteCheck.setSelected(muteMusicState);
    	musicLoopCheck.setSelected(loopMusicState);
    	soundMuteCheck.setSelected(muteSFXState);
    	musicSlider.setValue(volumeMusicValue);
    	soundSlider.setValue(volumeSFXValue);
    	
    	
    	savedSettings.add("MUTEMUSIC = " + muteMusicState); //0 
    	savedSettings.add("LOOPMUSIC = " + loopMusicState); //1
    	savedSettings.add("MUTESFX = " + muteSFXState); //2 
    	savedSettings.add("MUSICVALUE = " + volumeMusicValue); //3
    	savedSettings.add("SFXVOLUME = "+ volumeSFXValue); //4
        savedSettings.add("RESOLUTION = " + settings.get(Settings.RESOLUTION));
        savedSettings.add("DEBUG = " + settings.get(Settings.DEBUG));

    	
    	//System.out.println(savedSettings);
    	
    	
    	
        musicMuteCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                	//setMusicMute
                	//Music.getInstance().setMusicMute(muteMusicState);
                    Music.getInstance().musicMute();
                    muteMusicState = !muteMusicState;
                    savedSettings.remove(0);
                    savedSettings.add(0, "MUTEMUSIC = " + muteMusicState);
                    minesim.SettingsHandler.saveSettings(savedSettings);
                } catch (InterruptedException e) {
                    LOG.error("Failed to mute music", e);
                } catch (IOException e){
                	LOG.error("Problem Loading File");
                }
            }
        });

        musicLoopCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	loopMusicState = !loopMusicState;
            	savedSettings.remove(1);
            	savedSettings.add(1, "LOOPMUSIC = " + loopMusicState);
                Music.getInstance().tickMusicLoop();
            }
        });

        musicSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean changing) {
                if (!changing) {
                	try{
	                    Music.getInstance().setVolume(musicSlider.getValue());
	                    volumeMusicValue = Music.getInstance().getVolume();
	                    savedSettings.remove(3);
	                    savedSettings.add(3,"MUSICVALUE = " + volumeMusicValue);
	                    minesim.SettingsHandler.saveSettings(savedSettings);
                	} catch (IOException e){
                		LOG.error("Problem Saving File");
                	}
                }
            }
        });

        soundSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean changing) {
                if (!changing) {
                	try{
	                	Sound.setVolume(soundSlider.getValue());
	                	volumeSFXValue = Sound.getVolume();
	                	savedSettings.remove(4);
	                	savedSettings.add(4, "SFXVOLUME = "+ volumeSFXValue);
	                	minesim.SettingsHandler.saveSettings(savedSettings);
                	} catch (IOException e) {
                		LOG.error("Problem Saving File");
                	}
                }
            }
        });

        soundMuteCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	try{
	            	muteSFXState = !muteSFXState;
	            	savedSettings.remove(2);
	            	savedSettings.add(2, "MUTESFX = "+ muteSFXState);
	            	Sound.setMute(muteSFXState);
	            	minesim.SettingsHandler.saveSettings(savedSettings);
            	} catch (IOException e){
            		LOG.error("Problem Saving File");
            	}
            }
        });

        List<ResolutionType> resolutions = new ArrayList<>();
        String[] rezs = {"640 x 480", "800 x 600", "1024 x 600", "1024 x 768", "1152 x 864",
                "1280 x 720", "1280 x 768", "1280 x 800", "1280 x 960",
                "1280 x 1024", "1360 x 768", "1366 x 768", "1400 x 1050",
                "1440 x 900", "1600 x 1200", "1680 x 1050", "1920 x 1080",
                "1920 x 1200", "2048 x 1152", "2560 x 1440", "2560 x 1600",
                "768 x 1024", "1093 x 614", "1536 x 864"};
        for (String rez : rezs) {
            resolutions.add(new ResolutionType(rez));
        }
        //This checks if the desired resolution matches any of the resolutions and makes that one the default selection in the menu
        int defaultSelection = 0;
        for(int i = 0; i < resolutions.size(); i++){
        	if(desiredResolution.toString().equals(resolutions.get(i).toString())){
        		defaultSelection = i;
        	}
        }
        resolutionBox.getItems().addAll(resolutions);
        resolutionBox.getSelectionModel().select(defaultSelection);
        resolutionBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changeWindowSize(resolutionBox.getItems().get((Integer) newValue));
            }
        });
    }

    private void changeWindowSize(ResolutionType resolutionType) {
        Stage stage = (Stage) resolutionBox.getScene().getWindow();
        stage.setHeight(resolutionType.height);
        stage.setWidth(resolutionType.width);
    }

    private class ResolutionType {
        private int width, height;

        public ResolutionType(int x, int y) {
            width = x;
            height = y;
        }

        public ResolutionType(String res) {
            String[] tmplist = res.split(" ");
            width = Integer.parseInt(tmplist[0]);
            height = Integer.parseInt(tmplist[2]);
        }

        @Override
        public String toString() {
            return width + "x" + height;
        }
    }
    /**
     * Loads in the persistent settings and sets all the states to the state they should be
     */
    private void persistentSettings(){
		settings = minesim.SettingsHandler.loadSettings();

        muteMusicState = Boolean.parseBoolean(settings.get(Settings.MUTE_MUSIC));
        loopMusicState = Boolean.parseBoolean(settings.get(Settings.LOOP_MUSIC));
        muteSFXState = Boolean.parseBoolean(settings.get(Settings.MUTE_SFX));

        volumeMusicValue = Double.parseDouble(settings.get(Settings.MUSIC_VALUE));
        volumeSFXValue = Double.parseDouble(settings.get(Settings.SFX_VOLUME));
        String[] parts = settings.get(Settings.RESOLUTION).split("x");
        desiredResolution = new ResolutionType(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));

    }
}
