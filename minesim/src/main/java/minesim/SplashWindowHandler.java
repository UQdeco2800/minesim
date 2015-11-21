package minesim;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import minesim.LoginDatabaseHandler;
import minesim.inputhandlers.KeyboardHandler;

import minesim.controllers.DebugDumpController;
import minesim.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Michael on 25/09/2015. Modified by Connor and Daya.
 */
public class SplashWindowHandler implements Initializable {
	// The mute music control (muted, not muted)
	private boolean muteMusicState = false;
	// The loop music control (looping, not looping)
	private boolean loopMusicState = true;
	// The volume music control (loud, soft)
	private double volumeMusicValue = 1.0;
	// The volume sound effects control (loud, soft)
	private double volumeSFXValue = 1.0;
	// The mute sfx control (muted, not muted)
	private boolean muteSFXState = false;
	// The resolution width (for the game canvas)
	private int resolutionWidth = 800;
	// The resolution height (for the game canvas)
	private int resolutionHeight = 600;
	// The screen size for the game canvas
	private ResolutionType desiredResolution = new ResolutionType(resolutionWidth, resolutionHeight);
	// The mode control used for debugging purposes (debug mode, not debug mode)
	private boolean debugState = false;
	// The personal settings which will be saved out and loaded in, for
	// persistence
	private ArrayList<String> settings = new ArrayList<String>();
	// Logger which will display a message a based on the error
	private static final Logger LOGGER = LoggerFactory.getLogger(SplashWindowHandler.class);

	// Declaring the fx objects that will be modified
	@FXML
	Label userLabel, passwordLabel, errorLabel;
	@FXML
	PasswordField passwordField;
	@FXML
	VBox loginVBox, gameTitleVBox, mainMenuVBox, settingsMenuVBox, audioSettingsVBox, controlSettingsVBox;
	@FXML
	Button loginButton, playButton, loadButton, settingsButton, audioSettingsButton, videoSettingsButton,
			backSettingsButton, controlSettingsButton, quitButton;
	@FXML
	CheckBox muteMusic, loopMusic, muteSFX, debugButton;
	@FXML
	Slider volumeMusic, volumeSFX;
	@FXML
	ChoiceBox<ResolutionType> resolutionSelection;
	@FXML
	TextField userField, keyPad1, keyPad2, keyPad3, keyPad4, keyPad5, keyPad6, keyPad7, keyPad8, keyPad9;

	/**
	 * A private helper function which sets all the setting sub menus to not be
	 * visible.
	 */
	private void clearSettingsVboxs() {
		audioSettingsVBox.setVisible(false);
		audioSettingsVBox.managedProperty().bind(audioSettingsVBox.visibleProperty());
		controlSettingsVBox.setVisible(false);
		controlSettingsVBox.managedProperty().bind(controlSettingsVBox.visibleProperty());
	}

	/**
	 * A function which sets up the button/checkbox handlers.
	 * 
	 * @param location
	 *            The location used to resolve relative paths for the root
	 *            object, or null if the location is not known.
	 * @param resources
	 *            The resources used to localize the root object, or null if the
	 *            root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		keyPad1.setDisable(true);
		keyPad2.setDisable(true);
		keyPad3.setDisable(true);
		keyPad4.setDisable(true);
		keyPad5.setDisable(true);
		keyPad6.setDisable(true);
		keyPad7.setDisable(true);
		keyPad8.setDisable(true);
		keyPad9.setDisable(true);

		// Making sure vboxes will display according to their visibility
		// properties
		loginVBox.managedProperty().bind(loginVBox.visibleProperty());
		gameTitleVBox.managedProperty().bind(gameTitleVBox.visibleProperty());
		mainMenuVBox.managedProperty().bind(mainMenuVBox.visibleProperty());
		settingsMenuVBox.managedProperty().bind(settingsMenuVBox.visibleProperty());
		audioSettingsVBox.managedProperty().bind(audioSettingsVBox.visibleProperty());
		controlSettingsVBox.managedProperty().bind(controlSettingsVBox.visibleProperty());

		/**
		 * Sets the error message to be invisible, and sets the username.
		 */
		userField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				LOGGER.info("WE JUST SET THE USERNAME");
				errorLabel.setVisible(false);
			}
		});

		/**
		 * Hides the warning label when changing the text of the username.
		 */
		userField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				errorLabel.setVisible(false);
			}
		});

		/**
		 * Hides the warning label when changing the text of the password.
		 */
		passwordField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				errorLabel.setVisible(false);
			}
		});

		/**
		 * Sets the error message to the invisible, and sets the password.
		 */
		passwordField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				LOGGER.info("WE JUST SET THE PASSWORD");
				errorLabel.setVisible(false);
			}
		});

		/**
		 * Advances to the settings page, if both the username and password have
		 * been filled out. The saved settings are also loaded (default for a
		 * new player, and saved settings for returning user).
		 */
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				// One of these is empty
				if (passwordField.getText().equals("") || userField.getText().equals("")) {
					errorLabel.setVisible(true);
				} else {
					// Add the login to the database
					LoginDatabaseHandler.registerLogin(userField.getText(), passwordField.getText(), muteMusicState,
							loopMusicState, muteSFXState, volumeMusicValue, volumeSFXValue, resolutionWidth,
							resolutionHeight, debugState);
					LOGGER.info("ROWS: " + LoginDatabaseHandler.checkRows());
					// Making sure vboxes will display according to their
					// visibility properties
					loginVBox.setVisible(false);
					loginVBox.managedProperty().bind(loginVBox.visibleProperty());

					gameTitleVBox.setVisible(true);
					gameTitleVBox.managedProperty().bind(gameTitleVBox.visibleProperty());

					mainMenuVBox.setVisible(true);
					mainMenuVBox.managedProperty().bind(gameTitleVBox.visibleProperty());

					// Set all settings to display personal saved settings
					setPersistentSettings();
				}
			}
		});

		/**
		 * Sets the play button to begin the game when fired
		 */
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Stage primaryStage = (Stage) playButton.getScene().getWindow();

				Parent splash = null;
				try {
					splash = FXMLLoader.load(getClass().getResource("/layouts/MineSim.fxml"));
				} catch (IOException e) {
					LOGGER.error("Could not understand file: ", e);
					System.exit(-1);
					e.printStackTrace();
				}

				splash.setOnKeyPressed(KeyboardHandler.getInstance());

				Scene scene = new Scene(splash);
				primaryStage.setScene(scene);
				MineSim.RESOLUTION_WIDTH = desiredResolution.width;
				MineSim.RESOLUTION_HEIGHT = desiredResolution.height;
				primaryStage.setHeight(MineSim.RESOLUTION_HEIGHT);
				primaryStage.setWidth(MineSim.RESOLUTION_WIDTH);
				World.getInstance().setDimensions(MineSim.RESOLUTION_WIDTH, MineSim.RESOLUTION_HEIGHT);
				primaryStage.show();
			}
		});

		/**
		 * Sets the settings button to display the settings menu when fired and
		 * hide the splash screen menu
		 */
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				gameTitleVBox.setVisible(false);
				gameTitleVBox.managedProperty().bind(gameTitleVBox.visibleProperty());

				mainMenuVBox.setVisible(false);
				mainMenuVBox.managedProperty().bind(mainMenuVBox.visibleProperty());

				settingsMenuVBox.setVisible(true);
				settingsMenuVBox.managedProperty().bind(settingsMenuVBox.visibleProperty());

				audioSettingsVBox.setVisible(true);
				audioSettingsVBox.managedProperty().bind(settingsMenuVBox.visibleProperty());
			}
		});

		/**
		 * Meant to load game state so that play could save progress and resume.
		 * Currently is a placeholder.
		 */
		loadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				// DOES NOTHING
			}
		});

		/**
		 * Sets the audio settings button to open the audio settings menu when
		 * fired and modify the visibility of the sibling menus so that this
		 * menu is the only one visible
		 */
		audioSettingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				clearSettingsVboxs();
				audioSettingsVBox.setVisible(true);
				audioSettingsVBox.managedProperty().bind(settingsMenuVBox.visibleProperty());
			}
		});

		/**
		 * Sets the controls settings button, in the settings menu, to open the
		 * control settings menu when fired and modify the visibility of the
		 * sibling menus so that this menu is the only one visible
		 */
		controlSettingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				clearSettingsVboxs();
				controlSettingsVBox.setVisible(true);
				controlSettingsVBox.managedProperty().bind(controlSettingsVBox.visibleProperty());
			}
		});

		/**
		 * Sets the back button in the settings menu to take the user back to
		 * the main menu of the splash screen when fired
		 */
		backSettingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				settingsToString();
				LoginDatabaseHandler.updateSavedSettings(userField.getText(), passwordField.getText(), muteMusicState,
						loopMusicState, muteSFXState, volumeMusicValue, volumeSFXValue, resolutionWidth,
						resolutionHeight, debugState);
				try {
					minesim.SettingsHandler.saveSettings(settings);
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
				gameTitleVBox.setVisible(true);
				gameTitleVBox.managedProperty().bind(gameTitleVBox.visibleProperty());

				settingsMenuVBox.setVisible(false);
				settingsMenuVBox.managedProperty().bind(settingsMenuVBox.visibleProperty());

				clearSettingsVboxs();

				mainMenuVBox.setVisible(true);
				mainMenuVBox.managedProperty().bind(mainMenuVBox.visibleProperty());
			}
		});

		/**
		 * Sets the quick button to exit the application when fired. TO DO: SAVE
		 * THE DETAILS
		 */
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				settingsToString();
				LoginDatabaseHandler.updateSavedSettings(userField.getText(), passwordField.getText(), muteMusicState,
						loopMusicState, muteSFXState, volumeMusicValue, volumeSFXValue, resolutionWidth,
						resolutionHeight, debugState);
				try {
					minesim.SettingsHandler.saveSettings(settings);
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				} finally {
					System.exit(0);
				}
			}
		});

		/**
		 * Sets the debug mode when fired. NOT SURE IF PLACEHOLDER.
		 */
		debugButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				debugState = !debugState;
				DebugDumpController.toggleDebug();
			}
		});

		/**
		 * Mutes the music when fired. At the moment is a placeholder. No
		 * functionality exists.
		 */
		muteMusic.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				muteMusicState = !muteMusicState;
			}
		});

		/**
		 * Loops the music when fired. At the moment is a placeholder. No
		 * functionality exists.
		 */
		loopMusic.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				loopMusicState = !loopMusicState;
			}
		});

		/**
		 * Mutes the music sound effects when fired. At the moment is a
		 * placeholder. No functionality exists.
		 */
		muteSFX.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				muteSFXState = !muteSFXState;
			}
		});

		/**
		 * Changes the volume of the music when interacted with. At the moment
		 * is a placeholder. No functionality exists.
		 */
		volumeMusic.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging,
					Boolean changing) {
				if (!changing) {
					volumeMusicValue = volumeMusic.getValue();
				}
			}
		});

		/**
		 * Changes the volume of the music sound effects when interacted with.
		 * At the moment is a placeholder. No functionality exists.
		 */
		volumeSFX.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging,
					Boolean changing) {
				if (!changing) {
					volumeSFXValue = volumeSFX.getValue();
				}
			}
		});
	}

	/**
	 * A private helper function which gets the settings the user saved and sets
	 * the relevant audio settings.
	 */
	private void getStates() {
		muteMusicState = LoginDatabaseHandler.getSavedSettingsBoolean(userField.getText(), passwordField.getText(),
				"MUTEMUSIC");
		loopMusicState = LoginDatabaseHandler.getSavedSettingsBoolean(userField.getText(), passwordField.getText(),
				"LOOPMUSIC");
		volumeMusicValue = LoginDatabaseHandler.getSavedSettingsDouble(userField.getText(), passwordField.getText(),
				"MUSICVALUE");
		muteSFXState = LoginDatabaseHandler.getSavedSettingsBoolean(userField.getText(), passwordField.getText(),
				"MUTESFX");
		volumeSFXValue = LoginDatabaseHandler.getSavedSettingsDouble(userField.getText(), passwordField.getText(),
				"SFXVOLUME");
		resolutionWidth = LoginDatabaseHandler.getSavedSettingsInt(userField.getText(), passwordField.getText(),
				"RESOLUTIONWIDTH");
		resolutionHeight = LoginDatabaseHandler.getSavedSettingsInt(userField.getText(), passwordField.getText(),
				"RESOLUTIONHEIGHT");
		debugState = LoginDatabaseHandler.getSavedSettingsBoolean(userField.getText(), passwordField.getText(),
				"DEBUG");

		// Debug option default for admin
		if (userField.getText().equals("admin") && passwordField.getText().equals("admin")) {
			debugState = true;
			debugButton.setSelected(debugState);
		}
	}

	/**
	 * A private helper function which gets and sets the resolution.
	 */
	private void setResolution() {
		desiredResolution = new ResolutionType(resolutionWidth, resolutionHeight);

		List<ResolutionType> resolutions = new ArrayList<>();
		String[] rezs = { "640 x 480", "800 x 600", "1024 x 600", "1024 x 768", "1152 x 864", "1280 x 720",
				"1280 x 768", "1280 x 800", "1280 x 960", "1280 x 1024", "1360 x 768", "1366 x 768", "1400 x 1050",
				"1440 x 900", "1600 x 1200", "1680 x 1050", "1920 x 1080", "1920 x 1200", "2048 x 1152", "2560 x 1440",
				"2560 x 1600", "768 x 1024", "1093 x 614", "1536 x 864" };
		for (String rez : rezs) {
			resolutions.add(new ResolutionType(rez));
		}
		int defaultSelection = 0;
		for (int i = 0; i < resolutions.size(); i++) {
			if (desiredResolution.toString().equals(resolutions.get(i).toString())) {
				defaultSelection = i;
			}
		}

		resolutionSelection.getItems().addAll(resolutions);
		resolutionSelection.getSelectionModel().select(defaultSelection);
		resolutionSelection.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				desiredResolution = resolutionSelection.getItems().get((Integer) newValue);
				// Update resolution width/height
				String[] parts = desiredResolution.toString().split("x");
				resolutionWidth = Integer.parseInt(parts[0]);
				resolutionHeight = Integer.parseInt(parts[1]);
			}
		});
	}

	/**
	 * A private helper function which sets the settings the user saved.
	 */
	private void setStates() {
		muteMusic.setSelected(muteMusicState);
		loopMusic.setSelected(loopMusicState);
		muteSFX.setSelected(muteSFXState);
		volumeMusic.setValue(volumeMusicValue);
		volumeSFX.setValue(volumeSFXValue);
		debugButton.setSelected(debugState);
		setResolution();
	}

	/**
	 * A private helper function which gets and sets the saved settings of the
	 * user.
	 */
	private void setPersistentSettings() {
		getStates();
		setStates();
	}

	/**
	 * A private class for determining the resolution size. This is taken form
	 * settingsMenu
	 * 
	 * @author Michael
	 *
	 */
	private class ResolutionType {
		// The dimensions of the game screen
		private int width, height;

		/**
		 * Creates a new resolution type with specific dimensions
		 * 
		 * @param x
		 *            The specified width of the game canvas
		 * @param y
		 *            The specified height of the game canvs
		 */
		public ResolutionType(int x, int y) {
			width = x;
			height = y;
		}

		/**
		 * Creates another new resolution type with specific dimensions
		 * 
		 * @param res
		 *            the input to be broken up into width and height
		 */
		public ResolutionType(String res) {
			String[] tmplist = res.split(" ");
			width = Integer.parseInt(tmplist[0]);
			height = Integer.parseInt(tmplist[2]);
			
			
		}

		/**
		 * A method to represent the resolution dimensions
		 */
		@Override
		public String toString() {
			return width + "x" + height;
		}
	}

	/**
	 * A private function which adds to the persistent settings options the
	 * settings to be saved.
	 */
	private void settingsToString() {
		settings = new ArrayList<String>();
		settings.add("MUTEMUSIC = " + muteMusicState);
		settings.add("LOOPMUSIC = " + loopMusicState);
		settings.add("MUTESFX = " + muteSFXState);
		settings.add("MUSICVALUE = " + volumeMusicValue);
		settings.add("SFXVOLUME = " + volumeSFXValue);
		settings.add("RESOLUTION = " + desiredResolution);
		settings.add("DEBUG = " + debugState);
	}
}
