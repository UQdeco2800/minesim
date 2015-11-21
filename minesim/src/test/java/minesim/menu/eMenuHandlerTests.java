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

/**
 * Created by Michael on 25/10/2015.
 */
public class eMenuHandlerTests {
    public static int aValue = 0;


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
        eMenuHandler.resetAll();
        Pane testPane = new Pane();
        Pane oldTestPane = new Pane(testPane);
        eMenuHandler.init(testPane);
        waitForRunLater();
        assertNotEquals(oldTestPane.getChildren(), testPane.getChildren());
    }

    @Test
    public void backingTest() throws InterruptedException {
        eMenuHandler.resetAll();
        Pane testPane = new Pane();
        eMenuHandler.init(testPane);
        waitForRunLater();

        Pane hascorrectStyle = new Pane();
        hascorrectStyle.setStyle("-fx-background-color: #191d22; -fx-border-color: darkred; -fx-opacity: 0.8;");

        StackPane generatedAssembly = eMenuHandler.getAssembly();
        assertEquals("backing should be the only child", 1, generatedAssembly.getChildren().size());
        assertEquals(generatedAssembly.getChildren().get(0).getStyle(), hascorrectStyle.getStyle());

        eMenuHandler.showMenu();
        assertTrue(eMenuHandler.isVisible());
        generatedAssembly.getChildren().get(0).fireEvent(new MouseEvent(MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, false, false, false, new PickResult(null, 0, 0)));
        assertFalse(eMenuHandler.isVisible());
    }

    @Test
    public void registerMenuTest() throws InterruptedException {
        eMenuHandler.resetAll();
        Pane testPane = new Pane();
        eMenuHandler.init(testPane);
        waitForRunLater();

        new eMenuHandler().registerItem(new eMenuItem() {
            @Override
            public String getName() {
                return "Test Button";
            }

            @Override
            public void handle() {
                eMenuHandlerTests.aValue = 10;
            }
        });

        assertEquals("assembly should now have the backing and menu", 2, eMenuHandler.getAssembly().getChildren().size());

    }

    @Test
    public void hideShowTest() throws InterruptedException {
        eMenuHandler.resetAll();
        Pane testPane = new Pane();
        eMenuHandler.init(testPane);
        waitForRunLater();

        eMenuHandler.hideMenu();
        assertFalse(eMenuHandler.isVisible());

        eMenuHandler.showMenu();
        assertTrue(eMenuHandler.isVisible());
    }
}
