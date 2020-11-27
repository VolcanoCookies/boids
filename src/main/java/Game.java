import world.World;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	
	private final Settings settings;
	
	private Thread thread;
	
	private final World world;
	
	private final GUI gui;
	
	public int lastFrames = 0, lastUpdates = 0;
	
	public Game() {
		settings = new Settings();
		world = new World(1024, 1024, 64);
		gui = new GUI(this, settings);
		new Window(settings.width, settings.height, "Test", this);
		start();
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 30.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (settings.running) {
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
	
	public void render() {
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Render GUI
		gui.render((Graphics2D) g.create());
		
		// Render world
		world.render((Graphics2D) g.create());
		
		g.dispose();
		bs.show();
		
	}
	
	public void tick() {
		// Tick graphics
		gui.tick();
		// Tick world
		world.tick();
	}
}
