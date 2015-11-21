package minesim.tiles;

import java.util.Random;

import minesim.tiles.TileGridManager;

/**
 * Basic generator implementing the generator interface. Basic grass covered hills in a sine wave
 * pattern are created, with just solid stone appearing a certain depth under the ground.
 *
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public class RollingHillsGenerator implements TileGenerator {

    private int GROUND_LEVEL = 25;
    private int STONE_LEVEL = 32;
    private double HILL_WIDTH = 50.0;
    private double HILL_HEIGHT = 5;
    private double DIAMOND_VEIN_CHANCE = 0.25;
    

    /**
     * Generate a chunk for the location in the world specified. This generator creates chunks that
     * appear as grass covered hills with a stone layer occurring below. It is intended to act as an
     * example Note that Chunk coordinates are used, not tile coordinates.
     *
     * @param chunkX Horizontal *CHUNK* coordinate of the chunk to be generated.
     * @param chunkY Vertical *CHUNK* coordinate of the chunk to be generated.
     * @return TileChunk filled with tiles for their location in the world.
     */
    public TileChunk generateChunk(int chunkX, int chunkY) {
        TileChunk new_chunk = new TileChunk(); // New Chunk to be filled and returned.
        

        for (int x = 0; x < TileGridManager.CHUNK_GRID_SIZE; x++) {
            int x_pos = chunkX * TileGridManager.CHUNK_GRID_SIZE + x;

            // Calculate the ground level, based on a sine wave.
            int groundHeight = (int) Math.round((HILL_HEIGHT) * Math.sin(Math.PI * x_pos / HILL_WIDTH)) + GROUND_LEVEL;

            
            for (int y = 0; y < TileGridManager.CHUNK_GRID_SIZE; y++) {
                // Calculate the total offset from the top of the world (larger Y means lower down).
                int y_pos = chunkY * TileGridManager.CHUNK_GRID_SIZE + y;        		

                
                // Set the tile based on vertical height.
            	if (y_pos < groundHeight) {
                    new_chunk.setTileType(x, y, Tile.AIR);
            	} else if (y_pos == groundHeight) {
                    new_chunk.setTileType(x, y, Tile.GRASS);
                } else if (y_pos < STONE_LEVEL && y_pos > groundHeight) {
                    new_chunk.setTileType(x, y, Tile.DIRT);
                } else {
                    new_chunk.setTileType(x, y, Tile.STONE);
                }
            	
            	//add surface effects
            	double grassChance =  Math.random();
            	if (y_pos == groundHeight - 1){
             		if (grassChance < 0.5){
             			new_chunk.setTileType(x, y, Tile.BLADES);
             		} else if(grassChance > 0.9){
             			new_chunk.setTileType(x, y, Tile.FLOWER);
             		} else{
             			new_chunk.setTileType(x, y, Tile.THICKET);
             		}
            	}
            }
        }   

        // Randomly add in diamond veins below stone level
        if (chunkY * TileGridManager.CHUNK_GRID_SIZE > STONE_LEVEL) {
            Random r = new Random();
            if (((r.nextInt() % 100) / 100) < DIAMOND_VEIN_CHANCE) {
                int diamondXCentre = r.nextInt() % TileGridManager.CHUNK_GRID_SIZE;
                int diamondYCentre = r.nextInt() % TileGridManager.CHUNK_GRID_SIZE;
                int diamondRadius = r.nextInt() % 3 + 1;

                for (int x = Math.max(0, diamondXCentre - diamondRadius);
                     x < Math.min(TileGridManager.CHUNK_GRID_SIZE, diamondXCentre + diamondRadius); ++x) {
                    for (int y = Math.max(0, diamondYCentre - diamondRadius);
                         y < Math.min(TileGridManager.CHUNK_GRID_SIZE, diamondYCentre + diamondRadius); ++y) {
                        new_chunk.setTileType(x, y, Tile.DIAMOND);
                    }
                }
            }
        }
        return new_chunk;
    }
}
