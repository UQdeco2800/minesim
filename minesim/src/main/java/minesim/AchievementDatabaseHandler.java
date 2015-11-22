package minesim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import minesim.events.tracker.AchievementTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import minesim.entities.Achievement;
import minesim.World;

public class AchievementDatabaseHandler {

    /**
     * Code copied from DatabaseHandler and then modified. We use a logger to stop crazy amounts of
     * print statements. We are also going to be saving these to a log file
     */
    private static final Logger logger = LoggerFactory.getLogger(AchievementDatabaseHandler.class);

    private static String dbURL = "jdbc:derby:decoMineDB;create=true;user=decomine;password=decomine";

    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;

    private static AchievementDatabaseHandler instance = null;
    private int rockCount;
	private int zombieKilled;

    /**
     * Checks if there is an active connection to AchievementDatabase. If there is no connection,
     * creates connection and creates the table.
     */
    public static AchievementDatabaseHandler getInstance() {
        if (instance == null) {
            instance = new AchievementDatabaseHandler();
            createConnection();
            createplayerStatsTable();
        }
        return instance;
    }

    /**
     * Starts the connection to database.
     */
    private static void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(dbURL);
            logger.info("Connected database successfully");
        } catch (Exception except) {
            logger.error("Create database connection failed");
            instance = null;
        }

    }

    /**
     * creates playerStats table
     */
    public static void createplayerStatsTable() {
        try {
            if( conn == null ){
                logger.error("No database connection.");
                return;
            }
            
            Statement sta = conn.createStatement();
            sta.executeUpdate("CREATE TABLE playerStats (rockCounter INT)");
            logger.debug("Table rocks created.");
            sta.close();
            conn.close();
        } catch (SQLException e) {
            logger.debug("Table may already exist");
        }
    }

    /**
     * Increments and updates values in a column by 1 to the playerStats table.
     */
    public void incrementCounter(String column) {
        try {
            String sql = "UPDATE playerStats SET ? = ? + 1";
            PreparedStatement prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, column);
            prepStmt.setString(2, column);
            int i = prepStmt.executeUpdate();
            conn.commit();
            logger.debug("Incremented and updated {} by 1 to the playerStats table. {} lines changed", column, i);
            prepStmt.close();
            conn.close();
        } catch (SQLException sqlExcept) {
            logger.error("Failed to increment {}.", column);
        }
    }

    /**
     * reset rockCounter to 0
     */
    public void resetCounter(String column) {
        try {
            String sql = "UPDATE playerStats SET ? = 0";
            PreparedStatement prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, column);
            int i = prepStmt.executeUpdate();
            conn.commit();
            logger.info("{} reset to 0 in the playerStats table. {} lines changed", column, i);
            prepStmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.debug("Reset all counters failed");
        }
    }
    
    
    /**
     * Localises all counters in the database.
     */
    public void achievementSaver() {
        try {
            String sql = "SELECT * from playerStats";
            PreparedStatement prepStmt = conn.prepareStatement(sql);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                this.rockCount = rs.getInt("rockCounter");
                this.zombieKilled = rs.getInt("zombieKilled");
            }
        } catch (SQLException e) {
            logger.debug("Failed to retrieve data. achievementSaver()");
        }
    }

    /**
     * checks if the rockCounter matches conditions.
     *
     * @return variable type to use as a parameter when calling the achievement.
     */
    public void checker() {
        String type = null;
        if (this.rockCount == 50) {
            type = "rock50";
            World.getInstance().addAchievmentToWorld(new Achievement(100, 100, 80, 480, type));
            AchievementTracker.achievementHappened();
        } else if (this.rockCount == 20) {
            type = "rock20";
            World.getInstance().addAchievmentToWorld(new Achievement(100, 100, 80, 480, type));
            AchievementTracker.achievementHappened();
        } else if (this.rockCount == 1) {
            type = "rock1";
            World.getInstance().addAchievmentToWorld(new Achievement(100, 100, 80, 480, type));
            AchievementTracker.achievementHappened();
        }
        if (this.zombieKilled == 1){
        	type = "zombie1";
        	World.getInstance().addAchievmentToWorld(new Achievement(100, 100, 80, 480, type));
            AchievementTracker.achievementHappened();
        } else if (this.zombieKilled == 20){
        	type = "zombie20";
        	World.getInstance().addAchievmentToWorld(new Achievement(100, 100, 80, 480, type));
            AchievementTracker.achievementHappened();
        } else if (this.zombieKilled == 50){
        	type = "zombie50";
        	World.getInstance().addAchievmentToWorld(new Achievement(100, 100, 80, 480, type));
            AchievementTracker.achievementHappened();
        }
    }
    
    /**
     * Increments a counter like
     * "rockCount" | "zombieKilled"
     * and then checks the counters to achievement conditions.
     * @param counter
     */
    public void counterIncrement(String counter){
    	switch(counter){
    		case "rockCount": 
    			this.rockCount += 1;
    			logger.info("rockCount is now {}", this.rockCount);
    			break;
    		case "zombieKilled":
    			this.zombieKilled += 1;
    			logger.info("zombieKilled is now {}", this.zombieKilled);
    			break;
    	}
    	checker();
    }
    
}
