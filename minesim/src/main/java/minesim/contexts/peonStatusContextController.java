package minesim.contexts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import minesim.entities.Inventory;

import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import minesim.entities.Peon;
import minesim.World;
import minesim.controllers.PeonSelectorController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Handles all the actual data that is put into the peon status pane, by
 * preparing all the data to be put into the fxml file. Here, the elements from
 * the View (fxml made in scene builder) are grabbed to have values put into
 * them.
 *
 * If this were a MVC, this would be the Model.
 *
 * @author James
 */
public class peonStatusContextController implements Initializable {

	private static final Logger LOG = Logger
			.getLogger(defaultContextController.class);
	// Local instance of the peon being displayed in the pane
	private Peon peon;
	// The handler controls context controller by giving it the peon instance
	private PeonStatusContextControllerHandler handler = null;
	// The initial stamina figure given, which is used to provide a visual ratio
	public int maxStamina = 1000;
	// The initial health figure given, also for a visual ratio
	public int maxHP;

	// All FXML tagged vars reference the fx:id of objects in the fxml file
	@FXML
	// The label for the peon's name on the main pane
	private Label peonName = new Label("<Name>");

	@FXML
	// The label for the number amount of strength for a peon on the stats pane
	private Label strLabel;

	@FXML
	// Like strLabel, but for speed
	private Label speedLabel;

	@FXML
	// FXML label for luck
	private Label luckLabel;

	@FXML
	// Like other stat labels, but for experience
	private Label expLabel;

	@FXML
	// Label for the peon's level
	private Label lvlLabel;

	@FXML
	// Label for the peon's class, which is an unimplemented property for peons
	private Label classLabel;

	@FXML
	// The button on the main tab, that removes the selected peon
	private Button removePeonButton;

	@FXML
	// Button that centers the camera on the current peon chosen
	private Button centreOnPeonButton;

	@FXML
	private Button queryButton;

	@FXML
	// A list of notifications that apply to the peon, on the 'Actions' pane
	private ListView notificationList;

	@FXML
	// Similar to notifications list, but for peon 'thoughts'
	private ListView thoughtsList;

	@FXML
	// A second label pulling and conveying the chosen Peon's name
	private Label peonName2;

	@FXML
	// A label showing the peon's current action, which is fed from their tasks
	private Label currentAction;

	@FXML
	// Shows a numeric representation of Peon's happiness rating
	private Label happinessLabel;

	@FXML
	// Like happiness label, but for peon's mood
	private Label moodLabel;

	@FXML
	// Like mood, but for peon's annoyance figure
	private Label annoyanceLabel;

	@FXML
	// Shows the numeric figure for peon's health
	private Label healthLabel;

	@FXML
	// Shows peon's stamina rating
	private Label staminaLabel;

	@FXML
	// A progress bar that pairs with peon's health, and empties as they die
	private ProgressBar healthBar = new ProgressBar(0);

	@FXML
	// Progress bar that starts full and decreases as peon tiredness increases
	private ProgressBar staminaBar = new ProgressBar(0);

	@FXML
	// A list of a peon's inventory
	private ListView<String> inventoryList;

	@FXML
	// Progress bar from 1-10 for peon's strength rating, on stat pane
	private ProgressBar strBar;

	@FXML
	// Similar to strBar, but for speed
	private ProgressBar speedBar;

	@FXML
	// Like strBar, but for luck
	private ProgressBar luckBar;

	@FXML
	// Like stat bars, but for peon experience
	private ProgressBar expBar;

	@FXML
	private Button emptyInventoryButton;

	@FXML
	private Button dropButton;

	/**
	 * Empty constructor class for controller. Does nothing.
	 * 
	 * @author JamesG
	 */
	public peonStatusContextController() {
	}

	/**
	 * Sets the controller handler to be this class's handler
	 * 
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		handler = new PeonStatusContextControllerHandler(this);
	}

	/**
	 * Returns the locally stored instance of peon
	 * 
	 * @return local peon variable stored in this class
	 */
	public Peon getPeon() {
		return peon;
	}

	/**
	 * Sets the peon displayed to be the param's, and sets each tab pane to
	 * display this peon instances's data.Gets the peon's max health and stamina
	 * so that labels can have denominators, then executes functions that set up
	 * each pane.
	 * 
	 * @param peon
	 *            -the peon instance
	 * @author JamesG
	 */

	public void setPeon(Peon peon) {
		this.peon = peon;
		if (!peon.maxHPFound) {
			this.maxHP = peon.getHealth();
			peon.maxHPFound = true;
		}

		mainMenuSetup(peon);
		mainMenuButtonInit(peon);
		setInventoryPane(peon);
		getStats(peon);
		setupMoodPane(peon);
		setNotificationsPane(peon);
		setUpThoughtsPane(peon);
	}

	/**
	 * Sets up the items in the main pane to convey the peon's relevant info. As
	 * of now, this pane and the rest do not update in real time, so the peon
	 * needs to be clicked on again for any of the figures to be updated. This
	 * will be fixed with listeners.
	 *
	 * @param peon
	 * @author JamesG
	 */
	public void mainMenuSetup(Peon peon) {
		peonName.setText(peon.getName());
		healthLabel.setText(String.valueOf(peon.getHealth()) + "/"
				+ String.valueOf(this.maxHP));
		/*
		 * Health starts at 100, tiredness starts at 0, so the figures need to
		 * be slightly messed with so that they appear to be a depleting
		 * quantity on the progress bars
		 */
		staminaLabel.setText(String.valueOf(((1000 - peon.getTiredness()))
				+ "/1000"));
		// System.out.println("---" + ((double) maxStamina -
		// (peon.getTiredness())) + "/" + maxStamina);
		// System.out.println("guh " + ((double) (peon.getHealth()/100)));
		staminaBar.setStyle("-fx-accent: blue;");
		healthBar.setStyle("-fx-accent: green;");
		healthBar.setProgress((double) (peon.getHealth() / 100));
		staminaBar
				.setProgress(((double) maxStamina - (peon.getTiredness())) / 1000);
		currentAction.setText(peon.strOfTask);
	}

	/**
	 * Initialises the three buttons on the main panel, which are for camera,
	 * dismissing peons, and a 3rd mystery button without a function yet. each
	 * button has an event handler that conducts the appropriate action
	 * described on the label.
	 * 
	 * @param peon
	 *            -the peon the panel applies to
	 * @author JamesG
	 */
	public void mainMenuButtonInit(Peon peon) {
		removePeonButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				World.getInstance().removeEntityFromWorld(getPeon());
				handler.removePeonStatusPane();

			}
		});
		centreOnPeonButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Function for centering camera on peon
				moveCamera(peon, peon.getXpos(), peon.getYpos());
			}
		});
		queryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Unsure what this function will do!
			}
		});
	}

	/**
	 * Initialises the buttons and their event handlers for the inventory pane,
	 * where the remove item button removes an item from the inventory list view
	 * if an index is selected from it, and the emptyInventoryButton simply
	 * clears the inventory.
	 * 
	 * @param peon
	 * @author JamesG
	 */
	public void inventoryButtonInit(Peon peon) {
		emptyInventoryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				while (peon.getInventory().getInventorySize() != 0) {
					peon.dropItem(peon.getInventory().getInventory().get(0)
							.getItem(), true);
				}
				setPeon(peon);
			}
		});
		dropButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (inventoryList.getSelectionModel() != null
						&& peon.getInventory().getInventorySize() > inventoryList
								.getSelectionModel().getSelectedIndex()
						&& inventoryList.getSelectionModel().getSelectedIndex() >= 0) {
					peon.dropItem(
							peon.getInventory()
									.getInventory()
									.get(inventoryList.getSelectionModel()
											.getSelectedIndex()).getItem(),
							true);
					setPeon(peon);
					// PeonStatusContextControllerHandler.getInstance().showPeonStatus(this.peon);
				}
			}
		});
	}

	/**
	 * Sets up the listview inventoryList that displays a list of the peon's
	 * inventory, if they have items.
	 * 
	 * @param peon
	 * @author JamesG
	 */
	public void setInventoryPane(Peon peon) {
		ObservableList<String> data = FXCollections.observableArrayList();
		inventoryList.setItems(data);
		for (int i = 0; i < peon.getInventory().getInventorySize(); i++) {
			data.add(peon.getInventory().getInventory().get(i).toString()
					+ "     ("
					+ peon.getInventory().getInventory().get(i).getAmount()
					+ ")");
		}
		inventoryButtonInit(peon);
	}

	/**
	 * A copied function from PeonSelectorControllers, as that function could
	 * not be called from the class properly. This function is called whenever
	 * the camera is called to focus on the peon in the buttonInit event handler
	 * 
	 * @param peon
	 *            -the peon selected
	 * @param xpos
	 *            -the x-position of the camera's focus
	 * @param ypos
	 *            -the y-position of the camera's focus
	 * @return true
	 * @author
	 */
	public boolean moveCamera(Peon peon, int xpos, int ypos) {
		int currentX = World.getInstance().getXOffset();
		int currentY = World.getInstance().getYOffset();
		int center;
		if (xpos > currentX) {
			center = currentX + 360;
			while (xpos > center) {
				World.getInstance().moveScreenRight(1);
				center++;
			}
		} else if (xpos < currentX) {
			center = currentX + 360;
			while (xpos < center) {
				World.getInstance().moveScreenLeft(1);
				center--;
			}
		}
		if (ypos < currentY) {
			center = currentY + 360;
			while (ypos < center) {
				World.getInstance().moveScreenUp(1);
				center--;
			}
		} else if (ypos > currentY) {
			center = currentY + 360;
			while (ypos > center) {
				World.getInstance().moveScreenDown(1);
				center++;
			}
		}
		return true;
	}

	/**
	 * Sets up a label for the stat name, a label for its number, and a progress
	 * bar representing the stat's number, for the peon given in the argument.
	 * Since progress bars use a double within a range of 0 to 1 to display the
	 * filling, many of the stats had to be manipulated so that the int of the
	 * stat given would be represented on a scale from 0 to 1. Each stat also
	 * has tacky colour coding just for visual categorisation amongst each stat.
	 * 
	 * @param peon
	 *            -the peon who's information is displayed
	 */
	public void getStats(Peon peon) {
		// Stat labels
		strLabel.setText(Integer.toString(peon.getStrength()));
		speedLabel.setText(Integer.toString(peon.getSpeed()));
		luckLabel.setText(Integer.toString(peon.getLuck()));
		expLabel.setText(Integer.toString(peon.getExperience()) + "/" + Integer.toString(peon.experienceRequired()));
		lvlLabel.setText(Integer.toString(peon.getLevel()));
		classLabel.setText(peon.getClass().toString());
		// Stat bar
		strBar.setProgress((double) peon.getStrength() / 10);
		// System.out.println("Str is " + peon.getStrength() + "Or: " +
		// ((double) peon.getStrength()/10) + "StrDob: " + strDouble);
		// strBar.setProgress(0.1);
		speedBar.setProgress((double) peon.getSpeed() / 10);
		luckBar.setProgress((double) peon.getLuck() / 10);
		expBar.setProgress((double) peon.getExperience()
				/ peon.experienceRequired());
		// Stat bar colours
		strBar.setStyle("-fx-accent: red;");
		speedBar.setStyle("-fx-accent: lightblue;");
		luckBar.setStyle("fx-accent: yellow");
		expBar.setStyle("fx-accent: black");
	}

	/**
	 * Initialises the notifications pane where actions relevent to the peon
	 * given as the param, with a listview containing a list of all
	 * notifications by getting a list of notifications represented in a string
	 * array, and adding those to the listview.
	 * 
	 * @param peon
	 *            -the peon selected
	 * @author JamesG
	 */
	public void setNotificationsPane(Peon peon) {
		ObservableList data = FXCollections.observableArrayList();
		List<String> notificationArray = new ArrayList<String>();
		if (peon.getNotificationList() == null) {
			return;
		}
		for (String action : peon.getNotificationList()) {
			data.add(action);
		}
		notificationList.setItems(data);
	}

	/**
	 * Gets the string return of a peon's mood state, and sets the annoyance and
	 * happiness labels to show the int representation of the selected peon.
	 * 
	 * @param peon
	 *            -the peon selected
	 * @author JamesG
	 */
	public void setupMoodPane(Peon peon) {
		moodLabel.setText(getMoodState(peon));
		annoyanceLabel.setText(String.valueOf(peon.getAnnoyance()));
		happinessLabel.setText(String.valueOf(peon.getHappiness()));
	}

	/**
	 * Gets the mood of a peon, and depending on the range it is from 1 to 100,
	 * with 1 being the floor and 100 being the ceiling happinnes, and returns a
	 * string representing the current state, which is more indicative to the
	 * user since they won't know if 1 happiness represents maximised or
	 * minimised happiness, so this helps.
	 * 
	 * @param peon
	 * @return String conveying mood
	 */
	private String getMoodState(Peon peon) {
		int happiness = peon.getHappiness();
		String moodState = "<Mood not found>";
		if (happiness >= 75) {
			moodState = "Happy";
		} else if (happiness < 75 && happiness > 50) {
			moodState = "Content";
		} else if (happiness < 50 && happiness > 25) {
			moodState = "Unhappy";
		} else {
			moodState = "Miserable";
		}
		return moodState;
	}

	/**
	 * Almost identical in structure to the notifications pane, except for peon
	 * "thoughts", which aren't a feature yet.
	 * 
	 * @param peon
	 *            -peon selected
	 * @author JamesG
	 */
	public void setUpThoughtsPane(Peon peon) {
		ObservableList data = FXCollections.observableArrayList();
		List<String> thoughtsArray = new ArrayList<String>();
		if (peon.getThoughtsList() == null) {
			data.add("test1");
			data.add("test2");
			data.add("test3");
			thoughtsList.setItems(data);
			peonName2.setText(peon.getName());
			return;
		}
		for (String action : peon.getThoughtsList()) {
			data.add(action);
		}
		thoughtsList.setItems(data);
		peonName2.setText(peon.getName());
	}
}
