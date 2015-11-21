package minesim;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javafx.scene.image.Image;
import minesim.entities.Animation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AnimationTests {
	private static final Logger LOGGER = Logger.getLogger(Animation.class);

	private ArrayList<Image> walkingRight = new ArrayList<Image>();
	private ArrayList<Image> standingRight = new ArrayList<Image>();
	private Animation animation;
	private Animation walkRight;

	/**
	 * Tests that the right logger message is executed if an invalid animation
	 * initialisation is written (namely invalid duration is entered)
	 */
	@Test
	public void invalidAnimationInitialisation() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);

		try {
			// The functions to test
			animation = new Animation(standingRight, 0);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You cannot have a duration"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the right logger message is executed if an invalid animation
	 * initialisation is written (namely invalid frames entered)
	 */
	@Test
	public void invalidFramesAnimationInitialisation() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);

		try {
			// The functions to test
			animation = new Animation(null, 10);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You cannot have null frames"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if a valid single animation
	 * initialisation is written
	 */
	@Test
	public void validSingleAnimationInitialisation() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		try {
			// The functions to test
			animation = new Animation(standingRight, 10);
			assertTrue(animation.getFrameDelay() == 10);
			assertTrue(animation.getStopped());
			assertTrue(animation.getFrameCount() == 0);
			assertTrue(animation.getCurrentFrame() == 0);
			assertTrue(animation.getAnimationDirection() == 1);
			assertTrue(animation.getTotalFrames() == 1);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if a valid multiple animations
	 * initialisation is written
	 */
	@Test
	public void validMultipleAnimationsInitialisation() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		for (int a = 0; a < 3; a++) {
			walkingRight.add(mockSprite);
		}

		try {
			// The functions to test
			walkRight = new Animation(walkingRight, 10);
			assertTrue(walkRight.getFrameDelay() == 10);
			assertTrue(walkRight.getStopped());
			assertTrue(walkRight.getFrameCount() == 0);
			assertTrue(walkRight.getCurrentFrame() == 0);
			assertTrue(walkRight.getAnimationDirection() == 1);
			assertTrue(walkRight.getTotalFrames() == 3);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if the start function behaves as
	 * it should
	 */
	@Test

	public void testValidStart() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(standingRight, 10);

		try {
			// The functions to test
			assertTrue(animation.getStopped());
			assertTrue(animation.getFrames().size() > 0);
			animation.start();
			assertFalse(animation.getStopped());
			animation.start();

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the right logger message is executed if start function is
	 * executed with incorrect parameters (namely null frames).
	 */
	@Test
	public void testInvalidStart() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(null, 10);

		try {
			// The functions to test
			assertTrue(animation.getStopped());
			assertTrue(animation.getFrames().size() <= 0);
			animation.start();
			assertTrue(animation.getStopped());

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You cannot have null frames"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if the start function behaves is
	 * called when it's running already
	 */
	@Test
	public void testInvalidStart2() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(standingRight, 10);

		try {
			// The functions to test
			assertTrue(animation.getStopped());
			assertTrue(animation.getFrames().size() > 0);
			animation.start();
			assertFalse(animation.getStopped());
			animation.start();
			assertFalse(animation.getStopped());

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if the stop function is executed
	 * correctly
	 */
	@Test
	public void testValidStop() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(standingRight, 10);

		try {
			// The functions to test
			assertTrue(animation.getStopped());
			assertTrue(animation.getFrames().size() > 0);
			animation.start();
			assertFalse(animation.getStopped());
			assertTrue(animation.getFrames().size() > 0);
			animation.stop();
			assertTrue(animation.getStopped());

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the right logger message is executed if stop function is
	 * executed incorrectly (namely invalid frames: null)
	 */
	@Test
	public void testInvalidStop() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(null, 10);

		try {
			// The functions to test
			assertTrue(animation.getStopped());
			assertTrue(animation.getFrames().size() <= 0);
			animation.stop();

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You cannot have null frames"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if the stop function is executed
	 * if it's already stopped.
	 */
	@Test
	public void testInvalidStop2() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(standingRight, 10);

		try {
			// The functions to test
			assertTrue(animation.getStopped());
			assertTrue(animation.getFrames().size() > 0);
			animation.stop();
			assertTrue(animation.getStopped());

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if the restart function is
	 * executed correctly.
	 */
	@Test
	public void testValidRestart() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(standingRight, 10);

		try {
			// The functions to test
			assertTrue(animation.getStopped());
			assertTrue(animation.getFrames().size() > 0);
			animation.restart();
			assertFalse(animation.getStopped());
			assertTrue(animation.getCurrentFrame() == 0);

			String logMessage = outputStream.toString();

			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that an invalid restart call executes the right logger error
	 * message
	 */
	@Test
	public void testInvalidRestart() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(null, 10);

		try {
			// The functions to test
			assertTrue(animation.getStopped());
			assertTrue(animation.getFrames().size() <= 0);
			animation.restart();
			assertTrue(animation.getStopped());

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You cannot have null frames"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the reset function behaves as it should
	 */
	@Test
	public void testReset() {
		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(standingRight, 10);

		animation.reset();
		assertTrue(animation.getCurrentFrame() == 0);
		assertTrue(animation.getFrameCount() == 0);
		assertTrue(animation.getStopped());
	}

	/**
	 * Tests that the getter methods are not null, and by extension, all
	 * instance variables with a valid initialisation.
	 */
	@Test
	public void notNullGetterMethods() {
		Image mockSprite = mock(Image.class);
		standingRight.add(mockSprite);
		animation = new Animation(standingRight, 10);

		assertNotNull(animation.getAnimationDirection());
		assertNotNull(animation.getCurrentFrame());
		assertNotNull(animation.getCurrentSprite());
		assertNotNull(animation.getFrameCount());
		assertNotNull(animation.getFrameDelay());
		assertNotNull(animation.getFrames());
		assertNotNull(animation.getStopped());
		assertNotNull(animation.getTotalFrames());
	}

	/**
	 * Tests that the update function behaves as it should if the frame count is
	 * less than the frame delay.
	 */
	@Test
	public void testUpdateFinalCoverage() {
		Image mockSprite = mock(Image.class);
		for (int a = 0; a < 3; a++) {
			walkingRight.add(mockSprite);
		}

		// The functions to test
		walkRight = new Animation(walkingRight, 10);

		assertTrue(walkRight.getStopped());
		walkRight.start();
		assertFalse(walkRight.getStopped());

		walkRight.setAnimationDirection(-4);
		for (int i = 0; i < 11; i++) {
			walkRight.update();
		}
		assertTrue(walkRight.getFrameCount() == 0);
		assertTrue(walkRight.getCurrentFrame() == (walkRight.getTotalFrames() - 1));
	}

	/**
	 * Tests that the update function behaves as it should if the frame count is
	 * less than the frame delay
	 */
	@Test
	public void testUpdateFrameCountLessThan() {
		Image mockSprite = mock(Image.class);
		for (int a = 0; a < 3; a++) {
			walkingRight.add(mockSprite);
		}

		// The functions to test
		walkRight = new Animation(walkingRight, 10);

		assertTrue(walkRight.getStopped());
		walkRight.start();
		assertFalse(walkRight.getStopped());
		for (int i = 0; i < 9; i++) {
			walkRight.update();
		}
		assertTrue(walkRight.getFrameCount() == 9);
		assertTrue(walkRight.getCurrentFrame() == 0);
	}

	/**
	 * Tests that the update function behaves as it should if the frame count is
	 * greater than the frame delay, with the current frame greater than 0, and
	 * less than the total frames - 1
	 */
	@Test
	public void testUpdateFrameCountGreaterThan() {
		Image mockSprite = mock(Image.class);
		for (int a = 0; a < 3; a++) {
			walkingRight.add(mockSprite);
		}

		// The functions to test
		walkRight = new Animation(walkingRight, 10);

		assertTrue(walkRight.getStopped());
		walkRight.start();
		assertFalse(walkRight.getStopped());
		for (int i = 0; i < 11; i++) {
			walkRight.update();
		}
		assertTrue(walkRight.getFrameCount() == 0);
		assertTrue(walkRight.getCurrentFrame() == 1);
	}

	/**
	 * Tests that the update function behaves as it should if the frame count is
	 * greater than the frame delay, with the current frame greater than the
	 * total frames - 1
	 */
	@Test
	public void testUpdateFrameCountGreaterPositiveDirection() {
		Image mockSprite = mock(Image.class);
		for (int a = 0; a < 3; a++) {
			walkingRight.add(mockSprite);
		}

		// The functions to test
		walkRight = new Animation(walkingRight, 10);

		assertTrue(walkRight.getStopped());
		walkRight.start();
		assertFalse(walkRight.getStopped());
		for (int i = 0; i < 11; i++) {
			walkRight.update();
		}
		assertTrue(walkRight.getFrameCount() == 0);
		assertTrue(walkRight.getCurrentFrame() == 1);

		for (int i = 0; i < 11; i++) {
			walkRight.update();
		}
		assertTrue(walkRight.getCurrentFrame() == 2);
		for (int i = 0; i < 11; i++) {
			walkRight.update();
		}
		assertTrue(walkRight.getFrameCount() == 0);
		assertTrue(walkRight.getCurrentFrame() == 0);
	}

	/**
	 * Tests that the update function behaves as it should if the frame count is
	 * greater than the frame delay, with the current frame less than 0
	 */
	@Test
	public void testUpdateFrameCountGreaterNegativeDirection() {
		Image mockSprite = mock(Image.class);
		for (int a = 0; a < 3; a++) {
			walkingRight.add(mockSprite);
		}

		// The functions to test
		walkRight = new Animation(walkingRight, 10);

		assertTrue(walkRight.getStopped());
		walkRight.start();
		assertFalse(walkRight.getStopped());
		for (int i = 0; i < 11; i++) {
			walkRight.update();
		}
		assertTrue(walkRight.getFrameCount() == 0);
		assertTrue(walkRight.getCurrentFrame() == 1);
		for (int i = 0; i < 11; i++) {
			walkRight.update();
		}
		walkRight.setAnimationDirection(-1);
		assertTrue((walkRight.getTotalFrames() - 1) == 2);
		assertTrue(walkRight.getCurrentFrame() == (walkRight.getTotalFrames() - 1));
		assertTrue(walkRight.getCurrentFrame() == 2);
	}
}
