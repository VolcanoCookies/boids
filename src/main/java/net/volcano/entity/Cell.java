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
		for (int[] disp : DISPLACEMENTS) {
			if (disp[0] + x < cells.length && disp[0] + x > -1 &&
					disp[1] + y < cells[disp[0] + x].length && disp[1] + y > -1) {
				if (cells[x + disp[0]][y + disp[1]].alive) {
					living++;
				}
			}
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
		} else if (!alive && nAlive) {
			age = settings.initialAge;
		}
		
		alive = nAlive;
	}
	
}
