package minesim.menu;

import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import minesim.DummyApplication;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/*
 * Created by Team Cook
 */
public class BuildingMenuTest {
	
	@Before
    public void before() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(DummyApplication.class);
    }
	
	public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();
    }
	
	@Test
    public void initTest() throws InterruptedException {
		BuildingMenu.resetAll();
        StackPane menuPane = new StackPane();
        StackPane comparePane = new StackPane(menuPane);
        BuildingMenu.init(menuPane);
        waitForRunLater();
        assertNotEquals(comparePane.getChildren(), menuPane.getChildren());
    }

    @Test
    public void backingTest() throws InterruptedException {
    	BuildingMenu.resetAll();
    	StackPane menuPane = new StackPane();
        BuildingMenu.init(menuPane);
        waitForRunLater();

        Pane hascorrectStyle = new Pane();
        hascorrectStyle.setStyle("-fx-background-color: #191d22; -fx-border-color: darkred; -fx-opacity: 0.8;");

        StackPane initialOuterPane = BuildingMenu.getPane();
        assertEquals(initialOuterPane.getChildren().get(0).getStyle(), hascorrectStyle.getStyle());
        
        assertEquals("menu pane should have backing and menu", 2, initialOuterPane.getChildren().size());
        
        BuildingMenu.showMenu();
        assertTrue(BuildingMenu.isVisible());
        initialOuterPane.getChildren().get(0).fireEvent(new MouseEvent(MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, false, false, false, new PickResult(null, 0, 0)));
        assertFalse(BuildingMenu.isVisible());
    }

    @Test
    public void hideShowTest() throws InterruptedException {
    	BuildingMenu.resetAll();
        StackPane menuPane = new StackPane();
        BuildingMenu.init(menuPane);
        waitForRunLater();
        
        BuildingMenu.hideMenu();
        assertFalse(BuildingMenu.isVisible());
        
        BuildingMenu.showMenu();
        assertTrue(BuildingMenu.isVisible());
    }
}