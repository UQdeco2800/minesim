package minesim.inputhandlers;

import javafx.event.EventType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import minesim.World;
import minesim.contexts.PeonStatusContextControllerHandler;
import minesim.entities.Peon;
import minesim.entities.PeonGuard;
import minesim.entities.PeonMiner;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Mouse Handler Test Created by Sean on 14/09/2015.
 */

//@PrepareForTest(PeonStatusContextControllerHandler.class)
@PrepareForTest({MouseHandler.class})
//@RunWith(PowerMockRunner.class)
public class MouseHandlerTest {

//    @Test
    /**
     * Test the constructor() of MouseHandler
     * @throws Exception -throw exception when constructor could not work
     */
//    public void testConstructor() throws Exception {
//        World world = mock(World.class);
//        MouseHandler mouseHandler = new MouseHandler(world);
//    }

    /**
     * test the click event
     */
//    @Test
//    public void testClickEvent() throws Exception {
//        World myWorld = genPopulatedWorld();
//        MouseHandler mouseHandler = new MouseHandler(myWorld);
//
////        PowerMockito.mockStatic(PeonStatusContextControllerHandler.class);
////        PowerMockito.when(PeonStatusContextControllerHandler.getInstance()).thenReturn(mock(PeonStatusContextControllerHandler.class));
//
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_CLICKED, 1, 1));
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_PRESSED, 1, 1));
//        mouseHandler.handle(genMouseDrag(3, 3));
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_RELEASED, 3, 3));
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_CLICKED, 3, 3));
//
//        myWorld.getArea();
//    }

    /**
     * test the action mode
     */
//    @Test
//    public void testActionMode() throws Exception {
//        World myWorld = genPopulatedWorld();
//        MouseHandler mouseHandler = new MouseHandler(myWorld);
//
//        mouseHandler.handle(genRightClick(MouseEvent.MOUSE_CLICKED, 2, 2));
//
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_CLICKED, 1, 1));
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_PRESSED, 1, 1));
//        mouseHandler.handle(genMouseDrag(3, 3));
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_RELEASED, 3, 3));
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_CLICKED, 3, 3));
//
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_PRESSED, 4, 4));
//
//    }

    /**
     * test the dig mode
     */
//    @Test
//    public void testDigMode() throws Exception {
//        World myWorld = genPopulatedWorld();
//        MouseHandler mouseHandler = new MouseHandler(myWorld);
//        mouseHandler.toggleActionMode(1); //pressed D for dig on keyboard
//
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_PRESSED, 4, 4));
//    }

    /**
     * test the drag
     */
//    @Test
//    public void testDragFirstSomehow() throws Exception {
//        World myWorld = genPopulatedWorld();
//        MouseHandler mouseHandler = new MouseHandler(myWorld);
//
//        mouseHandler.handle(genMouseDrag(3, 3));
//    }

    /**
     * test the name
     */
//    @Test
//    public void testName() throws Exception {
//        World myWorld = genPopulatedWorld();
//        MouseHandler mouseHandler = new MouseHandler(myWorld);
//
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_PRESSED, 1, 1));
//    }

    /**
     * test the right click when chosen a group of peon
     */
    @Test
    public void testRightClickToGroup() throws Exception {
        World myWorld = genPopulatedWorld();
        MouseHandler mouseHandler = new MouseHandler(myWorld);
        mouseHandler.registerForNextClick(myWorld.getWorldentities().getFirst());
        mouseHandler.handle(genRightClick(MouseEvent.MOUSE_PRESSED, 7, 6));

    }

    /**
     * test the select when drug
     */
//    @Test
//    public void testSelect() throws Exception {
//        World myWorld = genPopulatedWorld();
//        MouseHandler mouseHandler = new MouseHandler(myWorld);
//
//        mouseHandler.handle(genMouseClick(MouseEvent.MOUSE_PRESSED, 4, 4));
//
//    }

    /**
     * test the set functions
     */
    @Test
    public void testSetFunstion() {
        World myWorld = genPopulatedWorld();
        MouseHandler mouseHandler = new MouseHandler(myWorld);
        assertEquals(100, mouseHandler.getHeight(), 0.1);
        assertEquals(100, mouseHandler.getWidth(), 0.1);
        assertEquals(MouseHandler.class, mouseHandler.getClass());
    }

    /**
     * test the padding fuction
     */
    @Test
    public void testPadding() {
        World myWorld = this.genPopulatedWorld();
        MouseHandler mouseHandler = new MouseHandler(myWorld);
        assertEquals(100, mouseHandler.getWidth(), 0.1);
        assertEquals(100, mouseHandler.getHeight(), 0.1);
        mouseHandler.toggleActionMode(1);
        int[] xs = {50, 720, 240};
        int[] ys = {30, 580, 240};
        for (int x : xs) {
            for (int y : ys) {
                mouseHandler.handle(this.paddingMouse(x, y));
            }
        }
    }

    /**
     * create a click mouse event
     */
    private MouseEvent genMouseClick(EventType<? extends MouseEvent> type, double x, double y) {
        return new MouseEvent(
                type,
                x,
                y,
                x,
                y,
                MouseButton.PRIMARY,
                1,
                false,
                false,
                false,
                false,
                true, //left click down
                false,
                false,
                false,
                false,
                false,
                new PickResult(null, x, y)
        );
    }

    /**
     * create a right click mouseEvent
     */
    private MouseEvent genRightClick(EventType<? extends MouseEvent> type, double x, double y) {
        return new MouseEvent(
                type,
                x,
                y,
                x,
                y,
                MouseButton.SECONDARY,
                1,
                false,
                false,
                false,
                false,
                false,
                false,
                true, //right click down
                false,
                false,
                false,
                new PickResult(null, x, y)
        );
    }

    /**
     * create a drag mouseEvent
     */
    private MouseDragEvent genMouseDrag(double x, double y) {
        return new MouseDragEvent(
                MouseDragEvent.MOUSE_DRAG_ENTERED,
                x,
                y,
                x,
                y,
                MouseButton.PRIMARY,
                1,
                false,
                false,
                false,
                false,
                true, //left click down
                false,
                false,
                false,
                false,
                new PickResult(null, x, y),
                genMouseClick(MouseEvent.MOUSE_PRESSED, 0, 0) //drag from origin
        );
    }

    /**
     * create a move mouseEvent
     */
    private MouseEvent paddingMouse(double x, double y) {
        return new MouseEvent(
                MouseEvent.MOUSE_MOVED,
                x,
                y,
                x,
                y,
                null,
                0,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                null
        );
    }

    /**
     * create the world for test
     */
    private World genPopulatedWorld() {
        World world = new World(100, 100);
        world.addEntityToWorld(new Peon(1, 1, 1, 1, "Derek"));
        world.addEntityToWorld(new PeonGuard(4, 4, 1, 1, "Bruce"));
        world.addEntityToWorld(new PeonMiner(3, 2, 1, 1, "Alison"));
        return world;
    }

}
