package net.volcano;

import net.volcano.world.World;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	
	public static final Settings settings = new Settings();
	
	private Thread thread;
	
	private final World world;
	
	private final GUI gui;
	
	private final Input input;
	
	public int lastFrames = 0, lastUpdates = 0;
	
	public Game() {
		
		world = new World();
		input = new Input(this);
		gui = new GUI(this, input);
		
		new Window(settings.width, settings.height, "Test", this);
		
		start();
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (settings.running) {
			double ns = 1000000000d / settings.tickRate;
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				lastUpdates = updates;
				lastFrames = frames;
				frames = 0;
				updates = 0;
			}
		}
		stop();
	}
	
	public synchronized void start() {
		settings.running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		settings.running = false;
		try {
			thread.join();
			System.out.println("Game thread stopped");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void render() {
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D guiGraphics = (Graphics2D) g.create();
		
		g.translate(settings.xOffset, settings.yOffset);
		g.scale(settings.scale, settings.scale);
		
		// Render world
		world.render((Graphics2D) g.create());
		
		// Render GUI
		gui.render(guiGraphics);
		
		g.dispose();
		bs.show();
		
	}
	
	public void tick() {
		// Tick graphics
		gui.tick();
		// Tick world
		if (!settings.tickPaused) {
			world.tick();
		}
	}
}
