package minesim;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * Created by Michael on 10/09/15.
 */
public class ContextAreaHandlerTests {

    StackPane stackPane;

    @Before
    public void before() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(DummyApplication.class);
        stackPane = new StackPane();
    }

    private Pane makePane(String string) {
        Pane pane = new Pane();
        pane.getChildren().add(new Label(string));
        return pane;
    }

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();
    }

    @Test
    public void testDefaultClass() {
        String foo = "foo";
        Pane bar = makePane(foo);
        ContextAreaHandler cman = ContextAreaHandler.getInstance();
        cman.setContextArea(stackPane, bar);
        Pane returnedDefault = cman.getDefaultContext();

        ObservableList<Node> expectedChildren = bar.getChildren();
        ObservableList<Node> actualChildren = returnedDefault.getChildren();

        assertArrayEquals(expectedChildren.toArray(), actualChildren.toArray());
    }

    @Test
    public void testResetInstance() {
        ContextAreaHandler.resetInstance();
        String foo = "foo";
        String bar = "bar";

        ContextAreaHandler cman = ContextAreaHandler.getInstance();
        cman.setContextArea(stackPane, makePane(foo+bar));
        cman.addContext("1", makePane(foo));
        cman.addContext("2", makePane(bar));
        System.out.println(cman.contexts);
        assertEquals("Should have 3 internal contexts", 2, cman.contexts.size());

        ContextAreaHandler.resetInstance();
        cman = ContextAreaHandler.getInstance();
        cman.setContextArea(stackPane, makePane(foo));
        assertEquals("Should have nothing in the instance", 0, cman.contexts.size());
    }

    @Test
    public void testAddContext() {
        ContextAreaHandler.resetInstance();
        ContextAreaHandler cam = ContextAreaHandler.getInstance();
        cam.setContextArea(stackPane);
        Pane foo = makePane("foo");
        cam.addContext("bar", foo);
        assertEquals(foo, cam.getContext("bar"));
    }

    @Test
    public void testAddContextFXML() {
        ContextAreaHandler.resetInstance();
        ContextAreaHandler cam = ContextAreaHandler.getInstance();
        cam.setContextArea(stackPane);
        cam.addContext("bar", "test.fxml");
        assertEquals(1, cam.getContext("bar").getChildren().size());

        cam.addContext("foo", "lolololol");
        assertEquals("Invalid fxml context should have no children", 0, cam.getContext("foo").getChildren().size());
    }

    @Test
    public void testDefaultContextDisplay() {
        ContextAreaHandler.resetInstance();
        ContextAreaHandler cam = ContextAreaHandler.getInstance();
        cam.setContextArea(stackPane);
        Pane defaultPane = cam.getDefaultContext();
        assertEquals("Default should being shown now",
                defaultPane,
                cam.getCurrentContext());
    }

    @Test
    public void testSetContext() {
        ContextAreaHandler.resetInstance();
        stackPane.getChildren().clear();
        ContextAreaHandler cam = ContextAreaHandler.getInstance();
        cam.setContextArea(stackPane, makePane("Default"));

        Pane bar = makePane("bar");
        cam.addContext("foo", bar).setContext("foo");

        try { // wait for the runLater
            waitForRunLater();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(stackPane.getChildren().contains(bar));
        assertTrue(stackPane.getChildren().size() == 1);
    }

    @Test
    public void testClearRemoveContext() throws InterruptedException {
        ContextAreaHandler.resetInstance();
        ContextAreaHandler cam = ContextAreaHandler.getInstance();
        cam.setContextArea(stackPane);
        Pane foo = makePane("foo");
        Pane bar = makePane("bar");
        cam.addContext("foo", foo);
        cam.addContext("bar", bar);

        assertEquals("Should have 2 things in the hasmap", 2, cam.contexts.size());
        cam.removeContext("foo");
        assertEquals("Should have only 1 things", 1, cam.contexts.size());
        assertEquals("removed should equal null", null, cam.getContext("foo"));

        cam.setContext("bar");
        waitForRunLater();
        assertEquals(bar, cam.getCurrentContext());
        cam.clearContext();
        waitForRunLater();
        assertEquals(cam.getDefaultContext(), cam.getCurrentContext());
    }




}
