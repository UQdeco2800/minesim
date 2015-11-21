package minesim.tile;

import org.junit.Before;
import org.junit.Test;

import minesim.tiles.RollingHillsGenerator;
import minesim.tiles.TileChunk;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicGeneratorTests {

    RollingHillsGenerator basicGen;

    @Before
    public void setUp() {
        basicGen = new RollingHillsGenerator();
    }

    @Test
    public void testBasicGenerator() {
        TileChunk airChunk = basicGen.generateChunk(0, 0);
        assertTrue("Chunk should all be air", airChunk.equals(new TileChunk()));

        TileChunk groundChunk = basicGen.generateChunk(0, 2);
        assertFalse("Chunk should not be air", airChunk.equals(groundChunk));

        TileChunk deepChunk = basicGen.generateChunk(0, 10);
        //assertTrue("Chunk should all be stone", deepChunk.equals(new TileChunk(Tile.STONE)));
    }
}
