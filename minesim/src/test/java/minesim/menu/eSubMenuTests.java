package minesim.menu;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxToolkit;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import minesim.DummyApplication;

import static org.junit.Assert.assertTrue;

/**
 * Created by Michael on 25/10/2015.
 */
public class eSubMenuTests {


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

    private Pane makeSub() {
        Pane p = new Pane();
        p.getChildren().add(new Label("This is a test label"));
        return p;
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void initTest() throws Exception {
        Pane bossPane = new Pane();
        eMenuHandler.init(bossPane);

        eSubMenu sub2 = new eSubMenu(new eMenuHandler(), makeSub());
        eSubMenu sub1 = new eSubMenu(new eMenuHandler());
        eSubMenu sub3 = new eSubMenu(new eMenuHandler());
        sub1.with(makeSub());
        sub3.with("settingsMenu.fxml");

        boolean var = false;
        try {
            sub1.with(makeSub());
        } catch (InstantiationError e) {
            var = true;
        }
        assertTrue(var); var = false;

        try {
            sub2.with(makeSub());
        } catch (InstantiationError e) {
            var = true;
        }
        assertTrue(var); var = false;

        try {
            sub2.with("somestringthatwouldusuallybeanfxmlfile");
        } catch (InstantiationError e) {
            var = true;
        }
        assertTrue(var); var = false;
    }

}
