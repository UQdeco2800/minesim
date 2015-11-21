/**
 * Modified from http://www.coderanch.com/t/96469/Testing/junit-asserts-log-messages
 *
 * @author dayakern
 */
package minesim;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import minesim.entities.SpriteLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SpriteLoaderTests {
	// Log error handling
	private static final Logger LOGGER = Logger.getLogger(SpriteLoader.class);

	/**
	 * Tests that the right logger message is executed if the sprite file is
	 * invalid.
	 */
	@Test
	public void testMissingSprite() {
		// Set up of logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		// Valid file name
		String inValidSpriteFile = "Sprite asdfsdfSheet";
		SpriteLoader spriteLoader = new SpriteLoader();

		try {
			// The functions to test
			spriteLoader.loadSpriteSheet(inValidSpriteFile, 32, 32);
			spriteLoader.getSprite(1, 0);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You are trying to use a"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the right logger message is executed if an incorrect position
	 * (on the sprite sheet) is entered.
	 */
	@Test
	public void testNegativeDimensions() {

		// Set up logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		// Valid file name
		String validSpriteFile = "peonSheet";
		SpriteLoader spriteLoader = new SpriteLoader();
		try {
			// The functions to test
			spriteLoader.loadSpriteSheet(validSpriteFile, 32, 32);
			spriteLoader.getSprite(-1, -1);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You are requesting a grid position"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the right logger message is executed if an incorrect position
	 * (on the sprite sheet) is entered.
	 */
	@Test
	public void testPositiveDimensions() {

		// Set up logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		// Valid file name
		String validSpriteFile = "peonSheet";
		SpriteLoader spriteLoader = new SpriteLoader();
		try {
			// The functions to test
			spriteLoader.loadSpriteSheet(validSpriteFile, 32, 32);
			spriteLoader.getSprite(6, 6);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - You are requesting a grid position"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the right logger message is executed if an invalid specified
	 * sprite dimension is entered.
	 */
	@Test
	public void testInvalidSpriteSheetDimensions() {

		// Set up logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		// Valid file name
		String validSpriteFile = "peonSheet";
		SpriteLoader spriteLoader = new SpriteLoader();
		try {
			// The functions to test
			spriteLoader.loadSpriteSheet(validSpriteFile, 13, 13);
			spriteLoader.getSprite(1, 0);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("WARN - Your file is not a multiple of"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if valid specified sprite
	 * dimensions are entered.
	 */
	@Test
	public void testValidSpriteDimensions() {
		// Set up logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		// Valid file name
		String validSpriteFile = "peonSheet";
		SpriteLoader spriteLoader = new SpriteLoader();
		try {
			// The functions to test
			spriteLoader.loadSpriteSheet(validSpriteFile, 32, 32);
			spriteLoader.getSprite(1, 0);

			String logMessage = outputStream.toString();
			assertEquals("", logMessage);// should be empty
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the right logger message is executed if a null sprite sheet
	 * file name is entered (i.e. does not exist due to trolling, typos)
	 */
	@Test
	public void testNullSpriteSheet() {
		// Set up logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		// Null file name
		String nullSpriteFile = null;
		SpriteLoader spriteLoader = new SpriteLoader();

		try {
			// The function to test
			spriteLoader.loadSpriteSheet(nullSpriteFile, 32, 32);
			assertNull(nullSpriteFile);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - Could not load file"));

		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the right logger message is executed if an invalid sprite
	 * sheet file name is entered (i.e. does not exist due to trolling, typos)
	 */
	@Test
	public void testInvalidSpriteSheet() {
		// Set up logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		// Invalid file name
		String invalidSpriteFile = "invalid name";
		SpriteLoader spriteLoader = new SpriteLoader();

		try {
			// The function to test
			spriteLoader.loadSpriteSheet(invalidSpriteFile, 32, 32);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("ERROR - Could not load file"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that no logger message is executed if a valid sprite sheet is
	 * entered
	 */
	@Test
	public void testValidSpriteSheet() {
		// Set up logger retrieval
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		// Valid file name
		String validSpriteFile = "peonSheet";
		SpriteLoader spriteLoader = new SpriteLoader();

		try {
			// The function to test
			spriteLoader.loadSpriteSheet(validSpriteFile, 32, 32);
			assertNotNull(validSpriteFile);
			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertEquals("", logMessage);// should be empty
		} finally {
			LOGGER.removeAppender(appender);
		}
	}
}
