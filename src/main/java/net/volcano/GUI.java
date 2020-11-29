package net.volcano;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

public class GUI {
	
	private final Game game;
	
	private final Settings settings;
	
	private final BitmapText hud;
	
	public GUI(Game game, Settings settings, BitmapFont font) {
		this.game = game;
		this.settings = settings;
		
		hud = new BitmapText(font);
		
		hud.setSize(font.getCharSet().getRenderedSize());
		hud.setColor(ColorRGBA.White);
		hud.setText("test");
		
		game.getRootNode().attachChild(hud);
		
	}
	
	public void tick() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("FPS: ");
		stringBuilder.append(123);
		stringBuilder.append("\nTPS: ");
		stringBuilder.append(123);
		if (settings.tickPaused) {
			stringBuilder.append("\npaused");
		}
		
		hud.setText(stringBuilder.toString());
	}
	
}
