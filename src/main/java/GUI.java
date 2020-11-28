import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
	
	private final Game game;
	
	private final Settings settings;
	
	private String[] information = new String[0];
	
	private int xOffset = 0;
	
	public GUI(Game game, Settings settings) {
		this.game = game;
		this.settings = settings;
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < information.length; i++) {
			g.drawString(information[i], 2, 14 * (i + 1));
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
