package net.volcano;

public class Settings {
	
	public int tickRate = 64;
	
	public boolean tickPaused = false;
	
	public boolean running = true;
	
	public int width = 800, height = 600;
	
	public int cellSize = 8;
	
	// Simulation settings
	
	public int[] parentsNeeded = {3};
	
	public int[] requiredPopulation = {2, 3};
	
	public int initialAge = 1;
	
	// Rendering
	
	public double scale = 1.0;
	
	public int xOffset = 0, yOffset = 0;
	
}
