package minesim.entities.items;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import minesim.ContextAreaHandler;
import minesim.World;
import minesim.entities.ElevatorShaft;
import minesim.entities.HasName;
import minesim.entities.items.properties.HasSpeed;
import minesim.tiles.Tile;
import minesim.tiles.TileGridManager;

/**
 * The transportation class represents items that peons can use to transport
 * themselves in the game (e.g. ropes, ladders elevators).
 *
 */
public class Transportation extends Item implements HasName, HasSpeed {

	// Stating point for dynamic transport generating
	private static int startX;
	private static int startY;
	// End point for dynamic transport generating
	private static int endX;
	private static int endY;
	// Start and end point for clearing a path of air for the transport
	private static int startClearX;
	private static int startClearY;
	private static int endClearY;

	private static TileGridManager manager;
	public static int transportMode;
	public static String transportType;
	public static Boolean primaryClick;
	public static Boolean transportCancel;
	private int speed;

	/**
	 * Creates instance of Transportation which can be a Rope, a Ladder or an
	 * Elevator, and builds it dynamically based on two mouse clicks, one at
	 * either end.
	 *
	 * @param xpos
	 *            The xpos of the transportation
	 * @param ypos
	 *            The ypos of the transportation
	 * @param height
	 *            The height of the transportation
	 * @param width
	 *            The width of the transportation
	 * @param name
	 *            The type of transportation to generate (rope, ladder, or
	 *            elevator)
	 * @param stackLimit
	 *            Transportation items can currently not be stored in
	 *            inventories, so stackLimit = 0
	 */

	public Transportation(int xpos, int ypos, int height, int width,
			String name, int stackLimit) {
		super(xpos, ypos, height, width, name, stackLimit);
		// Set the default stack limit for all transportation items to 5
		super.setStackLimit(5);
		setStartX(0);
		setStartY(0);
		setEnd(0);
	}

	/**
	 * Set the starting point for dynamic transport generating
	 *
	 * @param xCoord
	 *            The new value for the starting X coordinate
	 * @param yCoord
	 *            The new value for the starting Y coordinate
	 */
	public void setStart(int xCoord, int yCoord) {
		startX = xCoord;
		startY = yCoord;
	}

	/**
	 * Set the end point for dynamic transport generating
	 *
	 * @param yCoord
	 *            The new value for the ending Y coordinate
	 */
	public void setEnd(int yCoord) {
		endY = yCoord;
	}

	/**
	 * Builds the transportation entity according to the dynamic size
	 *
	 * @param xCoordinate
	 *            The x position the transportation will be generated on
	 * @param yCoordinate
	 *            The initial y position the transportation will be generated
	 *            from
	 */
	public static void buildTransportEntity(double xCoordinate,
			double yCoordinate) {
		int height;
		int entityAmount = 0;
		int currentX = (int) xCoordinate;
		int currentY = (int) yCoordinate;
		int adjustedX = 0;
		int adjustedY = 0;
		int adjustedEndY = 0;
		int adjustedEndX = 0;
		transportCancel = Boolean.FALSE;
		// Set the coordinates for the first click point, then move on to next
		// click
		if (transportMode == 1 && primaryClick == Boolean.TRUE) {
			startX = currentX;
			startY = currentY;
			primaryClick = Boolean.FALSE;
			transportMode = 2;

			VBox vbox = new VBox();
			vbox.setPadding(new Insets(20, 10, 10, 10));
			Pane t = new Pane();
			Label secondPoint = new Label("Now click the end point...");
			secondPoint.setTextFill(Paint.valueOf("white"));
			t.getChildren().add(secondPoint);
			vbox.getChildren().add(t);
			ContextAreaHandler.getInstance().addContext(transportType, vbox)
					.setContext(transportType);
		}
		// Set the coordinates for the second click point, then move on to build
		// it
		else if (transportMode == 2 && primaryClick == Boolean.TRUE) {

			endX = currentX;
			endY = currentY;
			primaryClick = Boolean.FALSE;
			transportMode = 3;
		} else if (transportMode == 3) {
			/*
			 * Adjust the coordinates to be in the top left corner of the tile
			 * that was clicked on. This ensures that ladders do not overlap
			 * with tiles, and gives a much neater appearance
			 */
			adjustedX = (startX / TileGridManager.TILE_SIZE)
					* TileGridManager.TILE_SIZE;
			adjustedY = (startY / TileGridManager.TILE_SIZE)
					* TileGridManager.TILE_SIZE;
			adjustedEndY = (endY / TileGridManager.TILE_SIZE)
					* TileGridManager.TILE_SIZE;
			adjustedEndX = (endX / TileGridManager.TILE_SIZE)
					* TileGridManager.TILE_SIZE;
			height = Math.abs(adjustedEndY - adjustedY);
			// The number of rope or ladder images required to fill the ladder
			// height
			entityAmount = height / 32;

			// Clear the path for the transport to be generated, by setting all
			// tiles to air
			clearTilePath(adjustedX, adjustedY, adjustedEndY, World
					.getInstance().getTileManager());
			if (transportCancel == Boolean.FALSE) {
				for (int i = 0; i < entityAmount; i++) {
					/*
					 * If the first position selected was above the second
					 * position, then begin generating the transportation from
					 * the top to the bottom of the selected area
					 */
					if (adjustedEndY > adjustedY) {
						if (transportType == "ladder") {
							World.getInstance().addEntityToWorld(
									new Ladder(adjustedX, adjustedY + (i * 32),
											32, 32, "ladder", 0));
						} else if (transportType == "rope") {
							World.getInstance().addEntityToWorld(
									new Rope(adjustedX, adjustedY + (i * 32),
											32, 32, "rope", 0));
						} else if (transportType == "elevator") {
							if (i == 0 || (i == (entityAmount - 1))) {
								World.getInstance().addEntityToWorld(
										new Elevator(adjustedX, adjustedY
												+ (i * 32), 32, 32, "elevator",
												0));
							} else {
								World.getInstance().addEntityToWorld(
										new ElevatorShaft(adjustedX, adjustedY
												+ (i * 32), 32, 32,
												"elevatorShaft", 0));
							}
						}

					}
					/*
					 * Otherwise, the first position selected was below the
					 * second position, so begin generating the transportation
					 * from the bottom to the top of the selected area
					 */
					else {
						if (transportType == "ladder") {
							World.getInstance().addEntityToWorld(
									new Ladder(adjustedX, adjustedY
											- (i * 32 + 16), 32, 32, "ladder",
											0));
						} else if (transportType == "rope") {
							World.getInstance()
									.addEntityToWorld(
											new Rope(adjustedX, adjustedY
													- (i * 32 + 16), 32, 32,
													"rope", 0));
						} else if (transportType == "elevator") {
							if (i == 0 || (i == (entityAmount - 1))) {
								World.getInstance().addEntityToWorld(
										new Elevator(adjustedX, adjustedY
												- (i * 32 + 16), 32, 32,
												"elevator", 0));
							} else {
								World.getInstance().addEntityToWorld(
										new ElevatorShaft(adjustedX, adjustedY
												- (i * 32 + 16), 32, 32,
												"elevatorShaft", 0));
							}
						}
					}
				}
				// Display to the player that a rope, ladder, or elevator was
				// just added
				VBox vbox = new VBox();
				vbox.setPadding(new Insets(20, 10, 10, 10));
				Pane t = new Pane();
				Label transportationLabel = new Label("You just added a "
						+ transportType);
				transportationLabel.setTextFill(Paint.valueOf("white"));
				t.getChildren().add(transportationLabel);
				vbox.getChildren().add(t);
				ContextAreaHandler.getInstance()
						.addContext(transportType, vbox)
						.setContext(transportType);
			}
			// Transport generating was cancelled with a right click, so do
			// nothing.
			else {
				Pane t = new Pane();
				t.getChildren().add(
						new Label(transportType + " creation was cancelled"));
				ContextAreaHandler.getInstance().addContext(transportType, t)
						.setContext(transportType);
			}
			primaryClick = Boolean.FALSE;
			transportCancel = Boolean.FALSE;
			transportMode = 0;
			transportType = null;
		}
	}

	/**
	 * Returns the speed of instance
	 *
	 * @return speed
	 */
	@Override
	public int getSpeed() {
		return this.speed;
	}

	/**
	 * Sets the speed of instance
	 *
	 * @param speed
	 *            The speed to set the instance to
	 */
	@Override
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Increases the item speed by the specified amount
	 *
	 * @param speed
	 *            The amount to increase the speed by
	 */
	@Override
	public void addSpeed(int speed) {
		this.speed = this.speed + speed;
	}

	/**
	 * Returns the first X position of the transport
	 *
	 * @return startX
	 */
	public static int getStartX() {
		return startX;
	}

	/**
	 * The starting x coordinate for the current transportation
	 *
	 * @param startX
	 *            Set the first X position of the transport
	 */
	public static void setStartX(int startX) {
		Transportation.startX = startX;
	}

	/**
	 * Returns the first Y position of the transport
	 *
	 * @return startY
	 */
	public static int getStartY() {
		return startY;
	}

	/**
	 * The starting y coordinate for the current transportation
	 *
	 * @param startY
	 *            Set the first Y position of the transport
	 */
	public static void setStartY(int startY) {
		Transportation.startY = startY;
	}

	/**
	 * Returns the last X position of the transport
	 *
	 * @return endX
	 */
	public static int getEndX() {
		return endX;
	}

	/**
	 * Returns the last Y position of the transport
	 *
	 * @return endX
	 */
	public static int getEndY() {
		return endY;
	}

	/**
	 * This function when called will set all tiles within the specified area to
	 * be air tiles. This is so that when ropes, ladders, or elevators are
	 * placed, peons are able to travel underground using them without
	 * unnecessary collisions.
	 *
	 * @param startX
	 *            The x position from which tiles will be cleared
	 * @param startY
	 *            The y position from which tiles will be cleared
	 * @param endY
	 *            The y position at which tiles will stop being cleared
	 * @param gridManager
	 *            The manager of all tiles currently stored in the world
	 */
	public static void clearTilePath(int startX, int startY, int endY,
			TileGridManager gridManager) {
		startClearX = startX;
		startClearY = startY;
		endClearY = endY;
		manager = gridManager;
		/*
		 * If the first point selected is above the last point selected, start
		 * setting the tiles to air from the top to the bottom
		 */
		if (startClearY < endClearY) {
			while (startClearY < endClearY) {
				/*
				 * We need to clear two tiles each time as peons are 32 wide,
				 * whilst tiles are only 16 wide
				 */
				manager.setTileAtLocation(startClearX
						/ TileGridManager.TILE_SIZE, startClearY
						/ TileGridManager.TILE_SIZE, Tile.AIR);
				manager.setTileAtLocation((startClearX + 16)
						/ TileGridManager.TILE_SIZE, startClearY
						/ TileGridManager.TILE_SIZE, Tile.AIR);
				// Increment to the next tile
				startClearY += 16;
			}
		}
		/*
		 * If the first point selected is below the last point selected, start
		 * setting the tiles to air from the bottom to the top
		 */
		else {
			while (startClearY > endClearY) {
				/*
				 * We need to clear two tiles each time as peons are 32 wide,
				 * whilst tiles are only 16 wide
				 */
				manager.setTileAtLocation(startClearX
						/ TileGridManager.TILE_SIZE, startClearY
						/ TileGridManager.TILE_SIZE, Tile.AIR);
				manager.setTileAtLocation((startClearX + 16)
						/ TileGridManager.TILE_SIZE, startClearY
						/ TileGridManager.TILE_SIZE, Tile.AIR);
				// Increment to the next tile
				startClearY -= 16;
			}
		}
	}
}