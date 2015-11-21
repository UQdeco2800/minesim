package minesim.menu;

import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import minesim.DummyApplication;

import static org.junit.Assert.assertTrue;

/**
 * Created by Michael on 25/10/2015.
 */
public class ButtonTests {
    public static int aVar = 0;

    @Before
    public void before() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(DummyApplication.class);
    }

    @Test
    public void buttonTest() throws Exception {
        Pane n = new Pane();
        Button b1 = new Button("Are you thinking what I'm thinking b2?");
        Button b2 = new Button("Yes I am b1", n);
        Button b3 = new Button();

        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aVar = 10;
            }
        });

        b3.fire();
        assertTrue(aVar == 10);
    }
}
