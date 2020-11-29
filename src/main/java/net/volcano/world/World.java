package net.volcano.world;

import net.volcano.Game;
import net.volcano.entity.Cell;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class World {
	
	public final int DIM;
	
	public final float CELL_SIZE;
	
	private final Cell[][][] cells;
	
	public final Game game;
	
	public World(Game game, int DIM, float CELL_SIZE) {
		this.game = game;
		this.DIM = DIM;
		this.CELL_SIZE = CELL_SIZE;
		
		cells = new Cell[DIM][DIM][DIM];
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				for (int z = 0; z < cells.length; z++) {
					cells[x][y][z] = new Cell(x, y, z, 0.2f, this);
					if (x < DIM - 4 && x > 4 &&
							y < DIM - 4 && y > 4 &&
							z < DIM - 4 && z > 4) {
						Cell cell = cells[x][y][z];
						cell.nAlive = true;
					}
				}
			}
		}
		
	}
	
	public synchronized void tick() {
		
		// Update new values
		Arrays.stream(cells)
				.parallel()
				.flatMap(a -> Arrays.stream(a).parallel())
				.flatMap(a -> Arrays.stream(a).parallel())
				.forEach(Cell::update);
		
		// Calculate new values
		Arrays.stream(cells)
				.parallel()
				.flatMap(a -> Arrays.stream(a).parallel())
				.flatMap(a -> Arrays.stream(a).parallel())
				.forEach(Cell::tick);
		
	}
	
	public Cell[][][] getCells() {
		return cells;
	}
	
	public void insertPattern(int startX, int startY, Boolean[][] aliveMatrix) {
		for (int x = 0; x < aliveMatrix.length; x++) {
			for (int y = 0; y < aliveMatrix[x].length; y++) {
				//cells[y + startY][x + startX].nAlive = aliveMatrix[x][y];
			}
		}
	}
	
	public List<Cell> listCells() {
		return Arrays.stream(cells)
				.flatMap(Arrays::stream)
				.flatMap(Arrays::stream)
				.collect(Collectors.toList());
	}
	
}
