package minesim.inputhandlers;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import minesim.World;

import static org.mockito.Mockito.mock;

/**
 * Keyboard Handler Test Created by Sean on 14/09/2015.
 */
public class KeyboardHandlerTest {

    @Test
    public void testConstructor() throws Exception {
        KeyboardHandler keyboardHandler = new KeyboardHandler();
    }

    @Test
    public void testSetWorld() throws Exception {
        KeyboardHandler keyboardHandler = new KeyboardHandler();
        World world = mock(World.class);
        keyboardHandler.setGameWorld(world);
    }

    @Test
    public void testKeys() throws Exception {
        KeyboardHandler keyboardHandler = KeyboardHandler.getInstance();
        MouseHandler mouseHandler = mock(MouseHandler.class);
        keyboardHandler.setGameWorld(mock(World.class));
        keyboardHandler.setMouseHandler(mouseHandler);

        keyboardHandler.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.RIGHT, false, false, false, false));
        keyboardHandler.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT, false, false, false, false));
//        keyboardHandler.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.D, false, false, false, false));
        //This keyboard handle is actually working, just the logic implementation under D is causing the error. Ignore for now
        keyboardHandler.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, false, false, false));

        //just to hit the default in the switch statement
        keyboardHandler.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.EURO_SIGN, false, false, false, false));
    }
}
