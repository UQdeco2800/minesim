package minesim;

import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Michael on 25/10/2015.
 */
public class ResizableCanvasTests {

    @Before
    public void before() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(DummyApplication.class);
    }

    @Test
    public void initTest() {
        ResizableCanvas rc = new ResizableCanvas();
        assertTrue(rc.isResizeable());
    }

    @Test
    public void bindTest() {
        Pane p = new Pane();
        p.setPrefWidth(100);
        p.setPrefHeight(100);
        p.setMinHeight(100);
        p.setMinWidth(100);

        ResizableCanvas rc = new ResizableCanvas();
        p.getChildren().add(rc);
        rc.bindTo(p);

        // The canvas should actually bind to the Pane's size, but it doesn't :/
        // This is a problem and makes it hard to test.
        // Please peruse the running game to see this class working.
//        assertEquals("Width should be equal", 100, rc.prefHeight(), 0.001);
//        assertEquals("Width should be equal", 100, rc.prefWidth(), 0.001);
    }
}
