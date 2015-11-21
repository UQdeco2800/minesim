package minesim.tasks;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;

import minesim.entities.Peon;
import minesim.entities.items.Item;
import minesim.entities.items.MinedEntity;
import minesim.entities.items.Tool;
import minesim.tiles.Tile;
import minesim.tiles.TileGridManager;
import minesim.tasks.WalkTowards;
import minesim.AchievementDatabaseHandler;

public class DigTile extends Task {

	private Logger logger = Logger.getLogger(DigTile.class);

	private int xpos, ypos, tileLife;
	private TileGridManager manager;
	private String[] oreTiles = new String[] { "Stone", "Gold", "Silver",
			"Copper", "Iron", "Ruby", "Sapphire", "Diamond", "Emerald" };
	private String tileType;
	private Boolean firstCall = true;

	public DigTile(Peon parent, int x, int y, TileGridManager m, int tileLife) {
		super(parent, x, y);
		xpos = x;
		ypos = y;
		manager = m;
		this.tileLife = tileLife;
		// getPeon().getInventory().addItem(shovel);
		tileType = manager.getTileAtLocation(xpos / TileGridManager.TILE_SIZE,
				ypos / TileGridManager.TILE_SIZE).getTitle();
	}

	@Override
	public void doTask() {
		if ((getPeon().getInventory()
				.doesInventoryContainItemWithName("shovel"))) {

			try {
				// check the type of the target tile
				if (manager.getTileAtLocation(xpos / TileGridManager.TILE_SIZE,
						ypos / TileGridManager.TILE_SIZE).equals(Tile.AIR)) {
					// do nothing if it's Air
					onCompletion();
					getPeon().standStill();
				} else {
					// check if grass block to prepare for grass effect removal
					if (manager.getTileAtLocation(
							xpos / TileGridManager.TILE_SIZE,
							ypos / TileGridManager.TILE_SIZE)
							.equals(Tile.GRASS)) {
						manager.tileMined = true;

					}
					// move towards the target tile
					if (Math.pow((getPeon().getXpos() - xpos + 16), 2)
							+ Math.pow((getPeon().getYpos() - ypos + 16), 2) < 900) {
						// check if target in range
						if (manager.getTileAtLocation(
								xpos / TileGridManager.TILE_SIZE,
								ypos / TileGridManager.TILE_SIZE).equals(
								Tile.DIRT)
								|| manager.getTileAtLocation(
										xpos / TileGridManager.TILE_SIZE,
										ypos / TileGridManager.TILE_SIZE)
										.equals(Tile.GRASS)) {
							getPeon().isDigging(true);
						} else {
							getPeon().isMining(true);
						}
						tileLife--;
						getPeon().taskTiredness(2, 1);
						if (tileLife <= 0) {
							manager.setTileAtLocation(xpos
									/ TileGridManager.TILE_SIZE, ypos
									/ TileGridManager.TILE_SIZE, Tile.AIR);
							onCompletion();
							if (getPeon().getMinning()) {
								getPeon().isMining(false);
							} else {
								getPeon().isDigging(false);
							}
						}
					} else {
						getPeon().moveTowards(xpos - 16, ypos, 10);
					}
				}
			} catch (IndexOutOfBoundsException e) {
				logger.debug("Out of Bounds " + xpos + "," + ypos, e);
				onCompletion();
				getPeon().standStill();
			}
		}
	}

	// }

	@Override
	public void onCompletion() {
		MinedEntity ore = new MinedEntity(0, 0, 20, 20, tileType, 10);
		// Subtract health from shovel, based on its durability, after each
		// "dig"
		getPeon()
				.getInventory()
				.getDiggingTool("shovel")
				.subtractHealth(
						(int) ((100 - getPeon().getInventory()
								.getDiggingTool("shovel").getDurability()) * 0.1));
		logger.debug("Shovel health is "
				+ getPeon().getInventory().getDiggingTool("shovel").getHealth());
		// If a peon's digging tool's health falls below zero, reset to 100
		if (getPeon().getInventory().getDiggingTool("shovel").getHealth() <= 0) {
			getPeon().getInventory().getDiggingTool("shovel").setHealth(100);
			// getPeon().getInventory().removeDiggingTool("shovel");
		}
		switch (tileType) {
		case "Stone":
			getPeon().addExperience(5);
			break;
		case "Gold":
			getPeon().addExperience(10);
			break;
		case "Silver":
			getPeon().addExperience(15);
			break;
		case "Copper":
			getPeon().addExperience(20);
			break;
		case "Iron":
			getPeon().addExperience(25);
			break;
		case "Ruby":
			getPeon().addExperience(30);
			break;
		case "Sapphire":
			getPeon().addExperience(35);
			break;
		case "Diamond":
			getPeon().addExperience(40);
			break;
		case "Emerald":
			getPeon().addExperience(45);
			break;
		}

		if (Arrays.asList(oreTiles).contains(tileType)) {
			for (int i = 0; i < getPeon().getLuck(); i++) {
				getPeon().getInventory().addItem(ore);
			}
		}
		AchievementDatabaseHandler achievement = AchievementDatabaseHandler.getInstance();
		achievement.counterIncrement("rockCount");
		super.onCompletion();
	}

	@Override
	public void switchActiveFlag() {
		super.doTask();
		if (manager.getTileAtLocation(xpos / TileGridManager.TILE_SIZE,
				ypos / TileGridManager.TILE_SIZE).equals(Tile.AIR)) {
			onCompletion();
		} else {
			if (firstCall == true) {
				firstCall = false;
				getPeon().addTask(this);
				if (xpos > getPeon().getXpos()) {
					getPeon().addTask(
							new WalkTowards(getPeon(), (xpos / 16) * 16 - 16,
									ypos - 16, getPeon().getParentWorld()
											.getPathfinder()));
				} else {
					getPeon().addTask(
							new WalkTowards(getPeon(), (xpos / 16) * 16 + 16,
									ypos - 16, getPeon().getParentWorld()
											.getPathfinder()));
				}
				getPeon().updateTask(getPeon().getNextTask());
				getPeon().getCurrentTask().get().switchActiveFlag();
			}
		}
	}
}