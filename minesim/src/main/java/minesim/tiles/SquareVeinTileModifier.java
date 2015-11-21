package minesim.tiles;

import java.util.Random;

// @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
public class SquareVeinTileModifier implements TileModifier {

    private int STONE_LEVEL = 32;
    private double VEIN_CHANCE = 0.25;

    public TileChunk modifyChunk(int chunkX, int chunkY, TileChunk chunk) {

        Random r = new Random();

        if (chunkY * TileGridManager.CHUNK_GRID_SIZE >= STONE_LEVEL) {
            if (r.nextInt((int)(1/VEIN_CHANCE)) == 1) {
                int veinX = r.nextInt(TileGridManager.CHUNK_GRID_SIZE);
                int veinY = r.nextInt(TileGridManager.CHUNK_GRID_SIZE);
                int veinRadius = r.nextInt(5);

                for (int x = veinX - veinRadius; x < veinX + veinRadius; x++) {
                    if (x >= 0 && x < TileGridManager.CHUNK_GRID_SIZE) {
                        for (int y = veinY - veinRadius; y < veinY + veinRadius; y++) {
                            if (y >= 0 && y < TileGridManager.CHUNK_GRID_SIZE) {
                                chunk.setTileType(x, y, Tile.EMERALD);
                            }
                        }
                    }
                }
            }
        }

        return chunk;
    }

}
