package net.volcano;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class GUI {
	
	private static final Settings settings = Game.settings;
	
	private final Game game;
	
	private final Input input;
	
	private String[] information = new String[0];
	
	public GUI(Game game, Input input) {
		this.game = game;
		this.input = input;
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
			g.fillRect(input.getPrevX(),
					input.getPrevY(),
					settings.cellSize,
					settings.cellSize);
		}
		
	}
	
	public void tick() {
		List<String> list = new ArrayList<>();
		list.add("FPS: " + game.lastFrames);
		list.add("TPS: " + game.lastUpdates);
		if (settings.tickPaused) {
			list.add("Paused");
		}
		
		information = list.toArray(new String[0]);
	}
	
}
