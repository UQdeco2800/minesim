package minesim;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import javafx.scene.paint.Color;
import minesim.entities.AvianMob;
import minesim.entities.BatMob;
import minesim.entities.Building;
import minesim.entities.BuildingTeleporterIn;
import minesim.entities.BuildingTeleporterOut;
import minesim.entities.Peon;
import minesim.entities.PeonGather;
import minesim.entities.PeonGuard;
import minesim.entities.PeonMiner;
import minesim.entities.RequisitionStore;
import minesim.entities.WorldEntity;
import minesim.entities.*;
import minesim.entities.items.Ladder;
import minesim.entities.items.Rope;
import minesim.inputhandlers.SelectionBox;
import minesim.pathfinder.Pathfinder;
import minesim.tasks.DigTile;
import minesim.tasks.InventoryGather;
import minesim.tasks.PeonPiggyBack;
import minesim.tasks.Task;
import minesim.tiles.*;

public class World {

    private static final int MAX_WIDTH = 2000;
    private static final int MAX_HEIGHT = 2000;
    public static WorldTimer timer = new WorldTimer();
    private static World instance = null;
    public int worldx = 800, worldy = 600;

    /**
     * The total width of the world in tiles
     */
    public static final int WIDTH = ((MAX_WIDTH + (int)(2 * MineSim.RESOLUTION_WIDTH)) / (TileGridManager.TILE_SIZE)) + 1;
    /**
     * The total height of the world in tiles
     */
    public static final int HEIGHT = ((MAX_HEIGHT + (int)(2 * MineSim.RESOLUTION_HEIGHT)) / (TileGridManager.TILE_SIZE)) + 1;
    /*
     * Task/class definitions
     */
    Class<Task>[] minerTasks = new Class[] { DigTile.class};
    Class<Task>[] guardTasks = new Class[] {};
    Class<Task>[] gatherTasks = new Class[] { InventoryGather.class,
            PeonPiggyBack.class };
    private int currentxoffset = 0;
    private int currentyoffset = 0;
    private TileGridManager tilemanager = new TileGridManager(
            new RollingHillsGenerator());
    private Pathfinder pathfinder = new Pathfinder(this, tilemanager);
    /*
     * A world has a big list of all its entities
     */
    private ConcurrentLinkedDeque<WorldEntity> worldentities = new ConcurrentLinkedDeque<WorldEntity>();
    
    public ConcurrentLinkedDeque<WorldEntity> achievments = new ConcurrentLinkedDeque<>();
    /*
     * a World has a list of assigned tasks
     */
    private ConcurrentLinkedDeque<Task> peonworldtasks = new ConcurrentLinkedDeque<Task>();
    /*
     * some tasks should be grouped based on what peons will be doing them
     */
    private ConcurrentLinkedDeque<Task> minerWorldTasks = new ConcurrentLinkedDeque<Task>();
    private ConcurrentLinkedDeque<Task> gathererWorldTasks = new ConcurrentLinkedDeque<Task>();
    private ConcurrentLinkedDeque<Task> guardWorldTasks = new ConcurrentLinkedDeque<Task>();
    /*
     * To ensure entites are not removed as they are being used we must queue
     * them for deletion and delete the necessary entities on the next tick,
     * where we can ensure they are not being used
     */
    private ConcurrentLinkedDeque<WorldEntity> deletionqueue = new ConcurrentLinkedDeque<WorldEntity>();
    private Rectangle2D selectedArea;
    private Rectangle2D screenArea;
    private int contextMode = 0;
    private Color areaColor = Color.rgb(255, 128, 128, 0.25);

    private ResizableCanvas canvas;

    public World(int worldx, int worldy) {
        this.worldx = worldx;
        this.worldy = worldy;

        // Generate the world
        tilemanager.addModifier(new SquareVeinTileModifier());
        tilemanager.addModifier(new PerlinVeinTileModifier());
        tilemanager.addModifier(new BasicCaveTileModifier(World.WIDTH,
                World.HEIGHT));
        tilemanager.generateWorld();
        tilemanager.modifyWorld();
        tilemanager.updateLighting();
        pathfinder.updateMap();

    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World(MineSim.RESOLUTION_WIDTH, MineSim.RESOLUTION_HEIGHT);
        }
        return instance;
    }

    public void setDimensions(int width, int height) {
        worldx = width;
        worldy = height;
    }

    public int getWidth() {
        return worldx;
    }

    public int getHeight() {
        return worldy;
    }

    /**
     * Set the width of the world screen
     *
     * @param width the new width
     */
    public void setWidth(int width) {
        worldx = width;
    }

    /**
     * Set the height of the world screen
     *
     * @param height the new height
     */
    public void setHeight(int height) {
        worldy = height;
    }

    public void addEntityToWorld(WorldEntity entity) {
        if (entity.getClass().equals(Building.class)
                || entity.getClass().equals(Ladder.class)
                || entity.getClass().equals(Rope.class)) {
            worldentities.addFirst(entity);
        }
        if (entity.getClass().getSuperclass().equals(Building.class)
                || entity.getClass().equals(Ladder.class)
                || entity.getClass().equals(Rope.class)) {
            worldentities.addFirst(entity);
        } else {
            worldentities.addLast(entity);
        }
    }
    
    /**
     * Adds an achivement to the world in the correct position
     * @param achievment 
     */
    public void addAchievmentToWorld(Achievement achievment) {
        if(achievment == null){
            return;
        }
        achievments.add(achievment);
   }

    public void setCanvas(ResizableCanvas canvas) {
        this.canvas = canvas;
    }

    public ResizableCanvas getCanvas() {
        return canvas;
    }

    public void removeEntityFromWorld(WorldEntity entity) {
        deletionqueue.add(entity);
    }

    /* only 1 teleporterin and out allowed */
    public boolean worldContainsTeleporterIn() {
        for (WorldEntity e : worldentities) {
            if (e.getClass().equals(BuildingTeleporterIn.class)) {
                return true;
            }
        }
        return false;
    }

    public boolean worldContainsTeleporterOut() {
        for (WorldEntity e : worldentities) {
            if (e.getClass().equals(BuildingTeleporterOut.class)) {
                return true;
            }
        }
        return false;
    }

    /*
     * this will check if both teleporters exist and set them up, otherwise they
     * will be set down. call either to check or tear down
     */
    public boolean worldSetUpTeleporter() {
        BuildingTeleporterIn in = null;
        BuildingTeleporterOut out = null;
        for (WorldEntity e : worldentities) {
            if (e.getClass().equals(BuildingTeleporterIn.class)) {
                in = (BuildingTeleporterIn) e;
            }
            if (e.getClass().equals(BuildingTeleporterOut.class)) {
                out = (BuildingTeleporterOut) e;
            }
        }

        if (in != null && out == null) {
            in.unsetExit();
        }

        if (in == null || out == null) {
            return false;
        }
        in.setExit(out.getXpos(), out.getYpos());
        return true;
    }

    /* for testing only. add teleporters once the world has been instantiated */
    public void addTeles() {
        addEntityToWorld(new BuildingTeleporterIn(40, 100));
        addEntityToWorld(new BuildingTeleporterOut(700, 100));
        worldSetUpTeleporter();
    }

    public void onTick(int tickCount) {
        timer.onTick();
        if (deletionqueue.size() != 0) {
            System.out.println("Cleaning up " + deletionqueue.size()
                    + " dead entities");
            for (WorldEntity entity : deletionqueue) {
                System.out.print("    ");
                System.out.println(entity);
                worldentities.remove(entity);
            }

            // Clear the deletion queue as they have all been removed from the
            // world
            deletionqueue.clear();
        }

        // Propogate the system ticks onto all entites to facilitate things AI
        // and tasks
        for (WorldEntity entity : worldentities) {
            entity.onTick();
        }

    }

    /**
     * Returns a the worldentitites hashmap
     *
     * @return THE ENTITIES LIST
     */
    public ConcurrentLinkedDeque<WorldEntity> getWorldentities() {
        return worldentities;
    }

    public void addTask(Task task) {
        if (Arrays.asList(minerTasks).contains(task.getClass())) {
            minerWorldTasks.add(task);
        } else if (Arrays.asList(guardTasks).contains(task.getClass())) {
            guardWorldTasks.add(task);
        } else if (Arrays.asList(gatherTasks).contains(task.getClass())) {
            gathererWorldTasks.add(task);
        } else {
            peonworldtasks.add(task);
        }
    }

    /**
     * removes the supplied tasks from the deque of tasks that contains it
     */
    public void removeTask(Task task) {
        if (peonworldtasks.contains(task)) {
            peonworldtasks.remove(task);
        } else if (minerWorldTasks.contains(task)) {
            minerWorldTasks.remove(task);
        } else if (guardWorldTasks.contains(task)) {
            guardWorldTasks.remove(task);
        } else if (gathererWorldTasks.contains(task)) {
            gathererWorldTasks.remove(task);
        }
    }

    /**
     * returns the list of tasks most appropriate for the supplied Peon to have
     * a task assigned from (eg, miners will get the list of mining related
     * tasks, or if that is empty, the list of generic tasks) (eg, a normal Peon
     * will get the list of generic tasks)
     *
     * @return tasksDeque
     */
    public Deque<Task> getWorldTasks(Peon peon) {
        if (peon instanceof PeonMiner) {
            if (minerWorldTasks.isEmpty()) {
                return peonworldtasks;
            }
            return minerWorldTasks;
        } else if (peon instanceof PeonGuard) {
            if (guardWorldTasks.isEmpty()) {
                return peonworldtasks;
            }
            return guardWorldTasks;
        } else if (peon instanceof PeonGather) {
            if (gathererWorldTasks.isEmpty()) {
                return peonworldtasks;
            }
            return gathererWorldTasks;
        }
        return peonworldtasks;
    }

    public String worldTasksToString() {
        String returnString;
        ArrayList<Task> tasks = new ArrayList(peonworldtasks);
        int i;
        // Pipe character (]) is used to split generic, miner, guard and gatherer tasks
        // Asterisk character (-) is used to split individual tasks
        returnString = "";
        for (i = 0; i < peonworldtasks.size(); i++) {
            returnString = returnString + tasks.get(i).getClass().getSimpleName() + ", x: "
                    + tasks.get(i).getDest() + ", y: " + tasks.get(i).getDestYPos() + "-";
        }
        tasks = new ArrayList(minerWorldTasks);
        returnString = returnString + "] ";
        for (i = 0; i < minerWorldTasks.size(); i++) {
            returnString = returnString + tasks.get(i).getClass().getSimpleName() + ", x: "
                    + tasks.get(i).getDest() + ", y: " + tasks.get(i).getDestYPos() + "-";
        }
        tasks = new ArrayList(guardWorldTasks);
        returnString = returnString + "] ";
        for (i = 0; i < guardWorldTasks.size(); i++) {
            returnString = returnString + tasks.get(i).getClass().getSimpleName() + ", x: "
                    + tasks.get(i).getDest() + ", y: " + tasks.get(i).getDestYPos() + "-";
        }
        tasks = new ArrayList(gathererWorldTasks);
        returnString = returnString + "] ";
        for (i = 0; i < gathererWorldTasks.size(); i++) {
            returnString = returnString + tasks.get(i).getClass().getSimpleName() + ", x: "
                    + tasks.get(i).getDest() + ", y: " + tasks.get(i).getDestYPos() + "-";
        }
        return returnString;

    }
    
    public int getMaxWidth(){
    	return MAX_WIDTH;
    }
    /*
     * Returns the area which is selected by dragging the mouse
     */
    public Rectangle2D getArea() {
        return selectedArea != null ? selectedArea : SelectionBox.NOTHING;
    }

    public void setArea(Rectangle2D rect) {
        this.selectedArea = rect;
    }
    
    public void setAreaColor(Color color) {
    	this.areaColor = color;
    }
    
    public Color getAreaColor() {
    	return this.areaColor;
    }

    /**
     * Use this to get the rectangle relative to the panel (screen) coordinates
     *
     * @return the selected area visually represented on screen
     */
    public Rectangle2D getScreenArea() {
        if (screenArea == null) {
            screenArea = new Rectangle2D.Double(getArea().getX(), getArea()
                    .getY(), getArea().getWidth(), getArea().getHeight());
        }
        screenArea.setRect(getArea().getX() - getXOffset(), getArea().getY()
                - getYOffset(), getArea().getWidth(), getArea().getHeight());
        return screenArea;
    }

    public Optional<WorldEntity> getEntityForCoordinates(double x, double y) {
        /*
         * Iterate over entity deque backwards(last->first) so peons are
         * selected before buildings
         */
        Iterator it = worldentities.descendingIterator();
        while (it.hasNext()) {
            WorldEntity entity = (WorldEntity) it.next();
            if (entity.containsCoordinate(x, y)) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }
    
    public Optional<WorldEntity> getEntityForCoordinates(double x, double y, Class c) {
        /*
         * Iterate over entity deque backwards(last->first) so peons are
         * selected before buildings
         */
        Iterator it = worldentities.descendingIterator();
        while (it.hasNext()) {
            WorldEntity entity = (WorldEntity) it.next();
            if (entity.containsCoordinate(x, y) && c.isInstance(entity)) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    public TileGridManager getTileManager() {
        return tilemanager;
    }

    public WorldTimer getWorldTimer() {
        return timer;
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    /**
     * Retrieve the entity of the given class that is closest to the sender's
     * xpos
     *
     * @param c
     *            The class to locate
     * @param sender
     *            The WorldEntity to search around
     * @return entity closest to sender (optional)
     */
    public Optional<?> getNearest(Class c, WorldEntity sender) {
        int distance = Integer.MAX_VALUE;
        WorldEntity closest = null;
        for (WorldEntity entity : this.getWorldentities()) {
            if (c.isInstance(entity)
                    && (closest == null || (Math.abs(entity.getXpos()
                            - sender.getXpos()) < distance && !entity
                                .equals(sender)))) {
                // This entity is the new closest
                distance = Math.abs(entity.getXpos() - sender.getXpos());
                closest = entity;
            }
        }
        return closest != null ? Optional.of(closest) : Optional.empty();
    }

    /**
     * Retrieve the entity of the given class that is closest to below the
     * sender's ypos
     * 
     * @param c
     *            The class to locate
     * @param sender
     *            The WorldEntity to search around
     * @return entity closest to sender (optional)
     */
    public Optional<?> getNearestYbelow(Class c, WorldEntity sender) {
        int distance = Integer.MAX_VALUE;
        WorldEntity closest = null;
        for (WorldEntity entity : this.getWorldentities()) {
            if (c.isInstance(entity)
                    && (closest == null || (Math.abs(entity.getYpos()
                            - sender.getYpos()) < distance && !entity
                                .equals(sender)))
                    && (entity.getXpos() == sender.getXpos())
                    && (entity.getYpos() >= (sender.getYpos())
                    && (!entity.equals(sender)))) {
                // This entity is the new closest
                distance = Math.abs(entity.getYpos() - sender.getYpos());
                closest = entity;
            }
        }
        return closest != null ? Optional.of(closest) : Optional.empty();
    }

    /**
     * Retrieve the entity of the given class that is closest to above the
     * sender's ypos
     * 
     * @param c
     *            The class to locate
     * @param sender
     *            The WorldEntity to search around
     * @return entity closest to sender (optional)
     */
    public Optional<?> getNearestYabove(Class c, WorldEntity sender) {
        int distance = Integer.MAX_VALUE;
        WorldEntity closest = null;
        for (WorldEntity entity : this.getWorldentities()) {
            if (c.isInstance(entity)
                    && (closest == null || (Math.abs(entity.getYpos()
                            - sender.getYpos()) < distance && !entity
                                .equals(sender)))
                    && (entity.getXpos() == sender.getXpos())
                    && (entity.getYpos() <= sender.getYpos()
                    && (!entity.equals(sender)))) {
                // This entity is the new closest
                distance = Math.abs(entity.getYpos() - sender.getYpos());
                closest = entity;
            }
        }
        return closest != null ? Optional.of(closest) : Optional.empty();
    }

    public int getXOffset() {
        return currentxoffset;
    }

    public int getYOffset() {
        return currentyoffset;
    }

    public void moveScreenLeft(int x) {
        currentxoffset = Math.max(0, currentxoffset - x);
    }

    public void moveScreenRight(int x) {
        currentxoffset = Math.min(MAX_WIDTH, currentxoffset + x);
    }

    public void moveScreenUp(int y) {
        currentyoffset = Math.max(0, currentyoffset - y);
    }

    public void moveScreenDown(int y) {
        currentyoffset = Math.min(MAX_HEIGHT, currentyoffset + y);
    }

    public int getContextMode() {
        return contextMode;
    }

    public void setContextMode(int contextMode) {
        this.contextMode = contextMode;
    }
}
