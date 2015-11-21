package minesim;

import java.net.URL;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import minesim.inputhandlers.KeyboardHandler;

//Jenkins #500 aww yeah
public class MineSim extends Application {
    private static URL url;
    private static Media media;
    public static MediaPlayer musicPlayer;

    public static int RESOLUTION_WIDTH = 800;
    public static int RESOLUTION_HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    /*public static void startMusic() {
        url = Music.class.getResource("../Music/" + "titlescreen.mp3");
        media = new Media(url.toString());
        musicPlayer = new MediaPlayer(media);
        // Setup starting music:
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(0.5);
        musicPlayer.play();
    }

    public static void stopMusic(){
        musicPlayer.stop();
    }*/

    @Override
    public void start(Stage primaryStage) throws Exception {
        org.apache.log4j.BasicConfigurator.configure();

        Parent root = FXMLLoader.load(getClass().getResource("/layouts/SplashScreen.fxml"));

        root.setOnKeyPressed(KeyboardHandler.getInstance());

        Music.getInstance().play();

        //Scene scene = new Scene(root, 940, 700);
        Scene scene = new Scene(root, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
        primaryStage.setTitle("DecoMine - A Mining Simulator");
        primaryStage.getIcons().add(new Image("mine-logo.png"));
        primaryStage.setScene(scene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(0); //TODO: make this fire an event to shut the database down instead of forcing this
            }
        });
    }
}
