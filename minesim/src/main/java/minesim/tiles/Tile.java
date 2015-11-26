package minesim.tiles;

/**
 * Discrete specifications of the tiles that constitute the game world's main composition.
 *
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public enum Tile {
    /**
     * Represents the absence of any solid material.
     */
    AIR((byte) 0, "Air", null, false),

    STONE((byte) 1, "Stone", "texturepack/stone.png", true),
    GRASS((byte) 2, "Grass", "texturepack/grass.png", true),
    DIRT((byte) 3, "Dirt", "texturepack/dirt.png", true),
    GOLD((byte) 4, "Gold", "texturepack/gold.png", true),
    SILVER((byte) 5, "Silver", "texturepack/silver.png", true),
    COPPER((byte) 6, "Copper", "texturepack/copper.png", true),
    IRON((byte) 7, "Iron", "texturepack/iron.png", true),
    RUBY((byte) 8, "Ruby", "texturepack/ruby.png", true),
    SAPPHIRE((byte) 9, "Sapphire", "texturepack/sapphire.png", true),
    DIAMOND((byte) 10, "Diamond", "texturepack/diamond.png", true),
    EMERALD((byte) 11, "Emerald", "texturepack/emerald.png", true),
    BLADES((byte) 12, "Blades", "texturepack/blades.png", false),
    FLOWER((byte) 13, "Flower", "texturepack/flower.png", false),
    THICKET((byte) 14, "Thicket", "texturepack/thicket.png", false);

    private final byte id;
    private final String title;
    private final String texture;
    private final boolean solid;

    Tile(byte newid, String newtitle, String newtexture, boolean isSolid) {
        id = newid;
        title = newtitle;
        texture = newtexture;
        solid = isSolid;

    }

    /**
     * Return the instance of a tile as specified by an id number.
     *
     * @param id the ID number of the tile being looked up.
     * @return The Tile possessing the id passed in.
     */
    public static Tile getById(byte id) {
        for (Tile tile : values()) {
            if (tile.id == id) {
                return tile;
            }
        }
        return null;
    }

    /**
     * Getter for the id of this tile.
     *
     * @return Byte id of this tile.
     */
    public byte getId() {
        return id;
    }

    /**
     * Getter method for the human readable title of this tile.
     *
     * @return Human Readable title for this tile.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter method for the texture url.
     *
     * @return The URL of the texture resource used for this tile.
     */
    public String getTextureUrl() {
        return texture != null ? texture : null;
    }

    /**
     * Getter method for the solid property
     *
     * @return the boolean value of whether the tile type is solid
     */
    public boolean isSolid() {
        return solid;
    }
}
