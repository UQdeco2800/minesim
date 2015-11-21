package minesim.tiles;

import java.util.Random;

/**
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public class BasicCaveTileModifier implements TileModifier {

    private int STONE_LEVEL = 32;
    private int WALL_PERCENTAGE = 50;
    private int ITERATIONS = 3;
    private int MAX_NUM_CAVES = 10;
    private int MIN_CAVE_DIM = 5;
    private int MAX_CAVE_DIM = 80;

    private Random random;
    private boolean[][] grid;

    /**
     * Create a new BasicCaveTileModifier
     *
     * @param gridWidth the width of the world
     * @param gridHeight the height of the world
     */
    public BasicCaveTileModifier(int gridWidth, int gridHeight) {
        random = new Random();
        grid = new boolean[gridWidth][gridHeight];
        fillGrid();
        generateCaves(random.nextInt(MAX_NUM_CAVES) + 1);
    }

    /**
     * Fill the cave grid with solid walls
     */
    public void fillGrid() {
        for (int x = 0; x < grid.length; ++x) {
            for (int y = 0; y < grid[x].length; ++y) {
                grid[x][y] = true;
            }
        }
    }

    /**
     * Generates a fixed number of caves at random locations, with random sizes
     *
     * @param caveNum the number of caves to generate
     */
    public void generateCaves(int caveNum) {
        for (int i = 0; i < caveNum; ++i) {
            // Get cave boundaries
            int caveWidth = random.nextInt(MAX_CAVE_DIM - MIN_CAVE_DIM) + MIN_CAVE_DIM;
            int caveHeight = random.nextInt(MAX_CAVE_DIM - MIN_CAVE_DIM) + MIN_CAVE_DIM;
            int xMin =  Math.min(grid.length - caveWidth - 1, random.nextInt(grid.length));
            int yMin =  Math.min(grid[xMin].length - caveHeight - 1, random.nextInt(grid[xMin].length));
            generateRandomArea(xMin, yMin, xMin + caveWidth, yMin + caveHeight);
        }

        for (int i = 0; i < ITERATIONS; ++i) {
            applyCellularAutomataStep();
        }
    }

    public void generateRandomArea(int xMin, int Ymin, int xMax, int yMax) {
        for (int x = xMin; x < xMax; ++x) {
            for (int y = Ymin; y < yMax; ++y) {
                grid[x][y] = (random.nextInt(100) < WALL_PERCENTAGE);
            }
        }
    }

    /**
     * Get the number of neighbouring cells that are solid
     *
     * @param x the x position to check
     * @param y the y position to check
     * @return the number of cells immediately adjacent which are walls
     */
    public int getNeighbourWallCount(int x, int y) {
        int count = 0;
        if (x > 0) {
            if (y > 0) {
                if (grid[x-1][y-1]) count++;
            }
            if (grid[x-1][y]) count++;
            if (y < grid[x-1].length - 1) {
                if (grid[x-1][y+1]) count++;
            }
        }
        if (y > 0) {
            if (grid[x][y-1]) count++;
        }
        if (y < grid[x].length - 1) {
            if (grid[x][y+1]) count++;
        }
        if (x < grid.length - 1) {
            if (y > 0) {
                if (grid[x+1][y-1]) count++;
            }
            if (grid[x+1][y]) count++;
            if (y < grid[x+1].length - 1) {
                if (grid[x+1][y+1]) count++;
            }
        }

        return count;
    }

    /**
     * Apply one iteration of the 4-5 rule.
     *
     * Any tile which is solid, and has less than 4 neighbours which are solid, becomes air.
     * Any tile which is air, and has 5 or more neighbours which are solid, becomes solid.
     */
    private void applyCellularAutomataStep() {
        int wallCount;

        for (int x = 0; x < grid.length; ++x) {
            for (int y = 0; y < grid[x].length; ++y) {
                wallCount = getNeighbourWallCount(x, y);
                if (grid[x][y]) {
                    grid[x][y] = (wallCount >= 4);
                } else {
                    grid[x][y] = (wallCount >= 5);
                }
            }
        }
    }

    /**
     * Modify a chunk to add in generated caves
     *
     * @param chunkX the x coordinate used in generation
     * @param chunkY the y coordinate used in generation
     * @param chunk the chunk to modify
     * @return the chunk, modified
     */
    public TileChunk modifyChunk(int chunkX, int chunkY, TileChunk chunk) {
        if (chunkY * TileGridManager.CHUNK_GRID_SIZE >= STONE_LEVEL) {
            for (int x = 0; x < TileGridManager.CHUNK_GRID_SIZE; x++) {
                int globalX = TileGridManager.CHUNK_GRID_SIZE * chunkX + x;
                for (int y = 0; y < TileGridManager.CHUNK_GRID_SIZE; y++) {
                    int globalY = TileGridManager.CHUNK_GRID_SIZE * chunkY + y;
                    if (!grid[globalX][globalY]) {
                        chunk.setTileType(x, y, Tile.AIR);
                    }
                }
            }
        }

        return chunk;
    }

}
