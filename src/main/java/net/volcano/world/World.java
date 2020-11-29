package net.volcano.world;

import net.volcano.Game;
import net.volcano.Settings;
import net.volcano.entity.Cell;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class World {
	
	private final Settings settings = Game.settings;
	
	private final Cell[][] cells;
	
	public World() {
		
		Random rand = new Random();
		
		cells = new Cell[settings.width][settings.height];
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				cells[x][y] = new Cell(x, y, this);
				if (rand.nextBoolean()) {
					cells[x][y].nAlive = true;
				}
			}
		}
		
	}
	
	public synchronized void render(Graphics2D g) {
		
		Arrays.stream(cells)
				.parallel()
				.flatMap(Arrays::stream)
				.forEach(c -> c.render((Graphics2D) g.create()));
		
		g.setColor(Color.RED);
		
		g.fillRect(-settings.cellSize * 2, -settings.cellSize * 2, settings.cellSize * 2, (settings.height + 4) * settings.cellSize);
		g.fillRect(-settings.cellSize * 2, -settings.cellSize * 2, (settings.width + 4) * settings.cellSize, settings.cellSize * 2);
		
		//g.fillRect(-settings.cellSize * 2, (settings.width + 4) * settings.cellSize, settings.cellSize * 2, (settings.width + 4) * settings.cellSize);
		//g.fillRect((settings.width + 4) * settings.cellSize, -settings.cellSize * 2, (settings.width + 4) * settings.cellSize, settings.cellSize * 2);
		
		g.fillRect(settings.width * settings.cellSize, -settings.cellSize * 2, settings.cellSize * 2, (settings.height + 4) * settings.cellSize);
		g.fillRect(-settings.cellSize * 2, settings.height * settings.cellSize, (settings.width + 4) * settings.cellSize, settings.cellSize * 2);
		
	}
	
	public synchronized void tick() {
		
		// Update new values
		Arrays.stream(cells)
				.parallel()
				.flatMap(a -> Arrays.stream(a).parallel())
				.forEach(Cell::update);
		
		// Calculate new values
		Arrays.stream(cells)
				.parallel()
				.flatMap(a -> Arrays.stream(a).parallel())
				.forEach(Cell::tick);
		
	}
	
	public Cell[][] getCells() {
		return cells;
	}
	
	public void insertPattern(int startX, int startY, Boolean[][] aliveMatrix) {
		for (int x = 0; x < aliveMatrix.length; x++) {
			for (int y = 0; y < aliveMatrix[x].length; y++) {
				//cells[y + startY][x + startX].nAlive = aliveMatrix[x][y];
			}
		}
	}
	
}
