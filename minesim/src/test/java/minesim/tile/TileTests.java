package minesim.tile;

import org.junit.Test;

import minesim.tiles.Tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TileTests {

    @Test
    public void testTileInitialise() {
        Tile tile = Tile.AIR;
        assertEquals("Air tile not returned from id=0 lookup", (byte) 0, tile.getId());
        assertEquals("Human readable Tile title incorrect", "Air", tile.getTitle());
    }

    @Test
    public void testTileGetById() {
        Tile tile = Tile.getById((byte) 0);
        assertEquals("Air tile not returned from id=0 lookup", Tile.AIR, tile);
    }

    @Test
    public void testTileGetByIdNull() {
        Tile tile = Tile.getById((byte) -1);
        assertNull("Returned Tile from bad ID not null", tile);
    }

    @Test
    public void testTileGetTexture() {
        Tile tile = Tile.DIRT;
        assertEquals("Texture url incorrect", "texturepack/dirt.png", tile.getTextureUrl());
    }
}
