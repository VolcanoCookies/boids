package net.volcano.entity;

import net.volcano.Game;
import net.volcano.Settings;
import net.volcano.Util;
import net.volcano.world.World;

import java.awt.*;

public class Cell implements Tickable,
		Renderable {
	
	private static final int[][] DISPLACEMENTS = {{1, 0},
	                                              {1, 1},
	                                              {0, 1},
	                                              {-1, 1},
	                                              {-1, 0},
	                                              {-1, -1},
	                                              {0, -1},
	                                              {1, -1}};
	
	private static final Settings settings = Game.settings;
	
	protected int x, y;
	
	private final World world;
	
	public boolean alive, nAlive;
	
	protected int age = settings.initialAge;
	
	public Cell(int x, int y, World world) {
		this.x = x;
		this.y = y;
		this.world = world;
	}
	
	@Override
	public void render(Graphics2D g) {
		if (alive) {
			g.setColor(Color.BLACK);
			g.fillRect(x * settings.cellSize, y * settings.cellSize, settings.cellSize, settings.cellSize);
		}
	}
	
	@Override
	public void tick() {
		
		Cell[][] cells = world.getCells();
		
		int living = 0;
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				int nX = x + this.x;
				int nY = y + this.y;
				if (nX < 0) {
					nX += settings.width;
				}
				if (nX >= settings.width) {
					nX -= settings.width;
				}
				if (nY < 0) {
					nY += settings.height;
				}
				if (nY >= settings.height) {
					nY -= settings.height;
				}
				if (cells[nX][nY].alive) {
					living++;
				}
			}
		}
		if (living > 0 && alive) {
			living--;
		}
		
		if (!alive && Util.arrayContains(settings.parentsNeeded, living)) {
			nAlive = true;
		} else if (alive && !Util.arrayContains(settings.requiredPopulation, living)) {
			age--;
		} else {
			nAlive = alive;
		}
		
	}
	
	public void update() {
		if (alive && age < 1) {
			nAlive = false;
		}
		
		setState(nAlive);
	}
	
	public void setState(boolean alive) {
		if (!this.alive && alive) {
			age = settings.initialAge;
		} else if (this.alive && !alive) {
			age = 0;
		}
		this.alive = alive;
		nAlive = alive;
	}
	
}
