package minesim;

import minesim.entities.items.Weapon;
import minesim.tiles.Tile;
import minesim.tiles.TileChunk;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseHandlerTests {

    //database tester (used for connecting to the database)
    private static IDatabaseTester dbTester;
    //store being tested
    private DatabaseHandler handler;
    private static final Logger logger = LogManager.getLogger(DatabaseHandlerTests.class);

    /**
     * Connect to the database
     */
    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException {
        dbTester = new JdbcDatabaseTester(
                "org.apache.derby.jdbc.EmbeddedDriver",
                "jdbc:derby:decoMineDB;create=true;user=decomine;password=decomine");
    }

    /**
     * Load in a set of data from a (fixed for now) XML file
     *
     * @return data set to be provided to database tester (or used for
     * comparison)
     */
    private IDataSet getDataSet() throws DataSetException, IOException {
        URL url = DatabaseHandler.class.getClassLoader().getResource("DatabaseHandlerTest.xml");
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        return builder.build(url);
    }

    /**
     * Set up for each test
     */
    @Before
    public void setUp() throws Exception {
        //create a new store to test
        handler = DatabaseHandler.getInstance();
        IDataSet ds = getDataSet();
        dbTester.setDataSet(ds);
        dbTester.onSetup(); //dbunit requires you to do this
        handler.destroyAllStock();
        handler.clearChunks();
    }

    @After
    public void tearDown() throws Exception {
        dbTester.onTearDown();
    }

    @Test
    public void testChunkSave() {
        assertNotNull(handler);
        assertEquals("Chunk count not zero.", 0, handler.countChunks());

        // Create a new, non-symetric chunk.
        TileChunk newchunk = new TileChunk();
        newchunk.setTileType(0, 0, Tile.STONE);
        newchunk.setTileType(2, 1, Tile.GRASS);
        newchunk.setTileType(4, 2, Tile.DIRT);

        // Save and account for that chunk.
        handler.saveChunk(0, 0, newchunk);
        assertEquals("Chunk not saved.", 1, handler.countChunks());

        // Get a chunk back out of the database.
        TileChunk retrived_chunk = handler.getChunk(0, 0);

        assertEquals("Stone tile not valid", Tile.STONE, retrived_chunk.getTileType(0, 0));
        assertEquals("Stone tile not valid", Tile.GRASS, retrived_chunk.getTileType(2, 1));
        assertEquals("Stone tile not valid", Tile.DIRT, retrived_chunk.getTileType(4, 2));
    }

    @Test
    public void ItemManipulation() {

        assertNotNull(handler);
        assertEquals("StockTable not empty", 0, handler.stockItemCount());

        //add item to table
        assertEquals("sword item not added to stock table", 1, handler.addItem(1, 2, 3, "sword", 5, 99, "all", "weapon", "sword"));
        assertEquals("wing item not added to stock table", 1, handler.addItem(2, 3, 3, "wing", 2, 50, null, null, null));
        assertEquals("item count wrong ", 2, handler.stockItemCount());
        assertEquals("incorrect Stock", 5, handler.getStock("sword"));
        Weapon testItem = new Weapon(1, 2, 3, 4, "sword", 99, 99, "all", "sword");
        logger.warn("my items name");
        logger.warn(testItem.getName());
        assertEquals("Retrieved item is bad ", testItem.getName(), handler.getItem("sword", 2).getName());
        assertEquals("bad stock removal", 3, handler.getStock("sword"));
        assertEquals("stock was unable to be added", true, handler.addStock("sword", 1));
        assertEquals("incorrect stock added", 4, handler.getStock("sword"));
        assertEquals("correctly returns no item", null, handler.getItem("sward", 1));
        handler.subtractStock("sword", 5);
        assertEquals("allowed negative stock decrease", 4, handler.getStock("sword"));
        assertEquals("allowed negative addition", false, handler.addStock("sword", -10));
        assertEquals("incorrectly handles items that don't exist being looked up", -1, handler.getStock("fake"));

    }

    @Test
    public void selectItems() {

        assertNotNull(handler);
        assertEquals("StockTable not empty", 0, handler.stockItemCount());

        assertEquals("sword item not added to stock table", 1, handler.addItem(1, 2, 3, "sword", 5, 99,  null, null ,null));
        assertEquals("wing item not added to stock table", 1, handler.addItem(2, 3, 3, "wing", 2, 50,  null, null, null));

        List<StockInfo> testStock = handler.getAllItems();
        assertEquals("number of entires incorrect", 2, testStock.size());
        for (StockInfo stock : testStock) {
            if (stock.getName().equals("sword")) {
                assertEquals("incorrect Stock", stock.getStock(), 5);
            } else if (stock.getName().equals("wing")) {
                assertEquals("incorrect Stock", stock.getStock(), 2);
            } else {
                assertEquals("incorrect name", true, false);
            }
        }

    }


}