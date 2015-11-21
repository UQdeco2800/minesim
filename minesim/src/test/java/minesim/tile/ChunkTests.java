package minesim.tile;

import org.junit.Ignore;
import org.junit.Test;

import minesim.tiles.Tile;
import minesim.tiles.TileChunk;
import minesim.tiles.TileGridManager;

import static org.junit.Assert.*;

public class ChunkTests {

    @Test
    public void testChunkConstructor() {
        TileChunk chunk = new TileChunk();
    }

    /**
     * Verify that a tile retrieved from a chunk by it's coordinates returns
     * correctly.
     */
    @Test
    public void testGetAndSetTileAtLocation() {
        TileChunk chunk = new TileChunk();

        // Set a tile in the middle to grass.
        chunk.setTileType(7, 7, Tile.GRASS);

        // Retrieve the tile that was just set, and an empty air tile.
        Tile grasschecktile = chunk.getTileType(7, 7);
        Tile airchecktile = chunk.getTileType(0, 0);

        // Check the retrived tile matches.
        assertEquals("Retrieved tile doesn't match.", Tile.GRASS, grasschecktile);
        // Check the other tile is Air (as per default).
        assertEquals("Retrieved tile doesn't match.", Tile.AIR, airchecktile);
    }

    @Test
    public void testNewChunkLightLevels() {
        TileChunk chunk = new TileChunk(Tile.DIRT);

        for (int x = 0; x < TileGridManager.CHUNK_GRID_SIZE; x++) {
            for (int y = 0; y < TileGridManager.CHUNK_GRID_SIZE; y++) {
                double lightLevel = chunk.getLightLevel(x, y);
                assertEquals("Retrieved Light value isn't 1", 1, lightLevel, 1e-5);
            }
        }
    }

    @Test
    public void testSetLightLevels() {
        TileChunk chunk = new TileChunk(Tile.DIRT);

        // Set some light levels.
        chunk.setLightLevel(7, 7, 0.07);
        chunk.setLightLevel(9, 9, 0.9);

        // Retrieve light levels at those locations.
        double checkLevel07 = chunk.getLightLevel(7, 7);
        double checkLevel9 = chunk.getLightLevel(9, 9);

        assertEquals("Retrieved light level not what was set (100)", checkLevel07, 0.07, 1e-5);
        assertEquals("Retrieved light level not what was set (9)", checkLevel9, 0.9, 1e-5);
    }

    @Test
    public void testChunkEquality() {
        TileChunk chunk1 = new TileChunk();
        TileChunk chunk2 = new TileChunk();

        assertTrue("Chunks should be identical", chunk1.equals(chunk2));
        assertTrue("Chunks should be identical", chunk2.equals(chunk1));

        chunk2.setTileType(5, 6, Tile.DIRT);
        assertFalse("Chunks should not be identical", chunk1.equals(chunk2));
        assertFalse("Chunks should not be identical", chunk2.equals(chunk1));

        chunk1.setTileType(5, 6, Tile.DIRT);
        assertTrue("Chunks should be identical", chunk1.equals(chunk2));
        assertTrue("Chunks should be identical", chunk2.equals(chunk1));

        chunk1.setTileType(5, 6, Tile.STONE);
        assertFalse("Chunks should not be identical", chunk1.equals(chunk2));
        assertFalse("Chunks should not be identical", chunk2.equals(chunk1));

    }

    /**
     * Verify that the string serialisation of a chunk can be generated/read
     * successfully
     */
    @Test
    public void testChunkToBytes() {
        TileChunk chunk = new TileChunk();

        // Set some tiles, in a pattern to avoid symmetry.
        chunk.setTileType(0, 0, Tile.STONE);
        chunk.setTileType(2, 1, Tile.GRASS);
        chunk.setTileType(4, 2, Tile.DIRT);

        int grid_width = TileGridManager.CHUNK_GRID_SIZE;
        int num_tiles = grid_width * grid_width;

        // Define the expected output.
        byte[] expected_data = new byte[num_tiles];
        expected_data[0] = Tile.STONE.getId();
        expected_data[grid_width * 2 + 1] = Tile.GRASS.getId();
        expected_data[grid_width * 4 + 2] = Tile.DIRT.getId();

        // Retrieve and compare the chunk data.
        assertArrayEquals("Chunk serialisation failed", expected_data, chunk.toBytes());
    }

    @Test
    public void testChunkHashCode() {
        TileChunk chunk1 = new TileChunk();
        chunk1.setTileType(0, 0, Tile.STONE);
        chunk1.setTileType(2, 1, Tile.GRASS);
        chunk1.setTileType(4, 2, Tile.DIRT);

        TileChunk chunk2 = new TileChunk();
        chunk2.setTileType(0, 0, Tile.STONE);
        chunk2.setTileType(2, 1, Tile.GRASS);

        assertNotEquals(chunk1.hashCode(), chunk2.hashCode());
        chunk2.setTileType(4, 2, Tile.DIRT);
        assertEquals(chunk1.hashCode(), chunk2.hashCode());
    }
}
