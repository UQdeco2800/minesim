package minesim.tiles;

import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import minesim.World;
import minesim.entities.WorldEntity;

/**
 * Container and manager for storing, modifying, and interacting with the world's tiles.
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public class TileGridManager {

    /**
     * The number of pixels a single tile occupies.
     */
    public static final int TILE_SIZE = 16;
    /**
     * The number of tiles a chunk stores horizontally and vertically.
     */
    public static final int CHUNK_GRID_SIZE = 16;
    public static final int UNDERGROUND_LEVEL = 2 * TILE_SIZE * CHUNK_GRID_SIZE;
    private static final int LIGHTING_DISTANCE = 4;
    private static final double LIGHTING_RADIUS = Math.sqrt(2*Math.pow(LIGHTING_DISTANCE, 2));
    private static Logger LOGGER = Logger.getLogger(TileGridManager.class);
    private HashMap<Integer, HashMap<Integer, TileChunk>> loadedChunks;
    private HashMap<Tile, Image> textureRegistry;
    private Image caveBackground;

    private TileGenerator generator;
    private ArrayList<TileModifier> modifiers;
    public boolean tileMined = false;

    /**
     * Construct an instance of a TileGridManager
     *
     * @param generator the world generation engine to use
     */
    public TileGridManager(TileGenerator generator) {
        loadedChunks = new HashMap<>();
        textureRegistry = new HashMap<>();
        this.generator = generator;
        this.modifiers = new ArrayList<>();
    }

    public TileGridManager() {
        this(null);
    }

    /**
     * Accessor for setting the texture for a tle type.
     *
     * @param tile    the tile for which the texture is being set.
     * @param texture the loaded fx image to be used for the tile type.
     */
    public void setTexture(Tile tile, Image texture) {
        textureRegistry.put(tile, texture);
    }

    /**
     * Iterate known tiles and load the images from disk as textures into the internal texture
     * registry for use in rendering.
     */
    public void loadTextures() {
        caveBackground = new Image("texturepack/cave-background.jpg");
        // Load texture for each tile.
        for (Tile tile : Tile.values()) {
            if (tile.getTextureUrl() != null)
                setTexture(tile, new Image(tile.getTextureUrl()));
            else
                setTexture(tile, null);
        }
        LOGGER.info("Loaded " + textureRegistry.size() + " textures");
    }

    /**
     * Determine if a chunk at a particular location is in the collection of loaded chunks.
     *
     * Note: parameters chunkX and chunkY are *Chunk* coordinates, not global tile coordinates.
     * Chunk coordinates specify the position of non-overlapping chunks in the world, which contain
     * CHUNK_GRID_SIZE tiles inside  of them. Therefore, Chunk coordinates are 1/CHUNK_GRID_SIZE
     * (currently 1/16th) of tile coordinates.
     *
     * @param chunkX The horizontal chunk coordinate (1/16 global tile position).
     * @param chunkY The vertical chunk coordinate (1/16 global tile position).
     * @return There is a chunk loaded at the chunk coordinates by the TileGridManager.
     */
    public boolean chunkIsLoaded(int chunkX, int chunkY) {
        return loadedChunks.containsKey(chunkX)
                && loadedChunks.get(chunkX).containsKey(chunkY);
    }

    /**
     * Verify if the location of a tile is loaded.
     * @param x Vertical position of the tile in the world.
     * @param y Horizontal position of the tile in the world.
     * @return boolean representing if a tile in a location is in a loaded chunk.
     */
    private boolean tileIsLoaded(int x, int y) {
        int xChunk = x / CHUNK_GRID_SIZE;
        int yChunk = y / CHUNK_GRID_SIZE;
        int xTile = x % CHUNK_GRID_SIZE;
        int yTile = y % CHUNK_GRID_SIZE;

        try {
            getTileAtLocation(x, y);
            return true;
        }
        catch (TileNotFoundException TnfError) {
            return false;
        }
    }

    /**
     * Add a Chunk to the collection of loaded chunks. Chunks will override other chunks with the
     * same position.
     *
     * Note: parameters chunkX and chunkY are *Chunk* coordinates, not global tile coordinates.
     * Chunk coordinates specify the position of non-overlapping chunks in the world, which contain
     * CHUNK_GRID_SIZE tiles inside of them. Therefore, Chunk coordinates are 1/CHUNK_GRID_SIZE
     * (currently 1/16th) of tile coordinates.
     *
     * @param chunkX    The horizontal Chunk coordinate for the chunk to be added at.
     * @param chunkY    The vertical Chunk coordinate for the chunk to be added at.
     * @param tileChunk Chunk to be added to the list of loaded chunks.
     */
    public void addChunk(int chunkX, int chunkY, TileChunk tileChunk) {
        // Initialise column if it doesn't exist.
        if (!loadedChunks.containsKey(chunkX)) {
            loadedChunks.put(chunkX, new HashMap<>());
        }

        // Insert the new chunk.
        loadedChunks.get(chunkX).put(chunkY, tileChunk);
    }

    /**
     * Returns the chunk object at a given location
     *
     * @param chunkX The horizontal Chunk coordinate to get
     * @param chunkY The vertical Chunk coordinate to get
     * @return the chunk at these coordinates
     * @throws TileNotFoundException if the chunk coordinates are invalid
     */
    private TileChunk getChunk(int chunkX, int chunkY) {
        if (loadedChunks.get(chunkX).get(chunkY) == null) {
            throw new TileNotFoundException("Chunk could not be retrieved");
        }
        return loadedChunks.get(chunkX).get(chunkY);
    }

    /**
     * Remove a chunk from the collection of loaded chunks.
     *
     * Note: parameters chunkX and chunkY are *Chunk* coordinates, not global tile coordinates.
     * Chunk coordinates specify the position of non-overlapping chunks in the world, which contain
     * CHUNK_GRID_SIZE tiles inside of them. Therefore, Chunk coordinates are 1/CHUNK_GRID_SIZE
     * (currently 1/16th) of tile coordinates.
     *
     * @param chunkX The horizontal chunk coordinate (1/16 global tile position).
     * @param chunkY The vertical chunk coordinate (1/16 global tile position).
     */
    public void unloadChunk(int chunkX, int chunkY) {
        // Check the column, and then the chunk exist.
        if (loadedChunks.containsKey(chunkX)
                && loadedChunks.get(chunkX).containsKey(chunkY)) {
            // Remove the specified chunk.
            loadedChunks.get(chunkX).remove(chunkY);

            // Remove column too if no chunks remain in it.
            if (loadedChunks.get(chunkX).isEmpty()) {
                loadedChunks.remove(chunkX);
            }
        } else { // The chunk wasn't found.
            throw new TileNotFoundException();
        }
    }

    /**
     * Count the number of chunks this manager currently has loaded.
     *
     * @return The number of chunks loaded.
     */
    public int loadedChunkCount() {
        int count = 0;
        for (HashMap column_map : loadedChunks.values()) {
            count += column_map.size();
        }
        return count;
    }

    /**
     * Get the tile type at a particular location from tile coordinates.
     *
     * @param x Vertical position of the tile in the world.
     * @param y Horizontal position of the tile in the world.
     * @return The Tile type occupying the position in the world.
     */
    public Tile getTileAtLocation(int x, int y) {
        // Calculate the chunk and inner-tile coordinates.
        int xchunk = x / CHUNK_GRID_SIZE;
        int ychunk = y / CHUNK_GRID_SIZE;
        int xtile = x % CHUNK_GRID_SIZE;
        int ytile = y % CHUNK_GRID_SIZE;

        // Check the column the chunk is in exists.
        if (!loadedChunks.containsKey(xchunk)) {
            throw new TileNotFoundException();
        }
        // Retrieve the chunk.
        TileChunk chunk = loadedChunks.get(xchunk).get(ychunk);

        if (chunk == null) {
            throw new TileNotFoundException();
        }
        return chunk.getTileType(xtile, ytile);
    }

    /**
     * Set the tile at a location to another.
     *
     * @param x    Vertical position of the tile in the world.
     * @param y    Horizontal position of the tile in the world.
     * @param tile The tile type to set the tile to
     */
    public void setTileAtLocation(int x, int y, Tile tile) {
        // Calculate the chunk and inner-tile coordinates.
        int xChunk = x / CHUNK_GRID_SIZE;
        int yChunk = y / CHUNK_GRID_SIZE;
        int xTile = x % CHUNK_GRID_SIZE;
        int yTile = y % CHUNK_GRID_SIZE;

        // Retrieve the chunk, and check it exists.
        if (!loadedChunks.containsKey(xChunk)) {
            throw new TileNotFoundException();
        }
        TileChunk chunk = loadedChunks.get(xChunk).get(yChunk);
        if (chunk == null) {
            throw new TileNotFoundException();
        }

        Tile currentTile = chunk.getTileType(xTile, yTile);
        chunk.setTileType(xTile, yTile, tile);
        // Update and log the tile update.
        LOGGER.info(String.format("Changed tile at (%d, %d) from %s to %s",
                xTile, yTile, currentTile.getTitle(), tile.getTitle()));

        //Removes grass effects if grass is removed
        if (tileMined){
        	chunk.setTileType(xTile, yTile - 1, Tile.AIR);
        }

        // Relight if light emitting tile added.
        if (currentTile.isSolid() && !tile.isSolid()) {
            lightAroundTile(x, y);
        }
        // Relight world if new tiles added.
        else if (!currentTile.isSolid() && tile.isSolid()) {
            updateLighting();
        }
    }

    /**
     * Get the tile type at a particular location from tile coordinates.
     *
     * @param x Vertical position of the tile in the world.
     * @param y Horizontal position of the tile in the world.
     * @return The Tile type occupying the position in the world.
     */
    public double getLightLevelAtLocation(int x, int y) {
        // Calculate the chunk and inner-tile coordinates.
        int xchunk = x / CHUNK_GRID_SIZE;
        int ychunk = y / CHUNK_GRID_SIZE;
        int xtile = x % CHUNK_GRID_SIZE;
        int ytile = y % CHUNK_GRID_SIZE;

        // Check the column the chunk is in exists.
        if (!loadedChunks.containsKey(xchunk)) {
            throw new TileNotFoundException();
        }
        // Retrieve the chunk.
        TileChunk chunk = loadedChunks.get(xchunk).get(ychunk);

        if (chunk == null) {
            throw new TileNotFoundException();
        }
        return chunk.getLightLevel(xtile, ytile);
    }

    /**
     * Set the tile at a location to another.
     *
     * @param x    Vertical position of the tile in the world.
     * @param y    Horizontal position of the tile in the world.
     * @param lightLevel Light value to set the tile to (between 0 and 127).
     */
    public void setLightLevelAtLocation(int x, int y, double lightLevel) {
        // Calculate the chunk and inner-tile coordinates.
        int xChunk = x / CHUNK_GRID_SIZE;
        int yChunk = y / CHUNK_GRID_SIZE;
        int xTile = x % CHUNK_GRID_SIZE;
        int yTile = y % CHUNK_GRID_SIZE;

        // Retrieve the chunk, and check it exists.
        if (!loadedChunks.containsKey(xChunk)) {
            throw new TileNotFoundException();
        }
        TileChunk chunk = loadedChunks.get(xChunk).get(yChunk);
        if (chunk == null) {
            throw new TileNotFoundException();
        }

        // Update the light level in the chunk.
        chunk.setLightLevel(xTile, yTile, lightLevel);
    }

    /**
     * Generate a new chunk at the given coordinate using the generator
     *
     * @param chunkX X coordinate of the chunk to generate
     * @param chunkY Y coordinate of the chunk to generate
     */
    public void generateChunk(int chunkX, int chunkY) {
        if (generator == null) {
            throw new TileException("TileGridManager has no generator");
        }
        TileChunk newChunk = generator.generateChunk(chunkX, chunkY);
        addChunk(chunkX, chunkY, newChunk);
    }

    /**
     * Generates the world from scratch, using the current TileGenerator
     */
    public void generateWorld() {
        if (generator == null) {
            throw new TileException("TileGridManager has no generator");
        }

        for (int x = 0; x < World.WIDTH / CHUNK_GRID_SIZE; ++x) {
            for (int y = 0; y < World.HEIGHT / CHUNK_GRID_SIZE; ++y) {
                generateChunk(x, y);
            }
        }
        LOGGER.info(String.format("Generated world of %d x %d chunks",
                World.WIDTH / CHUNK_GRID_SIZE, World.HEIGHT / CHUNK_GRID_SIZE));
    }

    /**
     * Adds a TileModifier to the modification pipeline
     *
     * @param modifier the modifier to add
     */
    public void addModifier(TileModifier modifier) {
        if (modifier == null) {
            throw new TileException("Modifier cannot be null");
        }

        modifiers.add(modifier);
    }

    /**
     * Runs the world through the list of TileModifiers the TileGridManager has
     *
     * @throws TileException if the generator lacks a pipeline
     */
    public void modifyWorld() {
        if (modifiers == null) {
            throw new TileException("TileGridManager has no modifier pipeline");
        }
        // Apply modification for each TileModifier in the pipeline
        for (TileModifier m : modifiers) {
            for (int x = 0; x < World.WIDTH / CHUNK_GRID_SIZE; ++x) {
                for (int y = 0; y < World.HEIGHT / CHUNK_GRID_SIZE; ++y) {
                    if (chunkIsLoaded(x,y)) {
                        m.modifyChunk(x, y, getChunk(x, y));
                    }
                }
            }
        }
    }

    /**
     * Checks whether there is a solid tile within a given rectangle
     *
     * @param pixelX      the pixel-based left x coordinate of the rectangle
     * @param pixelY      the pixel-based top y coordinate of the rectangle
     * @param pixelWidth  the width of the rectangle
     * @param pixelHeight the height of the rectangle
     * @return true iff there exists some solid tile such that it overlaps the rectangle
     */
    public boolean doesRectangleOverlapSolidTile(int pixelX, int pixelY,
                                                 int pixelWidth, int pixelHeight) {
        // Check only tiles within a radius of the rectangle
        int minTileCheckCol = (pixelX - pixelWidth) / TILE_SIZE;
        int minTileCheckRow = (pixelY - pixelHeight) / TILE_SIZE;
        int maxTileCheckCol = (pixelX + 2 * pixelWidth) / TILE_SIZE;
        int maxTileCheckRow = (pixelY + 2 * pixelHeight) / TILE_SIZE;

        // Check all tiles within this radius
        for (int i = minTileCheckCol; i <= maxTileCheckCol; i++) {
            for (int j = minTileCheckRow; j <= maxTileCheckRow; j++) {
                int curTileLeftBound = i * TILE_SIZE;
                int curTileTopBound = j * TILE_SIZE;
                int curTileRightBound = (i + 1) * TILE_SIZE - 1;
                int curTileBottomBound = (j + 1) * TILE_SIZE - 1;

                // Only check solid tiles, and skip tiles in non-existent chunks
                try {
                    if (!getTileAtLocation(i, j).isSolid()) {
                        continue;
                    }
                } catch (TileNotFoundException e) {
                    continue;
                }

                if (curTileRightBound >= pixelX
                        && curTileLeftBound < pixelX + pixelWidth
                        && curTileBottomBound >= pixelY
                        && curTileTopBound < pixelY + pixelHeight) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether there is a solid tile within a given entity
     *
     * @param entity the WorldEntity to check
     * @return true iff there exists some solid tile such that it overlaps the entity
     */
    public boolean doesEntityOverlapSolidTile(WorldEntity entity) {
        return doesRectangleOverlapSolidTile(entity.getXpos(), entity.ypos,
                entity.width, entity.height);
    }

    /**
     * Checks whether the entity would collide with a solid tile if it were moved to the given
     * position
     *
     * @param entity the WorldEntity to check
     * @param pixelX the prospective X position of the entity
     * @param pixelY the prospective Y position of the entity
     * @return true iff there exists some solid tile such that it would overlap the entity if moved
     * to the given position
     */
    public boolean shouldEntityCollideAtPos(WorldEntity entity, int pixelX,
                                            int pixelY) {
        return doesRectangleOverlapSolidTile(pixelX, pixelY, entity.width,
                entity.height);
    }

    /**
     * Update the entire world's lighting.
     */
    public void updateLighting() {
        // Darken all chunks.
        for (HashMap<Integer, TileChunk> chunkColumn : loadedChunks.values()) {
            chunkColumn.values().forEach(minesim.tiles.TileChunk::unlight);
        }

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {
                try {
                    if (!getTileAtLocation(x, y).isSolid()) {
                        lightAroundTile(x, y);
                    }
                }
                catch (TileNotFoundException tnfError) {
                    LOGGER.debug(String.format(
                            "Tried to light at coordinate (%d, %d)", x, y));
                }
            }
        }
    }


    /**
     * Apply a diminishing circle of light around a particular tile
     * @param x horizontal position in the world of the tile.
     * @param y vertical position in the world of the tile.
     */
    private void lightAroundTile(int x, int y) {
        setLightLevelAtLocation(x, y, 0);

        // Iterate a square region around the tile, with radius.
        for (int xTile = -LIGHTING_DISTANCE; xTile < LIGHTING_DISTANCE; xTile++) {
            for (int yTile = -LIGHTING_DISTANCE; yTile < LIGHTING_DISTANCE; yTile++) {

                double distance = Math.sqrt(Math.pow(xTile, 2) + Math.pow(yTile, 2));
                double lightLevel = distance / LIGHTING_RADIUS;

               try {
                   Tile tile = getTileAtLocation(x+xTile, y+yTile);

                   if (!tile.isSolid()) {
                       continue;
                   }

                   double currentLightLevel = getLightLevelAtLocation(x+xTile, y+yTile);
                   if (lightLevel < currentLightLevel) {
                       setLightLevelAtLocation(x+xTile, y+yTile, lightLevel);
                   }
               }
               catch (TileNotFoundException TnfError) {
                   LOGGER.debug(String.format(
                           "Didn't light surrounding tile at coordinate (%d, %d)",
                           x+xTile, y+yTile));
               }
            }
        }
    }


    /**
     * Render chunks to the graphical context being passed in.
     *
     * @param graphicsContext 2D context for a canvas to be drawn to.
     */
    public void render(GraphicsContext graphicsContext, double xOffset, double yOffset,
                       double screenWidth, double screenHeight) {

        graphicsContext.setFill(Color.BLACK);

        // Iterate loaded chunk columns.
        for (Map.Entry<Integer, HashMap<Integer, TileChunk>> xColumnPair :
                loadedChunks.entrySet()) {
            int chunkX = xColumnPair.getKey();
            HashMap<Integer, TileChunk> column = xColumnPair.getValue();

            // Skip if the column isn't visible.
            if ((chunkX + 1) * CHUNK_GRID_SIZE * TILE_SIZE < xOffset ||
                    (chunkX - 1) * CHUNK_GRID_SIZE * TILE_SIZE > xOffset + screenWidth) {
                continue;
            }

            // Iterate chunks in this column.
            for (Map.Entry<Integer, TileChunk> yChunkPair : column.entrySet()) {
                int chunkY = yChunkPair.getKey();

                // Skip chunk if it's not visible.
                if ((chunkY + 1) * CHUNK_GRID_SIZE * TILE_SIZE < yOffset ||
                        (chunkY - 1) * CHUNK_GRID_SIZE * TILE_SIZE > yOffset + screenHeight) {
                    continue;
                }

                // Calculate position to draw chunk.
                double xPos = (chunkX * TILE_SIZE * CHUNK_GRID_SIZE) - xOffset;
                double yPos = (chunkY * TILE_SIZE * CHUNK_GRID_SIZE) - yOffset;

                // Draw the cave background if necessary
                if ((chunkY * TILE_SIZE * CHUNK_GRID_SIZE) >= UNDERGROUND_LEVEL) {
                    graphicsContext.drawImage(caveBackground, xPos, yPos);
                }

                // Draw chunk textures.
                renderChunk(graphicsContext, yChunkPair.getValue(), xPos, yPos);
            }
        }
    }

    /**
     * Internal method to handle the rendering of a single chunk in the viewport.
     * Also includes rendering the light levels of tiles, by drawing dark
     * rectangles of varying opacity to simulate darkness.
     *
     * @param graphicsContext canvas drawing context to use to be drawn to.
     * @param chunk the chunk to be drawn to.
     * @param xPos left pixel position of the chunk to be drawn.
     * @param yPos top pixel position of the chunk to be drawn.
     */
    private void renderChunk(GraphicsContext graphicsContext, TileChunk chunk,
                             double xPos, double yPos) {
        // Iterate the tiles of the chunk.
        for (int x = 0; x < CHUNK_GRID_SIZE; x++) {
            for (int y = 0; y < CHUNK_GRID_SIZE; y++) {
                Tile tile = chunk.getTileType(x, y);

                // Skip air tiles.
                if (tile == Tile.AIR) {
                    continue;
                }

                double shadowLevel = chunk.getLightLevel(x, y);

                // Draw the image if less than full shadow
                if (shadowLevel < 1) {
                    graphicsContext.drawImage(textureRegistry.get(tile),
                            xPos + (x * TILE_SIZE), yPos + (y * TILE_SIZE));
                }

                // Draw the shadow if there's any shadow.
                if (shadowLevel != 1) { // Set opacity if not fully black.
                    graphicsContext.setGlobalAlpha(shadowLevel);
                    graphicsContext.fillRect(xPos + (x * TILE_SIZE), yPos + (y * TILE_SIZE),
                            TILE_SIZE, TILE_SIZE);
                    graphicsContext.setGlobalAlpha(1);
                } else { // Just draw black square in full shadow.
                    graphicsContext.fillRect(xPos + (x * TILE_SIZE), yPos + (y * TILE_SIZE),
                            TILE_SIZE, TILE_SIZE);
                }
            }
        }
        tileMined = false;
    }
}
