package world;

import entity.Entity;
import entity.Renderable;
import entity.Tickable;

import java.awt.*;
import java.util.LinkedList;

public class World implements Tickable,
		Renderable {
	
	private final int WIDTH, HEIGHT;
	
	private final int CHUNK_SIZE;
	
	private final int CHUNK_WIDTH, CHUNK_HEIGHT;
	
	private final LinkedList<Entity> entities = new LinkedList<Entity>();
	
	private final Chunk[][] CHUNKS;
	
	public World(int WIDTH, int HEIGHT, int CHUNK_SIZE) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.CHUNK_SIZE = CHUNK_SIZE;
		CHUNK_WIDTH = (int) Math.ceil((double) WIDTH / CHUNK_SIZE);
		CHUNK_HEIGHT = (int) Math.ceil((double) HEIGHT / CHUNK_SIZE);
		
		CHUNKS = new Chunk[CHUNK_WIDTH][CHUNK_HEIGHT];
		for (int x = 0; x < CHUNKS.length; x++) {
			for (int y = 0; y < CHUNKS[x].length; y++) {
				CHUNKS[x][y] = new Chunk();
			}
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		entities.parallelStream()
				.forEach(e -> e.render((Graphics2D) g.create()));
	}
	
	@Override
	public void tick() {
		entities.parallelStream()
				.forEach(Entity::tick);
	}
}
