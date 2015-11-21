package minesim.functions;

import java.util.ArrayList;
import java.util.Optional;

import minesim.World;
import minesim.entities.BuildingBar;
import minesim.entities.Peon;
import minesim.entities.WorldEntity;
import minesim.entities.items.Transportation;
import minesim.functions.DisplayBuildingInstructions;

public class AddEntityOnMouseClick {

    // The game world in which the world takes place
    private static World gameWorld;
    // A list of registered WorldEntites
    private static ArrayList<WorldEntity> entitiesList;
    private static ArrayList<WorldEntity> entityTag = new ArrayList<WorldEntity>();
    // A toggle to determine whether can add
    private static Boolean buildMode = Boolean.FALSE;
    private static Boolean connectElevator = Boolean.FALSE;
    private static int counter;
    private static WorldEntity entity;
    private static Boolean placeItem = Boolean.FALSE;
    private static Boolean alternate = Boolean.FALSE;

    /**
     * Never called, private constructor. Purely to fix sonar complaining
     */
    private AddEntityOnMouseClick() {
    }

    /**
     * An ArrayList of valid world entities that can be added into the world
     *
     * @require xpos >= 0 && ypos >= 0 && type <= (entitiesList.size() - 1) &&
     *          type >= 0
     */
    public static void entityList(WorldEntity entity) {
        entitiesList = new ArrayList<WorldEntity>();
        entitiesList.add(entity);
    }

    /**
     * Adds an entity to the world on primary mouse click. Entity added
     * dependent on type value
     *
     * @require xpos >= 0 && ypos >= 0 && type <= (entitiesList.size() - 1) &&
     *          type >= 0 && buildMode == Boolean.TRUE
     * @ensure new entity is added into world
     */
    public static boolean addEntity(double xpos, double ypos, World gameWorld) {
        try {
            AddEntityOnMouseClick.gameWorld = gameWorld;
            if (buildMode) {
                WorldEntity entity = entityTag.get(0);
                if (checkAddPosition(xpos, ypos, entity)) {
                    return false;
                }
                if (alternate) {
                    alternateModfiicationClick(xpos, ypos, gameWorld);
                } else {
                    placeItem = Boolean.TRUE;
                    return true;
                }

            }

        } catch (NoClassDefFoundError | ExceptionInInitializerError e) {
        }
        return false;

    }

    public static void moveEntity(double xpos, double ypos, World gameWorld) {
        try {
        AddEntityOnMouseClick.gameWorld = gameWorld;
        if (buildMode) {
            WorldEntity entity = entitiesList.get(0);
            if (entityTag.isEmpty()) {
                entityTag.add(entity);
                gameWorld.addEntityToWorld(entity);
                entity.setEntityGravity(Boolean.FALSE);
            } else {
                entity = entityTag.get(0);
            }
            if (alternate) {
                alternateModfiicationMove(xpos, ypos, gameWorld);
            } else {
                if (placeItem) {
                    entityTag.remove(0);
                    entity.setEntityGravity(Boolean.TRUE);
                    buildMode = Boolean.FALSE;
                    placeItem = Boolean.FALSE;
                    DisplayBuildingInstructions.clearContainer();
                } else {
                    int xGrid = formatGridX(xpos);
                    int yGrid = formatGridY(ypos);
                    entity.setXpos(xGrid);
                    entity.setYpos(yGrid);
                }
            }

        }
        } catch (NoClassDefFoundError | ExceptionInInitializerError e) {
            
        }
    }

    /**
     * Checks whether an entity will spawn on tile or if an entity already
     * exists at that location
     *
     * @require xpos >= 0 && ypos >= 0 &&
     * @ensure Entity does not spawn where a tile exists
     */
    private static boolean checkAddPosition(double xpos, double ypos, WorldEntity entityAdded) {
        try {
        Optional<WorldEntity> entityClicked = gameWorld.getEntityForCoordinates(xpos, ypos, entityAdded.getClass());
        if (!entityClicked.get().equals(entityAdded)
                || World.getInstance().getTileManager().shouldEntityCollideAtPos(entityAdded, (int) xpos, (int) ypos)) {
            return Boolean.TRUE;
        }
        } catch (Exception e) {
            
        }
        return Boolean.FALSE;
    }

    /**
     * Set entities to be added on a xpos factor of 16px. Entities only spawn on
     * an xpos which returns 0 when divided by 16
     *
     * @require xpos >= 0
     * @ensure entity added on x position which is a factor of 16
     */
    private static int formatGridX(double xpos) {
        double factor = 16.0;
        int xGrid = (int) Math.floor(Math.floor(xpos) / factor);
        xGrid = xGrid * (int) factor;
        return xGrid;
    }

    private static int formatGridY(double xpos) {
        double factor = 8.0;
        int yGrid = (int) Math.ceil(Math.ceil(xpos) / factor);
        yGrid = yGrid * (int) factor;
        return yGrid;
    }

    /**
     * @return the current state of the build mode toggle
     */
    public static boolean getBuildMode() {
        return buildMode;
    }

    /**
     * Toggle when an entity can be added. When false entity cannot, when true
     * entity can be added to world
     *
     * @require b == Boolean.FALSE || b == Boolean.TRUE
     * @ensure buildMode = b
     */
    public static void setBuildMode(Boolean b) {
        buildMode = b;
    }

    /**
     * Adds an entity to the world on primary mouse click. Entity added
     * dependent on type value
     *
     * @require xpos >= 0 && ypos >= 0 && type <= (entitiesList.size() - 1) &&
     *          type >= 0 && buildMode == Boolean.TRUE
     * @ensure new entity is added into world
     */
    public static boolean removeEntity(World gameWorld) {
        AddEntityOnMouseClick.gameWorld = gameWorld;
        if (buildMode) {
            WorldEntity entity = entityTag.get(0);
            entity.alive = false;
            placeItem = Boolean.TRUE;
            return true;

        }
        return false;
    }

    public static void activateMod(Boolean state) {
        alternate = Boolean.TRUE;
    }

    // If you require special modifications to the Point & Click function. Use
    // external methods to override existing functionality
    // If alternate is set true, the default single add is disabled. However,
    // all the required checking remains present
    private static void alternateModfiicationClick(double xpos, double ypos, World gameWorld) {
        if (alternate) {

        }
    }

    private static void alternateModfiicationMove(double xpos, double ypos, World gameWorld) {
        if (alternate) {

        }
    }
}
