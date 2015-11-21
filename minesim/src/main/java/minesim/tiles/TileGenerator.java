package minesim.tiles;

/**
 * Interface that can be used by the World and it's TileGridManager to generate new chunks.
 *
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public interface TileGenerator {

    /**
     * Generate a single chunk, for a position in world as specified.
     *
     * @param chunkX the x coordinate used in generation
     * @param chunkY the y coordinate used in generation
     * @return the TileChunk containing gneerated tile data
     */
    TileChunk generateChunk(int chunkX, int chunkY);

}
