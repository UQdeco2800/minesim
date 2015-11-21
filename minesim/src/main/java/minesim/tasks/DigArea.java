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

public class DigArea extends Task {
	private int x, y, height, width;
	private TileGridManager manager;
	public DigArea(Peon parent, int x1, int x2, int y1, int y2, TileGridManager m) {
		super(parent, x1, y1);
		x = Math.max(x1/16, x2/16);
		width = Math.abs((x1/16) - (x2/16)) + 1;
		y = Math.max(y1/16, y2/16);
		height = Math.abs((y1/16) - (y2/16)) + 1;
		manager = m;
		System.out.println(x);
		System.out.println(y);
		System.out.println(width);
		System.out.println(height);
	}
	
	@Override
	public void switchActiveFlag() {
		int count;
		if (getPeon().getXpos() > x * 16) {
			count = height % 2;
		} else {
			count = (height + 1) % 2;
		}
		for (int j = 0; j < height; j++) {
			if (count % 2 == 0) {
				for (int i = 0; i < width; i++) {
					Task newtask = new DigTile(getPeon(), (x - i) * 16, (y - j) * 16, manager, 10);
					//newtask.switchActiveFlag();
					getPeon().addTask(newtask);
				}
			} else {
				for (int i = width - 1; i >= 0; i--) {
					Task newtask = new DigTile(getPeon(), (x - i) * 16, (y - j) * 16, manager, 10);
					//newtask.switchActiveFlag();
					getPeon().addTask(newtask);
				}
			}
			count++;
		}
		getPeon().updateTask(getPeon().getNextTask());
		getPeon().getCurrentTask().get().switchActiveFlag();
	}
}
