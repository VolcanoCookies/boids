package net.volcano.entity;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import net.volcano.world.World;

public class Cell extends Geometry implements Tickable {
	
	protected final int x, y, z;
	
	private final World world;
	
	public boolean alive = false;
	
	public boolean nAlive = false;
	
	protected float size;
	
	protected int initialAge = 5;
	
	protected int age = 0;
	
	protected ColorRGBA color;
	
	protected ColorRGBA[] colors = {
			new ColorRGBA(0.2f, 0, 0, 1),
			new ColorRGBA(0.4f, 0, 0, 1),
			new ColorRGBA(0.6f, 0, 0, 1),
			new ColorRGBA(0.8f, 0, 0, 1),
			new ColorRGBA(1, 0, 0, 1)
	};
	
	public Cell(int x, int y, int z, float size, World world) {
		super("Cell", new Box(size, size, size));
		this.size = world.CELL_SIZE;
		setLocalTranslation(new Vector3f(x, y, z).multLocal(size / 2.5f));
		setLocalScale(size);
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		
		float distanceToCenter = (float) Math.sqrt(Math.pow(x - world.DIM / 2f, 2) + Math.pow(y - world.DIM / 2f, 2) + Math.pow(z - world.DIM / 2f, 2));
		float radius = (float) Math.sqrt(Math.pow(-world.DIM / 2f, 2) + Math.pow(-world.DIM / 2f, 2) + Math.pow(-world.DIM / 2f, 2));
		
		float diff = distanceToCenter / radius;
		
		if (diff < 0.2f) {
			color = new ColorRGBA().interpolateLocal(ColorRGBA.Yellow, ColorRGBA.Orange, diff * 2);
		} else if (diff < 0.6f) {
			color = new ColorRGBA().interpolateLocal(ColorRGBA.Orange, ColorRGBA.Cyan, (diff - 0.2f) * 2);
		} else {
			color = new ColorRGBA().interpolateLocal(ColorRGBA.Cyan, ColorRGBA.Blue, (diff - 0.6f) * 2);
		}
		
		Material material = new Material(world.game.getAssetManager(),
				"Common/MatDefs/Light/Lighting.j3md");
		
		material.setBoolean("UseMaterialColors", true);
		material.setFloat("Shininess", 64f);
		setMaterial(material);
		
		setColors(color);
		
		setCullHint(CullHint.Always);
		
		setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
		
	}
	
	private void setColors(ColorRGBA color) {
		getMaterial().setColor("Diffuse", color);
		getMaterial().setColor("Ambient", color);
		getMaterial().setColor("Specular", color);
	}
	
	@Override
	public void tick() {
		
		int living = getLivingMooreNeighbours();
		
		if (!alive && living == 4) {
			nAlive = true;
		} else if (alive && living != 4) {
			age--;
		} else {
			nAlive = alive;
		}
		
		if (age > 6 || age < 1) {
			//setColors(ColorRGBA.Black);
		} else {
			//setColors(colors[age - 1]);
		}
		
		if (getLivingNeumanNeighbours() == 6) {
			setCullHint(CullHint.Always);
		}
		
	}
	
	public synchronized void update() {
		synchronized (this) {
			
			if (alive != nAlive) {
				if (nAlive) {
					age = initialAge;
				} else {
					age = 0;
				}
			}
			
			if (alive && age < 1) {
				nAlive = false;
				age = 0;
			}
			
			alive = nAlive;
			
			setCullHint(alive ? CullHint.Inherit : CullHint.Always);
		}
	}
	
	private int[][] DIRS = {
			{0, 0, 1},
			{0, 0, -1},
			{0, 1, 0},
			{0, -1, 0},
			{1, 0, 0},
			{-1, 0, 0}
	};
	
	public int getLivingNeumanNeighbours() {
		
		Cell[][][] cells = world.getCells();
		
		int living = 0;
		
		for (int[] dir : DIRS) {
			if (dir[0] + x < cells.length && dir[0] + x > -1 &&
					dir[1] + y < cells[dir[0] + x].length && dir[1] + y > -1 &&
					dir[2] + z < cells[dir[0] + x][dir[1] + y].length && dir[2] + z > -1) {
				Cell cell = cells[dir[0] + x][dir[1] + y][dir[2] + z];
				if (cell.alive) {
					living++;
				}
			}
		}
		
		return living;
	}
	
	public int getLivingMooreNeighbours() {
		
		Cell[][][] cells = world.getCells();
		
		int living = 0;
		for (int x = -1; x < 2; x += 2) {
			for (int y = -1; y < 2; y += 2) {
				for (int z = -1; z < 2; z += 2) {
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
		
		return living;
		
	}
	
}
