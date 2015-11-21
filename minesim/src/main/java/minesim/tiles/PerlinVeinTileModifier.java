package minesim.tiles;

// @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
public class PerlinVeinTileModifier implements TileModifier {

    private int STONE_LEVEL = 32;
    private double RUBY_THRESHOLD = 0.5;
    private double DIAMOND_THRESHOLD = -0.5;
    private int noiseGridSize = 100;

    NoiseCreator noiseCreator;

    public PerlinVeinTileModifier() {
        noiseCreator = new NoiseCreator(noiseGridSize, noiseGridSize);
    }

    public TileChunk modifyChunk(int chunkX, int chunkY, TileChunk chunk) {

        if (chunkY * TileGridManager.CHUNK_GRID_SIZE >= STONE_LEVEL) {
            for (int x = 0; x < TileGridManager.CHUNK_GRID_SIZE; x++) {
                int globalX = TileGridManager.CHUNK_GRID_SIZE * chunkX + x;
                for (int y = 0; y < TileGridManager.CHUNK_GRID_SIZE; y++) {
                    int globalY = TileGridManager.CHUNK_GRID_SIZE * chunkY + y;
                    double pX, pY;
                    pX = (((double)globalX / 2000.0) * noiseGridSize) - noiseGridSize/2;
                    pY = (((double)globalY / 2000.0) * noiseGridSize) - noiseGridSize/2;
                    double perlin = noiseCreator.getPerlin(pX, pY);
                    if (perlin >= RUBY_THRESHOLD) {
                        chunk.setTileType(x, y, Tile.RUBY);
                    } else if (perlin <= DIAMOND_THRESHOLD) {
                        chunk.setTileType(x, y, Tile.GOLD);
                    }
                }
            }
        }

        return chunk;
    }

}
