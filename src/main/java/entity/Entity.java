package entity;

import world.Chunk;

public interface Entity extends Tickable,
		Renderable {
	
	ID getId();
	
	Chunk getChunk();
	
}
