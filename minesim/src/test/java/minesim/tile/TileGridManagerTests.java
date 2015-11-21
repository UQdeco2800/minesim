package minesim.tile;

import minesim.tiles.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javafx.scene.canvas.GraphicsContext;
import minesim.entities.WorldEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Collection of tests for the TileGridManager class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GraphicsContext.class})
public class TileGridManagerTests {

    TileGridManager manager;
    TileChunk chunk_origin;

    @Before
    public void setUp() throws Exception {

        manager = new TileGridManager(new RollingHillsGenerator());
        chunk_origin = new TileChunk();
        manager.addChunk(0, 0, chunk_origin);
    }

    @Test
    public void testInitialise() {
        TileGridManager manager = new TileGridManager(new RollingHillsGenerator());
    }

    @Test
    public void testWorldGen() {
        TileGridManager manager = new TileGridManager(new RollingHillsGenerator());
        manager.generateWorld();
        manager.addModifier(new SquareVeinTileModifier());
        manager.addModifier(new PerlinVeinTileModifier());
        manager.modifyWorld();
    }

    @Test
    public void testChunkIsLoaded() {

        assertTrue("Chunk should be loaded", manager.chunkIsLoaded(0, 0));
        assertFalse("Chunk should not be loaded", manager.chunkIsLoaded(100, 100));

        // Unload a chunk and test the location is now unloaded.
        manager.unloadChunk(0, 0);
        assertFalse("Unloaded Chunk should not be loaded", manager.chunkIsLoaded(0, 0));
    }

    @Test
    public void testAddChunk() {
        // Verify count after addition from @before setup method.
        assertEquals("Chunk count != 1 after insertion", 1, manager.loadedChunkCount());

        TileChunk chunk2 = new TileChunk();
        manager.addChunk(1, 0, chunk2);
        assertEquals("Chunk count != 2 after insertion", 2, manager.loadedChunkCount());

        TileChunk chunk3 = new TileChunk(); // Same position as chunk_origin.
        // Add a chunk in the same location as another chunk.
        manager.addChunk(0, 0, chunk3);
        assertEquals("Chunk count != 2 after same place insertion", 2, manager.loadedChunkCount());
    }

    @Test
    public void testUnloadChunk() {
        TileChunk chunk2 = new TileChunk();

        manager.addChunk(1, 0, chunk2);
        assertEquals("Chunk count != 2 after insertion", 2, manager.loadedChunkCount());

        manager.unloadChunk(1, 0);  // Unload second chunk.
        assertEquals("Chunk count != 1 after insertion", 1, manager.loadedChunkCount());

    }

    /**
     * Test retrieving tiles from the TileGridManager using global coordinates.
     */
    @Test
    public void testGetTileAtLocation() {
        TileChunk chunk2 = new TileChunk();

        // Set a tile in the further chunk to grass.
        chunk2.setTileType(7, 7, Tile.GRASS);

        // Add chunks to manager.
        manager.addChunk(1, 1, chunk2);

        // Check for grass tile in global position (x,y = 16+7).
        Tile checktile = manager.getTileAtLocation(23, 23);

        assertEquals("Retrieved tiles not what was set", Tile.GRASS, checktile);
    }

    @Test
    public void testGetLightAtLocation() {
        TileChunk chunk2 = new TileChunk(Tile.DIRT);

        // Set a tile in the further chunk to grass.
        chunk2.setLightLevel(7, 7, (byte) 100);
        chunk2.setLightLevel(9, 9, (byte) 9);

        // Add chunks to manager.
        manager.addChunk(1, 1, chunk2);

        // Check for grass tile in global position (x,y = 16+(7, 9)).
        double checkLightLevel07 = manager.getLightLevelAtLocation(23, 23);
        double checkLightLevel9 = manager.getLightLevelAtLocation(25, 25);

        assertEquals("Retrieved light level not what was set", (byte)100, checkLightLevel07, 1e-5);
        assertEquals("Retrieved light level not what was set", (byte) 9, checkLightLevel9, 1e-5);
    }

    /**
     * Test the render method of TileGridManager. Disabled currently because
     * GraphicsContext is final and cannot be mocked.
     */
    @Test
    public void testRender() {
        final GraphicsContext context = mock(GraphicsContext.class);

        // Test air and empty space don't call .fillRect().
        manager.render(context, 0, 0, 800, 600);
        verify(context, times(0)).drawImage(anyObject(), anyDouble(), anyDouble());


        // Create new manager to test some real drawing.
        manager = new TileGridManager();
        manager.setTexture(Tile.DIRT, null); // Add an entry to avoid NPE.

        // Create chunk of solid dirt to test drawing.
        TileChunk chunk = new TileChunk(Tile.DIRT);

        // Set light levels, so textures and shadows are drawn.
        for (int x = 0; x < TileGridManager.CHUNK_GRID_SIZE; x++) {
            for (int y = 0; y < TileGridManager.CHUNK_GRID_SIZE; y++) {
                chunk.setLightLevel(x, y, 0.5);
            }
        }

        manager.addChunk(0, 0, chunk);

        reset(context); // Reset mocked state, keep counts accurate.

        manager.render(context, 0, 0, 800, 600);

        int tilesPerChunk = TileGridManager.CHUNK_GRID_SIZE * TileGridManager.CHUNK_GRID_SIZE;
        verify(context, times(tilesPerChunk)).drawImage(anyObject(), anyDouble(), anyDouble());
    }

    @Test
    public void testGetLoadedChunkCount() {
        assertEquals("Chunk count != 1 after insertion", 1, manager.loadedChunkCount());

        TileChunk chunk2 = new TileChunk();
        manager.addChunk(1, 0, chunk2);
        assertEquals("Chunk count != 2 after insertion", 2, manager.loadedChunkCount());

        TileChunk chunk3 = new TileChunk();
        manager.addChunk(2, 0, chunk3);
        assertEquals("Chunk count != 3 after insertion", 3, manager.loadedChunkCount());
    }

    @Test
    public void testGetLoadedChunkCountWithRemoval() {
        testGetLoadedChunkCount(); // Load three chunks.

        manager.unloadChunk(2, 0);
        assertEquals("Chunk count != 2 after removal", 2, manager.loadedChunkCount());

        manager.unloadChunk(1, 0);
        assertEquals("Chunk count != 1 after removal", 1, manager.loadedChunkCount());

    }

    /**
     * Test the entity overlapping (collision) checking method for the
     * TileGridManager
     */
    @Test
    public void testDoesEntityOverlapWithTileType() {
        TileChunk chunk = new TileChunk();
        WorldEntity entity = new WorldEntity(35, 35, 100, 100);
        manager.addChunk(0, 0, chunk);
        // Test case of no tiles around entity
        assertFalse(manager.doesEntityOverlapSolidTile(entity));
        // Test case of non-overlapping tiles near entity
        manager.setTileAtLocation(1, 4, Tile.DIRT);
        manager.setTileAtLocation(9, 4, Tile.GRASS);
        manager.setTileAtLocation(4, 1, Tile.STONE);
        manager.setTileAtLocation(4, 9, Tile.GRASS);
        assertFalse(manager.doesEntityOverlapSolidTile(entity));
        assertTrue(manager.shouldEntityCollideAtPos(entity, 0, 0));
        manager.setTileAtLocation(1, 4, Tile.AIR);
        manager.setTileAtLocation(9, 4, Tile.AIR);
        manager.setTileAtLocation(4, 1, Tile.AIR);
        manager.setTileAtLocation(4, 9, Tile.GRASS);
        // Test case of tile overlapping entirely within entity
        manager.setTileAtLocation(4, 4, Tile.STONE);
        assertTrue(manager.doesEntityOverlapSolidTile(entity));
        manager.setTileAtLocation(4, 4, Tile.AIR);
        // Test case of tile overlapping on horizontal left edge
        manager.setTileAtLocation(2, 3, Tile.DIRT);
        assertTrue(manager.doesEntityOverlapSolidTile(entity));
        manager.setTileAtLocation(2, 3, Tile.AIR);
        // Test case of tile overlapping on horizontal right edge
        manager.setTileAtLocation(8, 3, Tile.DIRT);
        assertTrue(manager.doesEntityOverlapSolidTile(entity));
        manager.setTileAtLocation(8, 3, Tile.AIR);
        // Test case of tile overlapping on vertical top edge
        manager.setTileAtLocation(5, 2, Tile.DIRT);
        assertTrue(manager.doesEntityOverlapSolidTile(entity));
        manager.setTileAtLocation(5, 2, Tile.AIR);
        // Test case of tile overlapping on vertical bottom edge
        manager.setTileAtLocation(5, 8, Tile.DIRT);
        assertTrue(manager.doesEntityOverlapSolidTile(entity));
        manager.setTileAtLocation(5, 8, Tile.AIR);
        manager.unloadChunk(0, 0);
    }

    /**
     * Test the generation of a chunk (needs more testing pending generator
     * implementation!)
     */
    @Test
    public void testGenerateChunk() {
        manager.generateChunk(0, 0);
    }
    /**
     * Tests if grass effects are removed once a grass block is removed.
     */
    @Test
    public void testRemoveGrassEffects(){
    	manager.setTileAtLocation(1, 2, Tile.GRASS);
    	manager.setTileAtLocation(1, 1, Tile.BLADES);
    	manager.tileMined = true;
    	manager.setTileAtLocation(1, 2, Tile.AIR);
    	assertEquals(manager.getTileAtLocation(1, 1), Tile.AIR);
    }

}
