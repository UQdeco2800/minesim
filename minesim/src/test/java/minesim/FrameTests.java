package minesim;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import javafx.scene.image.Image;
import minesim.entities.Frame;
import minesim.entities.SpriteLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FrameTests {
	private static final Logger LOGGER = Logger.getLogger(Frame.class);

	private Frame frame;

	/**
	 * Tests that no logger message is executed for a valid frame setup.
	 */
	@Test
	public void validFrameInitialisation() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);

		try {
			// The functions to test
			frame = new Frame(mockSprite, 10);
			assertNotNull(frame.getFrame());
			assertTrue(frame.getDuration() == 10);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that getters are not null if there is a valid Frame initilisation.
	 */
	@Test
	public void validGetters() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);

		try {
			// The functions to test
			frame = new Frame(mockSprite, 10);
			assertNotNull(frame.getFrame());
			assertNotNull(frame.getDuration());

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the appropriate error message is displayed if the frame image
	 * is null.
	 */
	@Test
	public void inValidFrame() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		try {
			// The functions to test
			frame = new Frame(null, 10);
			assertNull(frame.getFrame());

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You cannot have null frames"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the appropriate logger message is executed if the duration is
	 * less than or equal to 0.
	 */
	@Test
	public void inValidDuration() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);

		try {
			// The functions to test
			frame = new Frame(mockSprite, 0);
			assertTrue(frame.getDuration() == 0);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You cannot have a duration equal to or less than 0"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the appropriate logger message is executed if the duration is
	 * set to be less than or equal to 0.
	 */
	@Test
	public void invalidSettersDuration() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		frame = new Frame(mockSprite, 10);

		try {
			// The functions to test
			assertTrue(frame.getDuration() == 10);
			frame.setDuration(-1);
			assertTrue(frame.getDuration() == 10);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You cannot have a duration"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if the duration is set to a
	 * valid number.
	 */
	@Test
	public void validSettersDuration() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		frame = new Frame(mockSprite, 10);

		try {
			// The functions to test
			assertTrue(frame.getDuration() == 10);
			frame.setDuration(20);
			assertTrue(frame.getDuration() == 20);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the appropriate logger message is executed if the frame is set
	 * to null.
	 */
	@Test
	public void invalidSettersFrame() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		frame = new Frame(mockSprite, 10);

		try {
			// The functions to test
			assertEquals(mockSprite, frame.getFrame());
			frame.setFrame(null);
			assertEquals(mockSprite, frame.getFrame());

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You cannot have null frames"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if the frame is set to a valid
	 * image.
	 */
	@Test
	public void validSettersFrame() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		Image mockSprite = mock(Image.class);
		Image mockSprite2 = mock(Image.class);
		frame = new Frame(mockSprite, 10);

		try {
			// The functions to test
			assertEquals(mockSprite, frame.getFrame());
			frame.setFrame(mockSprite2);
			assertEquals(mockSprite2, frame.getFrame());

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);
		} finally {
			LOGGER.removeAppender(appender);
		}
	}
}
