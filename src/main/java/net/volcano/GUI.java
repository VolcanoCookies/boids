package net.volcano;

import net.volcano.world.World;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class GUI {
	
	private static final Settings settings = Game.settings;
	
	private static final Statistics statistics = Game.statistics;
	
	private final Game game;
	
	private final World world;
	
	private String[] information = new String[0];
	
	private int worldX, worldY;
	
	public GUI(Game game, World world) {
		this.game = game;
		this.world = world;
	}
	
	public void render(Graphics2D g) {
		for (int i = 0; i < information.length; i++) {
			Rectangle2D bounds = g.getFont().getStringBounds(information[i], g.getFontRenderContext());
			g.setColor(Color.WHITE);
			g.fillRect(3, 1 + 14 * (i), (int) bounds.getWidth(), (int) bounds.getHeight());
			g.setColor(Color.BLACK);
			g.drawString(information[i], 3, 1 + 14 * (i + 1));
		}
		
		if (settings.tickPaused) {
			
			g.translate(settings.xOffset, settings.yOffset);
			g.scale(settings.scale, settings.scale);
			
			g.setColor(Color.GREEN);
			
			g.fillRect(worldX * settings.cellSize, worldY * settings.cellSize, settings.cellSize, settings.cellSize);
			
			/*g.fillRect(input.getPrevX() - settings.xOffset,
					input.getPrevY() - settings.yOffset,
					settings.cellSize,
					settings.cellSize);*/
		}
		
	}
	
	public void tick() {
		List<String> list = new ArrayList<>();
		list.add("FPS: " + statistics.framesPerSecond);
		list.add("TPS: " + statistics.ticksPerSecond);
		if (settings.tickPaused) {
			list.add("Paused");
		}
		
		worldX = Math.floorDiv((int) ((Game.mouseX - settings.xOffset) / settings.scale), settings.cellSize);
		worldY = Math.floorDiv((int) ((Game.mouseY - settings.yOffset) / settings.scale), settings.cellSize);
		
		list.add("X: " + worldX);
		list.add("Y: " + worldY);
		
		if (worldX >= 0 && worldX < settings.width &&
				worldY >= 0 && worldY < settings.height) {
			Game.hoveringCell = world.getCells()[worldX][worldY];
		} else {
			Game.hoveringCell = null;
		}
		
		information = list.toArray(new String[0]);
	}
	
}
