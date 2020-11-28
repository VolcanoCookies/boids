package world;

import entity.Cell;
import entity.Entity;
import org.scijava.java3d.*;
import org.scijava.java3d.utils.geometry.Box;
import org.scijava.java3d.utils.geometry.Sphere;
import org.scijava.java3d.utils.image.TextureLoader;
import org.scijava.java3d.utils.universe.SimpleUniverse;
import org.scijava.vecmath.Color3f;
import org.scijava.vecmath.Point3d;
import org.scijava.vecmath.Vector3f;

import java.util.Arrays;
import java.util.LinkedList;

public class World extends SimpleUniverse {
	
	private final int DIM;
	
	public final int CELL_SIZE;
	
	private final LinkedList<Entity> entities = new LinkedList<Entity>();
	
	private final Cell[][][] cells;
	
	public World(Canvas3D canvas3D, int DIM, int CELL_SIZE) {
		super(canvas3D);
		this.DIM = DIM;
		this.CELL_SIZE = CELL_SIZE;
		
		cells = new Cell[DIM][DIM][DIM];
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				for (int z = 0; z < cells.length; z++) {
					cells[x][y][z] = new Cell(x, y, z, this);
				}
			}
		}
		
		getViewingPlatform().setNominalViewingTransform();
		
	}
	
	public synchronized void render() {
		
		BranchGroup branchGroup = new BranchGroup();
		
		Arrays.stream(cells)
				.parallel()
				.flatMap(a -> Arrays.stream(a).parallel())
				.flatMap(a -> Arrays.stream(a).parallel())
				.forEach(c -> c.render(branchGroup));
		
		branchGroup.compile();
	}
	
	public synchronized void tick() {
		
		// Update new values
		Arrays.stream(cells)
				.parallel()
				.flatMap(a -> Arrays.stream(a).parallel())
				.flatMap(a -> Arrays.stream(a).parallel())
				.forEach(Cell::update);
		
		// Calculate new values
		Arrays.stream(cells)
				.parallel()
				.flatMap(a -> Arrays.stream(a).parallel())
				.flatMap(a -> Arrays.stream(a).parallel())
				.forEach(Cell::tick);
		
	}
	
	public Cell[][][] getCells() {
		return cells;
	}
	
	public void insertPattern(int startX, int startY, Boolean[][] aliveMatrix) {
		for (int x = 0; x < aliveMatrix.length; x++) {
			for (int y = 0; y < aliveMatrix[x].length; y++) {
				//cells[y + startY][x + startX].nAlive = aliveMatrix[x][y];
			}
		}
	}
	
	public void createSceneGraph(SimpleUniverse su) {

//*** A cube to be included in the scene including its transformation group.
		
		//The colours for the Appearance.
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
		Box fBox = new Box(0.2f, 0.2f, 0.2f, fBoxApp);
		
		//The transformation group of the cube.
		Transform3D tfFBox = new Transform3D();
		tfFBox.rotY(Math.PI / 6);
		Transform3D rotationX = new Transform3D();
		rotationX.rotX(-Math.PI / 5);
		tfFBox.mul(rotationX);
		TransformGroup tgFBox = new TransformGroup(tfFBox);
		tgFBox.addChild(fBox);

//*** And a sphere with its transformation group.
		
		//The colours for the Appearance.
		Color3f ambientColourBSphere = new Color3f(0.0f, 0.4f, 0.0f);
		Color3f emissiveColourBSphere = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f diffuseColourBSphere = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f specularColourBSphere = new Color3f(0.0f, 0.8f, 0.0f);
		float shininessBSphere = 128.0f;
		
		//The Appearance for the sphere.
		Appearance bSphereApp = new Appearance();
		bSphereApp.setMaterial(new Material(ambientColourBSphere, emissiveColourBSphere,
				diffuseColourBSphere, specularColourBSphere, shininessBSphere));
		
		//The sphere.
		Sphere bSphere = new Sphere(0.4f, bSphereApp);
		
		//The transformation group for the sphere.
		Transform3D tfBSphere = new Transform3D();
		tfBSphere.setTranslation(new Vector3f(0.0f, 2.7f, -10.5f));
		TransformGroup tgBSphere = new TransformGroup(tfBSphere);
		tgBSphere.addChild(bSphere);
		
		//Add everything to the scene.
		BranchGroup theScene = new BranchGroup();
		theScene.addChild(tgFBox);
		theScene.addChild(tgBSphere);
		
		//The bounding region for the background.
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
		//Load the background image.
		TextureLoader textureLoad = new TextureLoader("darkclouds.jpg", null);
		//Define the image as the background and add it to the scene.
		Background bgImage = new Background(textureLoad.getImage());
		bgImage.setApplicationBounds(bounds);
		theScene.addChild(bgImage);
		
		theScene.compile();
		
		//Add the scene to the universe.
		su.addBranchGraph(theScene);
		
	}
	
}
