package minesim.ui;

import minesim.MineSim;
import org.junit.Before;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;

import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxAssert.verifyThat;


public class UITest extends FxRobot {

    @Before
    public void before() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(MineSim.class);
    }

    //	@Test
    public void testInitialiseGame() {
        verifyThat("#startGame", NodeMatchers.isEnabled());
        clickOn("#startGame");
        verifyThat("#addBuilding", NodeMatchers.isEnabled());
        verifyThat("#exitGame", NodeMatchers.isEnabled());
    }

}
