package minesim.menu;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.NoSuchElementException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * A draggable window pane that is created and stored as part of the escape
 * menu. Created by Michael on 27/09/2015.
 */
public class eSubMenu {
    private static final Logger LOG = Logger.getLogger(eSubMenu.class);

    VBox subMenu = new VBox();
    StackPane parent;
    Boolean hasContents = Boolean.FALSE;
    Button closeButton = new Button("Close");

    /**
     * initialises the menu and stores the parent upon which to create itself.
     */
    public eSubMenu(eMenuHandler m) {
        parent = m.getAssembly();
    }

    /**
     * This method compounds the constructor and corresponding .with(String)
     * call.
     */
    public eSubMenu(eMenuHandler m, String fxml) {
        parent = m.getAssembly();
        with(fxml);
    }

    /**
     * This method compounds the constructor and corresponding .with(Pane)
     * call.
     */
    public eSubMenu(eMenuHandler m, Pane pane) {
        parent = m.getAssembly();
        with(pane);
    }

    /**
     * Uses an fxml document from the /resources/layouts/menus folder to create
     * a new submenu.
     */
    public void with(String fxmlDoc) {
        if (hasContents)
            throw new InstantiationError("This submenu has already been created with something");
        Pane fxml = new Pane();
        try {
            fxml = FXMLLoader.load(getClass().getResource("/layouts/menus/" + fxmlDoc));
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Couldn't load fxml", e);
        }
        subMenu.getChildren().add(fxml);
        hasContents = Boolean.TRUE;
        render();
    }

    /**
     * Makes a submenu that uses pane as it's content.
     */
    public void with(Pane pane) {
        if (hasContents)
            throw new InstantiationError("This submenu has already been created with something");
        subMenu.getChildren().add(pane);
        hasContents = Boolean.TRUE;
        render();
    }

    /**
     * Create the initial/default layout of the submenu.
     */
    private eSubMenu finaliseInit() {
        final DragContext dragContext = new DragContext();
        subMenu.setPrefWidth(100);
        subMenu.setPrefHeight(200);
        subMenu.setMaxWidth(400);
        subMenu.setMaxHeight(400);
        subMenu.setAlignment(Pos.CENTER);
        subMenu.setStyle("-fx-background-color: #111111; -fx-border-color: darkgray");
        subMenu.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // remember initial mouse cursor coordinates
                // and node position
                dragContext.mouseAnchorX = mouseEvent.getX();
                dragContext.mouseAnchorY = mouseEvent.getY();
                dragContext.initialTranslateX =
                        subMenu.getTranslateX();
                dragContext.initialTranslateY =
                        subMenu.getTranslateY();
            }
        });
        subMenu.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // shift node from its initial position by delta
                // calculated from mouse cursor movement
                subMenu.setTranslateX(
                        dragContext.initialTranslateX
                                + mouseEvent.getX()
                                - dragContext.mouseAnchorX);
                subMenu.setTranslateY(
                        dragContext.initialTranslateY
                                + mouseEvent.getY()
                                - dragContext.mouseAnchorY);
            }
        });


        // Set the close button's action
        eSubMenu tmp = this;
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tmp.close();
            }
        });

        return this;
    }

    /**
     * Actually shows the subMenu in the Escape Menu;
     */
    private void render() {
        finaliseInit();
        if (!hasContents)
            throw new NoSuchElementException("There was no actual content to render");
        subMenu.getChildren().add(closeButton);
        parent.getChildren().add(subMenu);
    }

    private void close() {
        parent.getChildren().remove(subMenu);
    }

    private static final class DragContext {
        public double mouseAnchorX;
        public double mouseAnchorY;
        public double initialTranslateX;
        public double initialTranslateY;
    }
}
