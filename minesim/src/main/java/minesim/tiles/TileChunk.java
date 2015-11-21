package minesim.tiles;

import java.util.Arrays;


/**
 * Class storing a small grid of tiles (16*16). TileChunks are the logical units that are loaded and
 * stored to a database, and that are simulated and rendered in the game.
 *
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public class TileChunk {

    private byte[][] tiles;
    private double[][] lightLevels;

    /**
     * Tileset constructor, creates an empty (all air) chunk
     */
    public TileChunk() {
        this(Tile.AIR);
    }

    /**
     * Constructor from byte data. Useful to restore chunks from database storage.
     *
     * @param data a 1-dimensional array of bytes to create a chunk from.
     */
    public TileChunk(byte[] data) {
        int grid_width = TileGridManager.CHUNK_GRID_SIZE;
        int num_tiles = grid_width * grid_width;

        // Check the serialised data fits in a chunk.
        if (data.length != num_tiles) {
            String err_message = "Incorrect size of serialised chunk data. Should be %d bytes.";
            throw new TileException(String.format(err_message, num_tiles));
        }

        // Allocate and populate from data.
        tiles = new byte[grid_width][grid_width];
        for (int i = 0; i < num_tiles; i++) {
            tiles[i / grid_width][i % grid_width] = data[i];
        }
    }


    /**
     * Construct a TileChunk consisting of entirely one type of tile.
     *
     * @param tile the type of tile to create this chunk from.
     */
    public TileChunk(Tile tile) {
        tiles = new byte[TileGridManager.CHUNK_GRID_SIZE][TileGridManager.CHUNK_GRID_SIZE];
        lightLevels = new double[TileGridManager.CHUNK_GRID_SIZE][TileGridManager.CHUNK_GRID_SIZE];

        // Fill with just air.
        for (byte[] column : tiles) {
            Arrays.fill(column, tile.getId());
        }

        // Set light level to black.
        unlight();
    }


    /**
     * Convert a chunks data into a 1-dimensional array of bytes.
     *
     * @return a byte array representing the chunks serialised data.
     */
    public byte[] toBytes() {
        int grid_width = TileGridManager.CHUNK_GRID_SIZE;
        int num_tiles = grid_width * grid_width;
        byte[] result = new byte[num_tiles];

        for (int i = 0; i < num_tiles; i++) {
            result[i] = tiles[i / grid_width][i % grid_width];
        }
        return result;
    }


    /**
     * Get the tile type of a given coordinate within the chunk
     *
     * @param tileX the relative x coordinate of the tile
     * @param tileY the relative y coordinate of the tile
     * @return the tile type id of the given tile
     */
    public Tile getTileType(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX >= tiles.length || tileY >= tiles[0].length) {
            throw new TileNotFoundException();
        }
        return Tile.getById(tiles[tileX][tileY]);
    }

    /**
     * Sets the given tile coordinate to a tile type
     *
     * @param tileX the relative x coordinate of the tile
     * @param tileY the relative y coordinate of the tile
     * @param type  The type of the tile
     * @throws TileNotFoundException if the coordinate is not within the chunk
     */
    public void setTileType(int tileX, int tileY, Tile type) throws TileNotFoundException {
        if (tileX < 0 || tileY < 0 || tileX >= tiles.length || tileY >= tiles[0].length) {
            throw new TileNotFoundException();
        }
        tiles[tileX][tileY] = type.getId();
    }

    /**
     * Lookup the light-level at a particular tile.
     *
     * Note: Light values are technically stored as alpha values of an
     * overlayed shadow rectangle; Therefore 1 is black, 0 is fully lit.
     *
     * @param tileX Horizontal position of the tile *within this chunk** (0-15).
     * @param tileY Vertical position of the tile *within this chunk** (0-15).
     * @return Value between 0 and 1, representing the light level of the tile.
     */
    public double getLightLevel(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX >= tiles.length || tileY >= tiles[0].length) {
            throw new TileNotFoundException();
        }
        return lightLevels[tileX][tileY];
    }

    /**
     * Set the light-level at a particular tile between 0 and 127.
     *
     * Note: Light values are technically stored as alpha values of an
     * overlayed shadow rectangle; Therefore 1 is black, 0 is fully lit.
     *
     * @param tileX Horizontal position of the tile *within this chunk** (0-15).
     * @param tileY Vertical position of the tile *within this chunk** (0-15).
     * @param lightLevel double between 0 and 1 to set the light too.
     */
    public void setLightLevel(int tileX, int tileY, double lightLevel) {
        if (tileX < 0 || tileY < 0 || tileX >= tiles.length || tileY >= tiles[0].length) {
            throw new TileNotFoundException();
        }

        lightLevels[tileX][tileY] = lightLevel;
    }

    /**
     * Set this chunk fully dark. (all light levels to 1).

     * Note: Light values are technically stored as alpha values of an
     * overlayed shadow rectangle; Therefore 1 is black, 0 is fully lit.
     */
    public void unlight() {
        // Set chunk light levels completely dark.
        for (double[] column : lightLevels) {
            Arrays.fill(column, (double)1);
        }
    }

    /**
     * Hash function for chunks, based on the tile grid.
     * Chunks with the same tiles in the same place will have the same hash.
     *
     * @return unique integer per chunk.
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }

    /**
     * Equality function for comparing Chunks, equality is determined by two chunks having the same
     * composition of tiles.
     *
     * @param obj object (chunk) to be compared to.
     * @return if the chunks are the same.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof TileChunk && Arrays.equals(toBytes(), ((TileChunk) obj).toBytes());

    }
}
