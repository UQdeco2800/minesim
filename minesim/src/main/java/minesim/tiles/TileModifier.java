package minesim.tiles;

/**
 * Interface that can be used by the World and it's TileGridManager to modify chunks
 * as part of a world generation pipeline.
 *
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public interface TileModifier {

    /**
     * Given an existing chunk, and coordinates, modifies the chunk,
     * and returns the modified chunk
     *
     * @param chunkX the x coordinate used in generation
     * @param chunkY the y coordinate used in generation
     * @param chunk the chunk to modify
     * @return the modified chunk
     */
    TileChunk modifyChunk(int chunkX, int chunkY, TileChunk chunk);

}
