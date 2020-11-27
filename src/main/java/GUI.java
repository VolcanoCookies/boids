import entity.Renderable;
import entity.Tickable;

import java.awt.*;

public class GUI implements Tickable,
		Renderable {
	
	private final Game game;
	
	private final Settings settings;
	
	private String information = "";
	
	private int xOffset = 0;
	
	public GUI(Game game, Settings settings) {
		this.game = game;
		this.settings = settings;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawString(information, (float) g.getDeviceConfiguration().getBounds().getMaxX(), 2);
	}
	
	@Override
	public void tick() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("FPS: ");
		stringBuilder.append(game.lastFrames);
		stringBuilder.append("\nTicks: ");
		stringBuilder.append(game.lastUpdates);
		if (settings.tickPaused) {
			stringBuilder.append("\nTicks paused");
		}
		
		information = stringBuilder.toString();
	}
	
}
