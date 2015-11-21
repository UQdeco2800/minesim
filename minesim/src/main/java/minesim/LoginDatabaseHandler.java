package minesim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to deal with database operations pertaining to user login. These are
 * namely, updating and inserting.
 * 
 * @author dayakern
 *
 */
public final class LoginDatabaseHandler {

	// Store the singleton instance of the login manager
	private static LoginDatabaseHandler instance;
	// The database connection url
	private static String dbURL = "jdbc:derby:decoMineDB;create=true;user=decomine;password=decomine";
	// The jdbc connection
	private static Connection conn = null;
	// Logger which will display a message a based on the error
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginDatabaseHandler.class);

	/**
	 * A public getter method for an instance of the LoginDatabase. If that
	 * instance is null, it creates a new handler and establishes a connection.
	 * 
	 * @return The singleton instance of the LoginDatabase
	 */
	public static LoginDatabaseHandler getInstance() {
		if (instance == null) {
			instance = new LoginDatabaseHandler();
			createConnection();
		}
		return instance;
	}

	/**
	 * A private function which results in a successful database connection. If
	 * the connection fails due to an exception, the error message is sent to
	 * the logger.
	 */
	private static void createConnection() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			conn = DriverManager.getConnection(dbURL);
			LOGGER.info("Connected database successfully");
		} catch (Exception e) {
			LOGGER.error("Create database connection failed", e);
			instance = null;
		}
	}

	/**
	 * A public function which checks whether or not the user-name entered in
	 * the login menu already exists in the list of saved login details.
	 * 
	 * @param username
	 *            the login name the user typed in.
	 * @param password
	 *            the password the user typed in.
	 * @return true if the user-name exists, false otherwise.
	 */
	public static boolean checkLogin(String username, String password) {
		// Establish the initial connection
		getInstance();
		// The SQL query to be executed
		String sqlQuery = "SELECT USERNAME FROM LOGIN WHERE USERNAME = ? AND " + "PASSWORD = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sqlQuery);
			// Fills in the blanks for the query to be executed
			stmt.setString(1, username);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				stmt.close();
				// Valid credentials
				LOGGER.info("Login exists");
				return true;
			} else {
				stmt.close();
				// Invalid credentials
				LOGGER.info("Login does not exist");
				return false;
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to execute login check ", e);
		}
		return false;
	}

	/**
	 * A public function which updates the contents of the data according to the
	 * username.
	 * 
	 * @param username
	 *            the username typed in
	 * @param password
	 *            the password typed in
	 * @param muteMusicState
	 *            the mute music control
	 * @param loopMusicState
	 *            the loop music control
	 * @param muteSFXState
	 *            the mute sfx control
	 * @param volumeMusicValue
	 *            the volume music control
	 * @param volumeSFXValue
	 *            the volumen sfx control
	 * @param resolutionWidth
	 *            the width of the game canvas
	 * @param resolutionHeight
	 *            the height of the game canvas
	 * @param debugState
	 *            the debug-mode control
	 */
	public static void updateSavedSettings(String username, String password, Boolean muteMusicState,
			Boolean loopMusicState, Boolean muteSFXState, Double volumeMusicValue, Double volumeSFXValue,
			int resolutionWidth, int resolutionHeight, Boolean debugState) {

		String sqlQuery = "UPDATE LOGIN SET MUTEMUSIC=?, LOOPMUSIC=?, MUTESFX=?,"
				+ "MUSICVALUE=?, SFXVOLUME=?, RESOLUTIONWIDTH=?,RESOLUTIONHEIGHT=?, DEBUG=?"
				+ "  WHERE USERNAME=? AND PASSWORD=?";

		try {
			PreparedStatement stmt = conn.prepareStatement(sqlQuery);
			stmt.setString(9, username);
			stmt.setString(10, password);

			stmt.setBoolean(1, muteMusicState);
			stmt.setBoolean(2, loopMusicState);
			stmt.setBoolean(3, muteSFXState);
			stmt.setDouble(4, volumeMusicValue);
			stmt.setDouble(5, volumeSFXValue);
			stmt.setInt(6, resolutionWidth);
			stmt.setInt(7, resolutionHeight);
			stmt.setBoolean(8, debugState);

			stmt.executeUpdate();
			LOGGER.info("Updating saved settings");
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error("Unable to execute login update ", e);
		}
	}

	/**
	 * Get the settings for all int settings
	 * 
	 * @param username
	 *            the username typed in
	 * @param password
	 *            the password typed in
	 * @param intType
	 *            the height or width for resolution settings
	 * @return the int-type setting which was retrieved
	 */
	public static int getSavedSettingsInt(String username, String password, String intType) {

		String query = "SELECT " + intType + " FROM LOGIN WHERE USERNAME = ? AND PASSWORD = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);

			stmt.setString(1, username);
			stmt.setString(2, password);

			ResultSet settings = stmt.executeQuery();
			if (settings.next()) {
				LOGGER.info("Resolution width or height retrieved");
				return settings.getInt(intType);
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to execute getting int-type argument ", e);
		}
		return 0;
	}

	/**
	 * Get the settings for all int settings
	 * 
	 * @param username
	 *            the username typed in
	 * @param password
	 *            the password typed in
	 * @param boolType
	 *            the mute,loop for audio & sound fx, and debug mode
	 * @return the boolean-type setting which was retrieved
	 */
	public static boolean getSavedSettingsBoolean(String username, String password, String boolType) {

		String sqlQuery = "SELECT " + boolType + " FROM LOGIN WHERE USERNAME = ? AND PASSWORD = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(sqlQuery);

			stmt.setString(1, username);
			stmt.setString(2, password);

			ResultSet settings = stmt.executeQuery();
			if (settings.next()) {
				LOGGER.info("Audio or debug-mode details retrieved");
				return settings.getBoolean(boolType);
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to execute getting boolean-type argument ", e);
		}
		return false;
	}

	/**
	 * Get the settings for all int settings
	 * 
	 * @param username
	 *            the username typed in
	 * @param password
	 *            the password typed in
	 * @param doubleType
	 *            the audio and sound fx volume
	 * @return the double-type setting which was retrieved
	 */
	public static double getSavedSettingsDouble(String username, String password, String doubleType) {

		String sqlQuery = "SELECT " + doubleType + " FROM LOGIN WHERE USERNAME = ? AND PASSWORD = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(sqlQuery);

			stmt.setString(1, username);
			stmt.setString(2, password);

			ResultSet settings = stmt.executeQuery();
			if (settings.next()) {
				LOGGER.info("Volume details retrieved");
				return settings.getDouble(doubleType);
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to execute getting double-type argument ", e);
		}
		return 0;
	}

	/**
	 * A function which adds to the database the new user if they are not
	 * already in there. Default settings are assigned.
	 * 
	 * @param username
	 *            the username typed in
	 * @param password
	 *            the password typed in
	 * @param muteMusicState
	 *            the mute music control
	 * @param loopMusicState
	 *            the loop music control
	 * @param muteSFXState
	 *            the mute sfx control
	 * @param volumeMusicValue
	 *            the volume music control
	 * @param volumeSFXValue
	 *            the volumen sfx control
	 * @param resolutionWidth
	 *            the width of the game canvas
	 * @param resolutionHeight
	 *            the height of the game canvas
	 * @param debugState
	 *            the debug-mode control
	 */
	public static void registerLogin(String username, String password, Boolean muteMusicState, Boolean loopMusicState,
			Boolean muteSFXState, Double volumeMusicValue, Double volumeSFXValue, int resolutionWidth,
			int resolutionHeight, Boolean debugState) {

		// User-name already exists in the database
		if (checkLogin(username, password)) {
			LOGGER.info("Login already exists");
			return;
		}
		String query = "INSERT INTO LOGIN VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setBoolean(3, muteMusicState);
			stmt.setBoolean(4, loopMusicState);
			stmt.setBoolean(5, muteSFXState);
			stmt.setDouble(6, volumeMusicValue);
			stmt.setDouble(7, volumeSFXValue);
			stmt.setInt(8, resolutionWidth);
			stmt.setInt(9, resolutionHeight);
			stmt.setBoolean(10, debugState);

			stmt.executeUpdate();
			stmt.close();
			LOGGER.info("Login successfully added");
		} catch (SQLException e) {
			LOGGER.error("Unable to register login ", e);
		}
	}

	/**
	 * FOR DEBUGGING PURPOSES ONLY. A method to clear the database from all
	 * login details
	 */
	public static void destroyAllLogins() {
		getInstance();
		try {
			String sql = "DELETE FROM LOGIN";
			PreparedStatement sta = conn.prepareStatement(sql);
			sta.executeUpdate();
			sta.close();
			LOGGER.info("All users destroyed");
		} catch (SQLException e) {
			LOGGER.error("Deleting logins failed ", e);
		}
	}

	/**
	 * FOR DEBUGGING PURPOSES ONLY. Checks the number of entries in the database
	 * 
	 * @return the number of users
	 */
	public static int checkRows() {
		int rowNumber = 0;
		try {
			String query2 = "SELECT COUNT(*) FROM LOGIN";
			PreparedStatement stmt2 = conn.prepareStatement(query2);
			ResultSet rs = stmt2.executeQuery();
			while (rs.next()) {
				rowNumber = rs.getInt(1);
			}
			stmt2.close();
		} catch (SQLException e) {
			LOGGER.error("Unable to get the number of users", e);
		}
		return rowNumber;
	}
}
