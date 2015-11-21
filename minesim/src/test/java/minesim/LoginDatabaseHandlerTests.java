package minesim;

import minesim.LoginDatabaseHandler;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A class for testing that the login database acts as it should. Updated from
 * /src/test/java/DatabaseHanderTests. The current tests only focus on the state
 * of the database, no logger errors.
 * 
 * @author dayakern
 *
 */
public class LoginDatabaseHandlerTests {
	// Database tester (used for connecting to the database)
	private static IDatabaseTester dbTester;
	// Login details being tested
	private LoginDatabaseHandler handler;
	// The logger to test
	private static final Logger LOGGER = Logger.getLogger(LoginDatabaseHandler.class);

	/**
	 * Connect to the database
	 */
	@BeforeClass
	public static void setUpClass() throws ClassNotFoundException {
		dbTester = new JdbcDatabaseTester("org.apache.derby.jdbc.EmbeddedDriver",
				"jdbc:derby:decoMineDB;create=true;user=decomine;password=decomine");
	}

	/**
	 * Load in a set of data from a (fixed for now) XML file
	 *
	 * @return data set to be provided to database tester (or used for
	 *         comparison)
	 */
	public IDataSet getDataSet() throws DataSetException, IOException {
		URL url = LoginDatabaseHandler.class.getClassLoader().getResource("LoginDatabaseHandlerTest.xml");
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);
		return builder.build(url);
	}

	/**
	 * Set up for each test, with a new instance of the login database. WARNING:
	 * If handler isn't created than your actual database is affected.
	 */
	@Before
	public void setUp() throws Exception {
		// Create a new login system to test
		handler = LoginDatabaseHandler.getInstance();
		IDataSet ds = getDataSet();
		dbTester.setDataSet(ds);
		dbTester.onSetup(); // dbunit requires you to do this

		handler.destroyAllLogins();
	}

	/**
	 * Set up for after test, assumed that dbunit requires this to happen.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		dbTester.onTearDown();
	}

	/**
	 * Tests that the database behaves as it should with an invalid username.
	 */
	@Test
	public void testInvalidLoginInvalidUsername() {
		String username = null;
		String password = "Beta";
		assertEquals("Invalid login details", false, handler.checkLogin(username, password));
	}

	/**
	 * Tests that the database behaves as it should with an invalid password.
	 */
	@Test
	public void testInvalidLoginInvalidPassword() {
		String username = "Alpha";
		String password = null;
		assertEquals("Invalid login details", false, handler.checkLogin(username, password));
	}

	/**
	 * Tests that the database behaves as it should with an invalid password and
	 * username.
	 */
	@Test
	public void testInvalidLoginInvalidUsernamePassword() {
		String username = null;
		String password = null;
		assertEquals("Invalid login details", false, handler.checkLogin(username, password));
	}

	/**
	 * Tests that the database behaves as it should with a valid username and
	 * password (that hasn't been added to the database yet)
	 */
	@Test
	public void testNoLoginExists() {
		String username = "Omega";
		String password = "Omega";
		assertEquals("0 x login member", 0, handler.checkRows());
		assertEquals("Valid login details not in list", false, handler.checkLogin(username, password));
	}

	/**
	 * Tests that the database behaves as it should with a valid username and
	 * password (that has been added to the database)
	 */
	@Test
	public void testLoginExistsWithLogger() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		String username = "Beta";
		String password = "Beta";
		Boolean muteMusicState = false;
		Boolean loopMusicState = false;
		Boolean muteSFXState = false;
		Double volumeMusicValue = 1.0;
		Double volumeSFXValue = 1.0;
		int resolutionWidth = 800;
		int resolutionHeight = 600;
		Boolean debugState = false;

		try {
			assertEquals("Valid login details not in list", false, handler.checkLogin(username, password));
			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("INFO - Login does not exist"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the database behaves as it should with a valid username and
	 * password (that has been added to the database)
	 */
	@Test
	public void testLoginExistsPartTwoWithLogger() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		String username = "Beta";
		String password = "Beta";
		Boolean muteMusicState = false;
		Boolean loopMusicState = false;
		Boolean muteSFXState = false;
		Double volumeMusicValue = 1.0;
		Double volumeSFXValue = 1.0;
		int resolutionWidth = 800;
		int resolutionHeight = 600;
		Boolean debugState = false;

		try {
			// The functions to test
			assertEquals("Valid login details not in list", false, handler.checkLogin(username, password));
			handler.registerLogin(username, password, muteMusicState, loopMusicState, muteSFXState, volumeMusicValue,
					volumeSFXValue, resolutionWidth, resolutionHeight, debugState);
			assertEquals("1 x login member", 1, handler.checkRows());
			assertEquals("Valid login details in list", true, handler.checkLogin(username, password));

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("INFO - Login exists"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the database behaves as it should with a valid username and
	 * password (that has been added to the database)
	 */
	@Test
	public void testLoginExistsPartThreeWithLogger() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		String username = "Beta";
		String password = "Beta";
		Boolean muteMusicState = false;
		Boolean loopMusicState = false;
		Boolean muteSFXState = false;
		Double volumeMusicValue = 1.0;
		Double volumeSFXValue = 1.0;
		int resolutionWidth = 800;
		int resolutionHeight = 600;
		Boolean debugState = false;

		try {
			// The functions to test
			assertEquals("Valid login details not in list", false, handler.checkLogin(username, password));
			handler.registerLogin(username, password, muteMusicState, loopMusicState, muteSFXState, volumeMusicValue,
					volumeSFXValue, resolutionWidth, resolutionHeight, debugState);
			assertEquals("1 x login member", 1, handler.checkRows());
			handler.registerLogin(username, password, muteMusicState, loopMusicState, muteSFXState, volumeMusicValue,
					volumeSFXValue, resolutionWidth, resolutionHeight, debugState);

			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("INFO - Login already exists"));
			assertEquals("1 x login member", 1, handler.checkRows());
			assertEquals("Valid login details in list", true, handler.checkLogin(username, password));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the database behaves as it should with a valid username and
	 * password (that has been added to the database)
	 */
	@Test
	public void testLoginExistsPartFourWithLogger() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		String username = "Beta";
		String password = "Beta";
		Boolean muteMusicState = false;
		Boolean loopMusicState = false;
		Boolean muteSFXState = false;
		Double volumeMusicValue = 1.0;
		Double volumeSFXValue = 1.0;
		int resolutionWidth = 800;
		int resolutionHeight = 600;
		Boolean debugState = false;

		try {
			// The functions to test
			assertEquals("Valid login details not in list", false, handler.checkLogin(username, password));
			handler.registerLogin(username, password, muteMusicState, loopMusicState, muteSFXState, volumeMusicValue,
					volumeSFXValue, resolutionWidth, resolutionHeight, debugState);
			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("INFO - Login successfully added"));
			assertEquals("1 x login member", 1, handler.checkRows());
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the database with a single entry behaves as it should with
	 * updating a users details.
	 */
	@Test
	public void updateSavedSettingsSingleUser() {
		double delta = 1e-15;
		String username = "Beta";
		String password = "Beta";
		Boolean muteMusicState = false;
		Boolean loopMusicState = false;
		Boolean muteSFXState = false;
		Double volumeMusicValue = 1.0;
		Double volumeSFXValue = 1.0;
		int resolutionWidth = 800;
		int resolutionHeight = 600;
		Boolean debugState = false;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Layout layout = new SimpleLayout();
		Appender appender = new WriterAppender(layout, outputStream);
		LOGGER.addAppender(appender);

		try {
			handler.registerLogin(username, password, muteMusicState, loopMusicState, muteSFXState, volumeMusicValue,
					volumeSFXValue, resolutionWidth, resolutionHeight, debugState);

			assertEquals("1 login member", 1, handler.checkRows());

			assertEquals("mutemusic", false, handler.getSavedSettingsBoolean(username, password, "MUTEMUSIC"));
			assertEquals("loopmusic", false, handler.getSavedSettingsBoolean(username, password, "LOOPMUSIC"));
			assertEquals("mutesfx", false, handler.getSavedSettingsBoolean(username, password, "MUTESFX"));
			assertEquals("musicsfx", 1.0, handler.getSavedSettingsDouble(username, password, "MUSICVALUE"), delta);
			assertEquals("sfxvolume", 1.0, handler.getSavedSettingsDouble(username, password, "SFXVOLUME"), delta);
			assertEquals("debug", false, handler.getSavedSettingsBoolean(username, password, "DEBUG"));
			assertEquals("resolutionWidth", 800, handler.getSavedSettingsInt(username, password, "RESOLUTIONWIDTH"));
			assertEquals("resolutionHeight", 600, handler.getSavedSettingsInt(username, password, "RESOLUTIONHEIGHT"));

			username = "Beta";
			password = "Beta";
			muteMusicState = true;
			loopMusicState = true;
			muteSFXState = true;
			volumeMusicValue = 0.0;
			volumeSFXValue = 0.0;
			resolutionWidth = 1024;
			resolutionHeight = 1024;
			debugState = true;

			handler.updateSavedSettings(username, password, muteMusicState, loopMusicState, muteSFXState,
					volumeMusicValue, volumeSFXValue, resolutionWidth, resolutionHeight, debugState);
			String logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("INFO - Updating saved settings"));

			// Checking that the getting of details from DB behave as they
			// should
			assertEquals("mutemusic", true, handler.getSavedSettingsBoolean(username, password, "MUTEMUSIC"));
			logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("INFO - Audio or debug-mode details retrieved"));
			assertEquals("loopmusic", true, handler.getSavedSettingsBoolean(username, password, "LOOPMUSIC"));
			assertEquals("mutesfx", true, handler.getSavedSettingsBoolean(username, password, "MUTESFX"));
			assertEquals("musicsfx", 0.0, handler.getSavedSettingsDouble(username, password, "MUSICVALUE"), delta);
			logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("INFO - Volume details retrieved"));
			assertEquals("sfxvolume", 0.0, handler.getSavedSettingsDouble(username, password, "SFXVOLUME"), delta);
			assertEquals("debug", true, handler.getSavedSettingsBoolean(username, password, "DEBUG"));
			assertEquals("resolutionWidth", 1024, handler.getSavedSettingsInt(username, password, "RESOLUTIONWIDTH"));
			logMessage = outputStream.toString();
			assertNotNull(logMessage);
			assertTrue(logMessage.contains("INFO - Resolution width or height retrieved"));
			assertEquals("resolutionHeight", 1024, handler.getSavedSettingsInt(username, password, "RESOLUTIONHEIGHT"));
		} finally {
			LOGGER.removeAppender(appender);
		}
	}

	/**
	 * Tests that the database with multiple entries behaves as it should with
	 * updating multiple users details.
	 */
	@Test
	public void updateSavedSettingsMultiUser() {
		double delta = 1e-15;
		String username = "Beta";
		String password = "Beta";
		Boolean muteMusicState = false;
		Boolean loopMusicState = false;
		Boolean muteSFXState = false;
		Double volumeMusicValue = 1.0;
		Double volumeSFXValue = 1.0;
		int resolutionWidth = 800;
		int resolutionHeight = 600;
		Boolean debugState = false;

		String username2 = "Alpha";
		String password2 = "Alpha";
		Boolean muteMusicState2 = true;
		Boolean loopMusicState2 = false;
		Boolean muteSFXState2 = false;
		Double volumeMusicValue2 = 1.0;
		Double volumeSFXValue2 = 1.0;
		int resolutionWidth2 = 1080;
		int resolutionHeight2 = 600;
		Boolean debugState2 = true;

		// First user entered in db
		handler.registerLogin(username, password, muteMusicState, loopMusicState, muteSFXState, volumeMusicValue,
				volumeSFXValue, resolutionWidth, resolutionHeight, debugState);

		assertEquals("1 x login member", 1, handler.checkRows());

		assertEquals("mutemusic", false, handler.getSavedSettingsBoolean(username, password, "MUTEMUSIC"));
		assertEquals("loopmusic", false, handler.getSavedSettingsBoolean(username, password, "LOOPMUSIC"));
		assertEquals("mutesfx", false, handler.getSavedSettingsBoolean(username, password, "MUTESFX"));
		assertEquals("musicsfx", 1.0, handler.getSavedSettingsDouble(username, password, "MUSICVALUE"), delta);
		assertEquals("sfxvolume", 1.0, handler.getSavedSettingsDouble(username, password, "SFXVOLUME"), delta);
		assertEquals("debug", false, handler.getSavedSettingsBoolean(username, password, "DEBUG"));
		assertEquals("resolutionWidth", 800, handler.getSavedSettingsInt(username, password, "RESOLUTIONWIDTH"));
		assertEquals("resolutionHeight", 600, handler.getSavedSettingsInt(username, password, "RESOLUTIONHEIGHT"));

		// Second user entered in db
		handler.registerLogin(username2, password2, muteMusicState2, loopMusicState2, muteSFXState2, volumeMusicValue2,
				volumeSFXValue2, resolutionWidth2, resolutionHeight2, debugState2);

		assertEquals("2 x login members", 2, handler.checkRows());

		assertEquals("mutemusic2", true, handler.getSavedSettingsBoolean(username2, password2, "MUTEMUSIC"));
		assertEquals("loopmusic2", false, handler.getSavedSettingsBoolean(username2, password2, "LOOPMUSIC"));
		assertEquals("mutesfx2", false, handler.getSavedSettingsBoolean(username2, password2, "MUTESFX"));
		assertEquals("musicsfx2", 1.0, handler.getSavedSettingsDouble(username2, password2, "MUSICVALUE"), delta);
		assertEquals("sfxvolume2", 1.0, handler.getSavedSettingsDouble(username2, password2, "SFXVOLUME"), delta);
		assertEquals("debug2", true, handler.getSavedSettingsBoolean(username2, password2, "DEBUG"));
		assertEquals("resolutionWidth", 1080, handler.getSavedSettingsInt(username2, password2, "RESOLUTIONWIDTH"));
		assertEquals("resolutionHeight", 600, handler.getSavedSettingsInt(username2, password2, "RESOLUTIONHEIGHT"));

		// User 1 changes details
		username = "Beta";
		password = "Beta";
		muteMusicState = true;
		loopMusicState = true;
		muteSFXState = true;
		volumeMusicValue = 0.0;
		volumeSFXValue = 0.0;
		resolutionWidth = 1024;
		resolutionHeight = 1024;
		debugState = true;

		handler.updateSavedSettings(username, password, muteMusicState, loopMusicState, muteSFXState, volumeMusicValue,
				volumeSFXValue, resolutionWidth, resolutionHeight, debugState);

		assertEquals("mutemusic", true, handler.getSavedSettingsBoolean(username, password, "MUTEMUSIC"));
		assertEquals("loopmusic", true, handler.getSavedSettingsBoolean(username, password, "LOOPMUSIC"));
		assertEquals("mutesfx", true, handler.getSavedSettingsBoolean(username, password, "MUTESFX"));
		assertEquals("musicsfx", 0.0, handler.getSavedSettingsDouble(username, password, "MUSICVALUE"), delta);
		assertEquals("sfxvolume", 0.0, handler.getSavedSettingsDouble(username, password, "SFXVOLUME"), delta);
		assertEquals("debug", true, handler.getSavedSettingsBoolean(username, password, "DEBUG"));
		assertEquals("resolutionWidth", 1024, handler.getSavedSettingsInt(username, password, "RESOLUTIONWIDTH"));
		assertEquals("resolutionHeight", 1024, handler.getSavedSettingsInt(username, password, "RESOLUTIONHEIGHT"));

		// User 2 changes details
		username2 = "Alpha";
		password2 = "Alpha";
		muteMusicState2 = false;
		loopMusicState2 = true;
		muteSFXState2 = false;
		volumeMusicValue2 = 1.0;
		volumeSFXValue2 = 1.0;
		resolutionWidth2 = 800;
		resolutionHeight2 = 600;
		debugState2 = false;

		handler.updateSavedSettings(username2, password2, muteMusicState2, loopMusicState2, muteSFXState2,
				volumeMusicValue2, volumeSFXValue2, resolutionWidth2, resolutionHeight2, debugState2);

		assertEquals("mutemusic", false, handler.getSavedSettingsBoolean(username2, password2, "MUTEMUSIC"));
		assertEquals("loopmusic", true, handler.getSavedSettingsBoolean(username2, password2, "LOOPMUSIC"));
		assertEquals("mutesfx", false, handler.getSavedSettingsBoolean(username2, password2, "MUTESFX"));
		assertEquals("musicsfx", 1.0, handler.getSavedSettingsDouble(username2, password2, "MUSICVALUE"), delta);
		assertEquals("sfxvolume", 1.0, handler.getSavedSettingsDouble(username2, password2, "SFXVOLUME"), delta);
		assertEquals("debug", false, handler.getSavedSettingsBoolean(username2, password2, "DEBUG"));
		assertEquals("resolutionWidth", 800, handler.getSavedSettingsInt(username2, password2, "RESOLUTIONWIDTH"));
		assertEquals("resolutionHeight", 600, handler.getSavedSettingsInt(username2, password2, "RESOLUTIONHEIGHT"));

		// Just checking to make sure the changes of the other user haven't been
		// affected
		assertEquals("mutemusic", true, handler.getSavedSettingsBoolean(username, password, "MUTEMUSIC"));
		assertEquals("loopmusic", true, handler.getSavedSettingsBoolean(username, password, "LOOPMUSIC"));
		assertEquals("mutesfx", true, handler.getSavedSettingsBoolean(username, password, "MUTESFX"));
		assertEquals("musicsfx", 0.0, handler.getSavedSettingsDouble(username, password, "MUSICVALUE"), delta);
		assertEquals("sfxvolume", 0.0, handler.getSavedSettingsDouble(username, password, "SFXVOLUME"), delta);
		assertEquals("debug", true, handler.getSavedSettingsBoolean(username, password, "DEBUG"));
		assertEquals("resolutionWidth", 1024, handler.getSavedSettingsInt(username, password, "RESOLUTIONWIDTH"));
		assertEquals("resolutionHeight", 1024, handler.getSavedSettingsInt(username, password, "RESOLUTIONHEIGHT"));
	}

}