package net.volcano;

import net.volcano.entity.Cell;
import net.volcano.world.World;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas {
	
	public static final Settings settings = new Settings();
	
	public static final Statistics statistics = new Statistics();
	
	public static int mouseX, mouseY;
	
	public static Cell hoveringCell;
	
	private Thread thread;
	
	private final World world;
	
	private final GUI gui;
	
	private final Input input;
	
	public int lastFrames = 0, lastUpdates = 0;
	
	private Thread tickThread;
	
	private Thread renderThread;
	
	public Game() {
		
		world = new World();
		input = new Input(this);
		gui = new GUI(this, world);
		
		new Window(settings.width, settings.height, "Test", this);
		
		start();
	}
	
	public synchronized void start() {
		settings.running = true;
		
		tickThread = new Thread(() -> {
			
			long lastTime = System.nanoTime();
			double delta = 0;
			long timer = System.currentTimeMillis();
			int updates = 0;
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
				
				if (System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					statistics.ticksPerSecond = updates;
					updates = 0;
				}
			}
			stop();
			
		});
		tickThread.start();
		
		renderThread = new Thread(() -> {
			
			long timer = System.currentTimeMillis();
			int frames = 0;
			while (settings.running) {
				
				render();
				frames++;
				
				if (System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					statistics.framesPerSecond = frames;
					frames = 0;
				}
			}
			stop();
			
		});
		renderThread.start();
	}
	
	public synchronized void stop() {
		settings.running = false;
		try {
			tickThread.join();
			renderThread.join();
			System.out.println("Game threads stopped");
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
