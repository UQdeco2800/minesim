package minesim.menu;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * This class handles the registering of new menu actions as well as the
 * appearance and utility of the escape/pause menu.
 * Created by Michael on 27/09/2015.
 */
public class eMenuHandler {
    private static final Logger LOG = Logger.getLogger(eMenuHandler.class);

    private static List<eMenuItem> registeredMenus = new ArrayList<>();
    private static StackPane assembly = new StackPane();
    private static StackPane menu = new StackPane();

    // The 'backing' of the escape menu (the faded bit)
    private static Pane backing = createBacking();

    public eMenuHandler() {
    /**
     * Unused constructor.
     */
    }

    /**
     * Controls how the area behind the menu actually looks. Should only be
     * called during initialisation.
     */
    private static Pane createBacking() {
        Pane p = new Pane();
        p.setStyle("-fx-background-color: #191d22; -fx-border-color: darkred; -fx-opacity: 0.8;");
        p.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new eMenuHandler().hideMenu();
            }
        });
        return p;
    }

    /**
     * Updates the assembly with all the appropriate parts of the escape menu.
     */
    private void refreshAssembly() {
        LOG.info("Refreshing the escape menu to ensure it's up to date");
        this.clearPanes();
        assembly.getChildren().add(backing);

        VBox buttons = new VBox();
        buttons.getStyleClass().add("escape");
        buttons.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
        buttons.getStylesheets().add(getClass().getResource("/css/ButtonStyles.css").toExternalForm());
        buttons.setAlignment(Pos.CENTER);
        buttons.setMaxWidth(Control.USE_PREF_SIZE);
        buttons.setMaxHeight(Control.USE_PREF_SIZE);

        for (eMenuItem item : registeredMenus) {
            Button tmpB = new Button();
            tmpB.getStyleClass().add("otherbutton");
            tmpB.setContentDisplay(ContentDisplay.CENTER);
            tmpB.setAlignment(Pos.CENTER);
            tmpB.setText(item.getName());
            tmpB.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    item.handle();
                }
            });
            buttons.getChildren().add(tmpB);
        }

        menu.getChildren().add(buttons);
        menu.setPickOnBounds(false);
        assembly.getChildren().add(menu);
    }

    /**
     * Should only be run when the menu needs to be setup.
     */
    public static eMenuHandler init(Pane attachmentPoint) {
        clearPanes();
        registeredMenus.clear();
        assembly.getChildren().add(backing);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                attachmentPoint.getChildren().add(assembly);
            }
        });
        assembly.setVisible(false);
        return new eMenuHandler();
    }

    /**
     * Register an item for listing in the menu. Returns itself to allow
     * chaining of methods.
     */
    public eMenuHandler registerItem(eMenuItem item) {
        registeredMenus.add(item);
        refreshAssembly();
        return this;
    }

    /**
     * Sets the menu's visible property to true.
     */
    public static void showMenu() {
        assembly.setVisible(true);
    }

    /**
     * Sets the menu's visible property to false.
     */
    public static void hideMenu() {
        assembly.setVisible(false);
    }

    /**
     * Returns a Boolean indicting whether or not the menu is visible.
     */
    public static Boolean isVisible() {
        return assembly.isVisible();
    }

    /**
     * Clear all the panes of the menu
     */
    private static void clearPanes() {
        assembly.getChildren().clear();
        menu.getChildren().clear();
    }

    /**
     * Exposed for testing
     */
    protected static StackPane getAssembly() {
        return assembly;
    }

    /**
     * For testing
     */
    protected static void resetAll() {
        registeredMenus = new ArrayList<>();
        assembly = new StackPane();
        menu = new StackPane();
    }
}
