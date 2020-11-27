package world;

import entity.Entity;

import java.util.Arrays;
import java.util.LinkedList;

public class Chunk {
	
	private final LinkedList<Entity> entities = new LinkedList<Entity>();
	
	public synchronized void addEntity(Entity... entities) {
		synchronized (this.entities) {
			this.entities.addAll(Arrays.asList(entities));
		}
	}
	
	public synchronized void removeEntity(Entity... entities) {
		synchronized (this.entities) {
			this.entities.removeAll(Arrays.asList(entities));
		}
	}
	
}
