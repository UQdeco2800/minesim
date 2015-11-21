package minesim;



import minesim.entities.items.*;
import minesim.tiles.TileChunk;
import minesim.tiles.TileGridManager;
import minesim.tiles.TileNotFoundException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler {

    /**
     * We use a logger to stop crazy amounts of print statements. We are also going to be saving
     * these to a log file
     */
    private static final Logger logger = LogManager.getLogger(DatabaseHandler.class);

    private static String dbURL = "jdbc:derby:decoMineDB;create=true;user=decomine;password=decomine";

    // jdbc Connection
    private static Connection conn = null;

    private static DatabaseHandler instance = null;

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
            createConnection();


        }
        return instance;
    }

    private static void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(dbURL);
        } catch (Exception except) {

            logger.error("Create database connection failed", except);
            instance = null;
        }

    }


    /**

     * Count the number of chunks currently stored in the database.
     */
    public static int countChunks() {
        String sql = "SELECT COUNT(*) FROM chunks";
        int count = 0;

        try {
            Statement st = conn.createStatement();
            ResultSet results = st.executeQuery(sql);
            while (results.next()) {
                count = results.getInt(1);
            }
        }
        catch (SQLException sqlError) {
            logger.error(sqlError);
            logger.warn("Counting Chunks failed (SQL not executed).");
        }
        return count;
    }

    /**
     * Save a chunk to the data base at a set of coordinates.
     *
     * @param chunkX the horizontal *Chunk* postion to save the chunk at.
     * @param chunkY the vertical *Chunk* postion to save the chunk at.
     * @param chunk  the chunk for which tile's will be stored.
     */
    public static int saveChunk(int chunkX, int chunkY, TileChunk chunk) {
        String sql = "INSERT INTO chunks VALUES (?, ?, ?)";
        int rowsAffected = 0;

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, chunkX);
            stmt.setInt(2, chunkY);
            stmt.setBinaryStream(3, new ByteArrayInputStream(chunk.toBytes()));
            rowsAffected = stmt.executeUpdate();
        } catch (SQLException sqlError) {
        	logger.error(sqlError);
            logger.warn("Saving chunk to the database failed.");
        }
        return rowsAffected;
    }

    /**
     * Retrieve a chunk from the database
     *
     * @param chunkX the horizontal *Chunk* postion to save the chunk at.
     * @param chunkY the vertical *Chunk* postion to save the chunk at.
     * @return a TileChunk from the location requested.
     * @throws TileNotFoundException If a chunk at the coordinates specified does not exist.
     */
    public static TileChunk getChunk(int chunkX, int chunkY) {
        String sql = "SELECT data FROM chunks WHERE pos_x = ? AND pos_y = ?";

        ResultSet results;
        byte[] chunkData = new byte[TileGridManager.CHUNK_GRID_SIZE * TileGridManager.CHUNK_GRID_SIZE];

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, chunkX);
            stmt.setInt(2, chunkY);
            results = stmt.executeQuery();
            while (results.next()) {
                chunkData = results.getBytes(1);
            }
        } catch (SQLException sqlError) {
        	logger.error(sqlError);
            logger.warn("Saving chunk to the database failed.");
            throw new TileNotFoundException("Chunk not found");
        }

        return new TileChunk(chunkData);
    }

    /**
     * Method to remove all chunk data from the database.
     */
    public static void clearChunks() {
        try {
            String sql = "DELETE FROM chunks";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.executeUpdate();
        } catch (SQLException sqlError) {
            logger.warn("Deleting chunks from the database failed.");
        }
    }

    /**
     * Add new item to stock table
     */
    public static int addItem(int id, int height, int width, String name, int stock, int stack,
                               String peonClass, String itemClass, String itemType ) {

        if (inTable(name)) {
            logger.debug("item already exists");
            return 0;
        }
        String query = "INSERT INTO stockTable(id, height, width, itemName, stock, stack, peonClass, itemClass, itemType" +
                " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int rowsChanged = 0;
        try {
            logger.warn("read?");
            PreparedStatement sta = conn.prepareStatement(query);
            sta.setInt(1, id);
            sta.setInt(2, height);
            sta.setInt(3, width);
            sta.setString(4, name);
            sta.setInt(5, stock);
            sta.setInt(6, stack);
            if (peonClass == null) {
                sta.setNull(7, Types.VARCHAR);
            } else {
                sta.setString(7, peonClass);
            }
            if (itemClass == null) {
                sta.setNull(8, Types.VARCHAR);
            } else {
                sta.setString(8, itemClass);
            }
            if (itemType == null) {
                sta.setNull(9, Types.VARCHAR);
            } else {
                sta.setString(9, itemType);
            }
            logger.warn("WHY?");
            rowsChanged = sta.executeUpdate();
        } catch (SQLException e) {
            logger.warn("unable to add stock item " + e);
            logger.debug("unable to add stock item " + e);
        }
        return rowsChanged;
    }


    /**
     * Method to remove all items from the stock table,
     */
    public static void destroyAllStock() {
        try {
            String sql = "DELETE FROM stockTable";
            PreparedStatement sta = conn.prepareStatement(sql);
            sta.executeUpdate();
            sta.close();
        } catch (SQLException sql_error) {
            logger.debug("Deleting StockTable items failed " + sql_error);
        }
    }


    /**
     * check if only one item of that name exists
     **/

    public static boolean inTable(String name) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM stockTable WHERE itemName=?";

        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, name);
            ResultSet results = st.executeQuery();
            while (results.next()) {
                count = results.getInt(1);
            }
            st.close();
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            logger.warn("failed to sql - find row " + e);
            logger.warn("failed to sql - find row " + e);
        }
        return false;
    }



    public List<StockInfo> getAllItems() {
        String query = "SELECT itemName, stock FROM StockTable";
        List<StockInfo> items = new ArrayList<>();

        try {
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet results = st.executeQuery();
            while (results.next()) {
                items.add(new StockInfo(results.getString(1), results.getInt(2)));
            }
            return items;
        } catch (SQLException e) {
            logger.debug("unable to get items " + e);
            return new ArrayList<StockInfo>();
        }
    }


    /**
     * increases the stock value by a sent amount for the given item
     **/

    public boolean addStock(String name, int amount) {
        if (amount <= 0) {
            logger.warn("added negative or 0");
            logger.debug("added negative or 0");
            return false;
        }
        String query = "UPDATE stockTable" +
                " SET stock = stock + ? WHERE " +
                "itemName=?";
        try {
            PreparedStatement sta = conn.prepareStatement(query);
            sta.setInt(1, amount);
            sta.setString(2, name);
            sta.executeUpdate();
            sta.close();
            logger.debug("stock updated");
            return true;
        } catch (SQLException e) {
            logger.warn(e);
            logger.debug("failed to add stock");
            return false;
        }

    }

    /**
     * returns the current stock of the selected item
     **/
    public int getStock(String name) {
        logger.warn("looking for this" +name);
        String query = "SELECT stock FROM stockTable WHERE itemName=?";
        try {
            PreparedStatement sta = conn.prepareStatement(query);
            sta.setString(1, name);
            ResultSet resultSet = sta.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("stock");
            }
        } catch (SQLException e) {
            logger.warn("failed to get stock "+ e);
            logger.debug("failed to get stock " + e);
            return -2;
        }
        return -1;
    }



    /**
     * removes stock from the selected item by the given amount
     **/
    public boolean subtractStock(String name, int amount) {
        if ((getStock(name) - amount < 0) || amount <= 0) {
            return false;
        }
        String query = "UPDATE stockTable" +
                " SET stock = stock -?  WHERE " +
                "itemName=?";
        try {
            PreparedStatement sta = conn.prepareStatement(query);
            sta.setInt(1, amount);
            sta.setString(2, name);
            sta.executeUpdate();
            sta.close();
            logger.warn("stock subtracted");
            logger.debug("stock subtracted");
            return true;
        } catch (SQLException e) {
            logger.warn(e);
            logger.debug("failed to subtracted stock");
            return false;
        }
    }


    /**
     * creates an item from the values given in the database
     **/
    public Item getItem(String name, int amount) {
        if ((getStock(name) - amount < 0) || amount <= 0) {
            return null;
        }
        String query = "SELECT * FROM stockTable WHERE itemName=? AND stock >=?";
        Item item = null;
        try {
            PreparedStatement sta = conn.prepareStatement(query);

            sta.setString(1, name);
            sta.setInt(2, amount);
            ResultSet result = sta.executeQuery();
            while (result.next()) {
                int width = result.getInt("width");
                int height = result.getInt("height");
                int stack = result.getInt("stack");
                String pClass = result.getString("peonClass");
                String type = result.getString("itemType");
                String iClass = result.getString("itemClass");
                if (iClass == null) {
                    item = new Item(0, 0, height, width, name, stack);
                } else {
                    switch (iClass) {
                        case "appreal":
                            item = new Apparel(0, 0, 0, 0, name, 99, 0, pClass, type);
                            break;
                        case "tool":
                            item = new Tool(0, 0, 0, 0, name, 99, 0, pClass, type);
                            break;
                        case "weapon":
                            item = new Weapon(0, 0, 0, 0, name, 99, 0, pClass, type);
                            break;
                        case "minedEntity":
                            item = new MinedEntity(0, 0, 0, 0, name, 99);
                            break;
                        case "food":
                            item = new Food(0, 0, 0, 0, name, 99);
                            break;
                        default:
                            item = new Item(0, 0, height, width, name, stack);
                    }
                }
            }
            sta.close();
        } catch (SQLException e) {
            logger.debug("failed to get get item", e);
        }
        if (item == null) {
            logger.warn("dayum");
            return null;
        } else {
            subtractStock(name, amount);
            return item;
        }
    }

    public static int stockItemCount() {
        String sql = "SELECT COUNT(*) FROM stockTable";
        int count = 0;

        try {
            Statement st = conn.createStatement();
            ResultSet results = st.executeQuery(sql);
            while (results.next()) {
                count = results.getInt(1);
            }
        }
        catch (SQLException sqlError) {
            logger.warn("stock item count failed"+sqlError);
            logger.debug("stock item count failed" + sqlError);
            return -1;
        }
        return count;
    }
}

