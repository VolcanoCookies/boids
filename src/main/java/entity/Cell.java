package entity;

import org.scijava.java3d.*;
import org.scijava.java3d.utils.geometry.Box;
import org.scijava.vecmath.Color3f;
import org.scijava.vecmath.Vector3d;
import world.World;

import java.awt.*;

public class Cell implements Tickable,
		Renderable {
	
	protected int x, y, z;
	
	private final World world;
	
	private final int[][] DISPLACEMENTS = {{1, 0},
	                                       {1, 1},
	                                       {0, 1},
	                                       {-1, 1},
	                                       {-1, 0},
	                                       {-1, -1},
	                                       {0, -1},
	                                       {1, -1}};
	
	public boolean alive = false;
	
	public boolean nAlive = false;
	
	protected int size;
	
	protected int age = 0;
	
	public Color color = Color.BLACK;
	
	public Cell(int x, int y, int z, World world) {
		this.x = x;
		this.y = y;
		this.world = world;
		size = world.CELL_SIZE;
	}
	
	@Override
	public void render(BranchGroup branchGroup) {
		
		if (alive) {
			Color3f ambientColourFBox = new Color3f(0.4f, 0.0f, 0.0f);
			Color3f emissiveColourFBox = new Color3f(0.0f, 0.0f, 0.0f);
			Color3f diffuseColourFBox = new Color3f(0.6f, 0.0f, 0.0f);
			Color3f specularColourFBox = new Color3f(0.8f, 0.0f, 0.0f);
			float shininessFBox = 10.0f;
			
			//The Appearance for the cube.
			Appearance fBoxApp = new Appearance();
			fBoxApp.setMaterial(new Material(ambientColourFBox, emissiveColourFBox,
					diffuseColourFBox, specularColourFBox, shininessFBox));
			
			//The cube.
			Box fBox = new Box(1f, 1f, 1f, fBoxApp);
			
			Transform3D transform3D = new Transform3D();
			transform3D.setTranslation(new Vector3d(x, y, z));
			transform3D.setScale(size);
			
			TransformGroup transformGroup = new TransformGroup(transform3D);
			transformGroup.addChild(fBox);
			
			branchGroup.addChild(transformGroup);
		}
		
	}
	
	@Override
	public void tick() {
		
		Cell[][][] cells = world.getCells();
		
		int living = 0;
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				for (int z = -1; z < 2; z++) {
					if (x + this.x < cells.length && x + this.x > -1 &&
							y + this.y < cells[x + this.x].length && y + this.y > -1 &&
							z + this.z < cells[x + this.x][y + this.y].length && z + this.z > -1) {
						Cell cell = cells[x + this.x][y + this.y][z + this.z];
						if (cell.alive) {
							living++;
						}
					}
				}
			}
		}
		
		if (!alive && living == 3) {
			nAlive = true;
		} else if (alive && (living < 2 || living > 3)) {
			nAlive = false;
		} else {
			nAlive = alive;
		}
		
	}
	
	public synchronized void update() {
		synchronized (this) {
			if (alive && nAlive) {
				age++;
			} else {
				age = 0;
			}
			alive = nAlive;
		}
	}
	
}
