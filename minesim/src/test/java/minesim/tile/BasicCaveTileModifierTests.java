package minesim.tile;

import minesim.tiles.BasicCaveTileModifier;
import minesim.tiles.Tile;
import minesim.tiles.TileChunk;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public class BasicCaveTileModifierTests {

    /**
     * Test the constructor and basic tile modification for one chunk
     */
    @Test
    public void testBasicCaveTileModifierConstructor() {
        BasicCaveTileModifier m = new BasicCaveTileModifier(2000, 2000);
        TileChunk chunk = new TileChunk(Tile.AIR);
        m.modifyChunk(0, 6, chunk);
    }

    /**
     * Tests the cellular automation method for checking a tile's neighbours
     */
    @Test
    public void testGetNeighbourWallCount() {
        BasicCaveTileModifier m = new BasicCaveTileModifier(2000, 2000);
        m.fillGrid();
        assertEquals(8, m.getNeighbourWallCount(5,5));
    }

}
