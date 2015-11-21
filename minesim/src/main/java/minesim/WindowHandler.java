package minesim;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import minesim.contexts.JobsContextControllerHandler;
import minesim.contexts.TransportContextControllerHandler;
import minesim.controllers.DebugDumpController;
import minesim.entities.BatMob;
import minesim.entities.Peon;
import minesim.entities.RequisitionStore;
import minesim.entities.ZombieMob;
import minesim.events.tracker.*;
import minesim.inputhandlers.KeyboardHandler;
import minesim.inputhandlers.MouseHandler;
import minesim.menu.*;
import notifications.NotificationView;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InvalidClassException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WindowHandler implements Initializable {
    private static final Logger LOG = Logger.getLogger(WindowHandler.class);
    
    Sound btnMain = new Sound("btnmain.mp3");
    Sound btnSecondary = new Sound("btnsecondary.mp3");
    Boolean firstNight = true;
    Boolean firstAction = true;
    Boolean firstDay = true;
    Boolean firstMarket = true;

    private Boolean soundContextAdded = false;
    private MouseHandler mouseHandler;
    private KeyboardHandler keyboardHandler;
    private Boolean instructionsAdded = false;

    @FXML
    private StackPane bossPane;
    @FXML
    private Button viewTasksButton;
    @FXML
    private Button openMenuButton;
    @FXML
    private Button viewMarketButton;
    @FXML
    private Button transportButton;
    @FXML
    private Button buildingOptionsButton;
    @FXML
    private Button openMarketButton;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private StackPane contextArea;
    @FXML
    private ListView<String> notifDisplay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResizableCanvas canvas = new ResizableCanvas();
        rightPane.getChildren().add(canvas);
        canvas.bindTo(rightPane);

        World mainworld = World.getInstance();
        
        addInstructions();

        this.mouseHandler = new MouseHandler(mainworld);
        this.keyboardHandler = KeyboardHandler.getInstance();
        keyboardHandler.setGameWorld(mainworld);
        rightPane.setOnMouseClicked(mouseHandler);
        rightPane.setOnMousePressed(mouseHandler);
        rightPane.setOnMouseReleased(mouseHandler);
        rightPane.setOnMouseDragged(mouseHandler);
        rightPane.setOnMouseMoved(mouseHandler);
        rightPane.setOnMouseExited(mouseHandler);
        rightPane.setOnKeyPressed(keyboardHandler);

        KeyboardHandler.getInstance().setGameWorld(mainworld);
        KeyboardHandler.getInstance().setMouseHandler(mouseHandler);

        NotificationView.getInstance().setListView(notifDisplay);
        ContextAreaHandler.getInstance().setContextArea(contextArea);

        ContextAreaHandler.getInstance().addContext("peonStatus", "peonStatus.fxml");
        ContextAreaHandler.getInstance().addContext("jobsContext", "jobsContext.fxml");
        MarketMenu.init(bossPane);
        BuildingMenu.init(bossPane);
        eMenuHandler.init(bossPane)
        .registerItem(new eMenuItem() { // Back
            @Override
            public String getName() {
                return "Return to Game";
            }

            @Override
            public void handle() {
               	btnMain.play();
                eMenuHandler.hideMenu();
            }
        })
                .registerItem(new eMenuItem() { // Settings
                    @Override
                    public String getName() {
                        return "Settings";
                    }


            @Override
            public void handle() {
               	btnMain.play();
                new eSubMenu(new eMenuHandler()).with("settingsMenu.fxml");
            }
        })
        .registerItem(new eMenuItem() { // Instructions
            @Override
            public String getName() {
                return "Instructions";
            }

            @Override
            public void handle() {
               	btnMain.play();
                new eSubMenu(new eMenuHandler()).with("instructionsMenu.fxml");
            }
        })

        .registerItem(new eMenuItem() { // Back
            @Override
            public String getName() {
                return "QUIT GAME";
            }

            @Override
            public void handle() {
               	btnMain.play();
                exit();
            }
        });

        // This will add everything to the Debug Box. only call this once!
        if(DebugDumpController.getDebug()) {
            // Debug buttons from the side
            new eMenuHandler().registerItem(new eMenuItem() {
                @Override
                public String getName() {
                    return "DEBUG";
                }

                @Override
                public void handle() {
                	btnMain.play();
                    new eSubMenu(new eMenuHandler()).with(DebugBox.getPane());
                }
            });
            new DebugDumpController();
        }

        openMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               	btnSecondary.play();
                new eMenuHandler().showMenu();
            }
        });

        viewTasksButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	btnSecondary.play();
                JobsContextControllerHandler.getInstance().showJobsStatus();
                LOG.info("Pressed Tasks Button.");
            }
        });


        viewMarketButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	btnSecondary.play();
                new MarketMenu().showMenu();
                LOG.info("Pressed Market Button.");
            }
        });

        buildingOptionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	btnSecondary.play();
            	new BuildingMenu().showMenu();
                LOG.info("Pressed the building options Button.");
            }
        });

        transportButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	btnSecondary.play();
            	TransportContextControllerHandler.getInstance().showTransportStatus();
            }
        });
        
        // INSTRUCTIONS EVENT WATCHERS
		try { // register to the Day/Night Transistion Tracker
			MineEventHandler.getInstance().registerFor(DayNightTransistionTracker.class, new Watcher() {
				@Override
				public void handle(Object arg) {
					if (DayNightTransistionTracker.states.DAYTONIGHT == arg && firstNight) {
						addInstructions();
						firstNight = false;
					} else {
						return;
					}

				}
			});
		} catch (InvalidClassException e) {
			LOG.error("There was an error handling the day/night transtion + ", e);
		}
		
	     try {
	           MineEventHandler.getInstance().registerFor(PeonDigTracker.class, new Watcher() {
	               @Override
	               public void handle(Object arg) {
	            	   if (firstAction) {
	            		   addInstructions();
	            		   firstAction = false;
	            	   } else {
	            		   return;
	            	   }
	               }
	           });
	       } catch (InvalidClassException e) {
	           e.printStackTrace();
	       }
	     
		//EVENT WATCHING
        try {
            MineEventHandler.getInstance().registerFor(PeonDeathTracker.class, new Watcher() {
                @Override
                public void handle(Object arg) {
                    Sound oof = new Sound("oof.mp3");
                    oof.play();
                }
            });
        } catch (InvalidClassException e) {
            e.printStackTrace();
        }

       try {
            MineEventHandler.getInstance().registerFor(PeonDamagedTracker.class, new Watcher() {
                @Override
                public void handle(Object arg) {
                	System.out.println(this.getClass().getName() + " was damaged");
                    Sound alarm = new Sound("alarm.mp3");
                    alarm.play();
                }
            });
        } catch (InvalidClassException e) {
            e.printStackTrace();
        }
       
     try {
           MineEventHandler.getInstance().registerFor(PeonDigTracker.class, new Watcher() {
               @Override
               public void handle(Object arg) {
                   Sound dig = new Sound("dig.mp3");
                   dig.play();
               }
           });
       } catch (InvalidClassException e) {
           e.printStackTrace();
       }
     
     try {
         MineEventHandler.getInstance().registerFor(PeonMineTracker.class, new Watcher() {
             @Override
             public void handle(Object arg) {
                 Sound mine = new Sound("pick.mp3");
                 mine.play();
             }
         });
     } catch (InvalidClassException e) {
         e.printStackTrace();
     }
     
     try {
         MineEventHandler.getInstance().registerFor(AchievementTracker.class, new Watcher() {
             @Override
             public void handle(Object arg) {
                 Sound win = new Sound("achievementWoW.mp3");
                 win.play();
             }
         });
     } catch (InvalidClassException e) {
         e.printStackTrace();
     }

        startGameTicks(World.getInstance(), canvas);
    }
    
    /**
     * This is the function that starts the game actually moving forwards.
     * @param mainworld
     * @param canvas
     */
    private void startGameTicks(World mainworld, ResizableCanvas canvas) {
            // Start the game ticks
            Executor mainexecutor = Executors.newCachedThreadPool();
            mainexecutor.execute(new GameTimer(mainworld, 10));

            // Start the game renderer
            mainworld.getTileManager().loadTextures();
            mainworld.setCanvas(canvas);
            new FrameHandler(canvas.getGraphicsContext2D(), mainworld, canvas).start();

            Music.getInstance().playDayMusic();
            World.getInstance().addEntityToWorld(new RequisitionStore(400, 30, 100, 100));
            World.getInstance().addEntityToWorld(new ZombieMob(300, 0, 100, 32, 32));
            World.getInstance().addEntityToWorld(new ZombieMob(220, 0, 100, 32, 32));
            World.getInstance().addEntityToWorld(new BatMob(22, 39));
         // Add some dummy items to the world
            World.getInstance().addEntityToWorld(new Peon(0, 0, 32, 32, "Jim Steel"));
            World.getInstance().addEntityToWorld(new Peon(0, 100, 32, 32, "Timmy"));

    }
    
	/** 
	 * Opens the instructions popup 
	 */
	public void addInstructions() {    
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/layouts/menus/InstructionsBubble.fxml"));
					bossPane.getChildren().add(loader.load());
					LOG.info("InstructionsPane Loaded");
				} catch (IndexOutOfBoundsException ex) {
					LOG.info("IOOBException when trying to load the instructions fxml");
				} catch (IOException e) {
					LOG.info("IOException when trying to load the instructions fxml" +e.getMessage());
				} catch (IllegalStateException e) {
					LOG.info("IOException when trying to load the instructions fxml" +e.getMessage());
				} catch (Exception e) {
					LOG.info("Instructions - it's something else" +e.getMessage());
				}
				return;
			}
		});
	}

    public void exit() {
        System.exit(0); //TODO: fire an event that tells both the game and the database to shutdown gracefully
    }
}
