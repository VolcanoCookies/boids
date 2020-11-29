package net.volcano;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import net.volcano.entity.Cell;
import net.volcano.world.World;

public class Game extends SimpleApplication {
	
	private final Settings settings;
	
	private Thread thread;
	
	private World world;
	
	private final GUI gui;
	
	public int lastFrames = 0, lastUpdates = 0;
	
	public Game() {
		settings = new Settings();
		
		start();
		
		gui = new GUI(this, settings, guiFont);
	}
	
	private final SpotLight flashlight = new SpotLight();
	
	@Override
	public void simpleInitApp() {
		
		world = new World(this, 41, 0.1f);
		
		viewPort.setBackgroundColor(ColorRGBA.DarkGray);
		
		flashlight.setColor(ColorRGBA.White.mult(0.7f));
		//rootNode.addLight(flashlight);
		
		AmbientLight ambientLight = new AmbientLight(ColorRGBA.White.mult(0.7f));
		rootNode.addLight(ambientLight);
		
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(ColorRGBA.White);
		directionalLight.setDirection(new Vector3f(0.66f, 0.33f, 1f));
		rootNode.addLight(directionalLight);
		
		FilterPostProcessor processor = new FilterPostProcessor(assetManager);
		int numSamples = getContext().getSettings().getSamples();
		if (numSamples > 0) {
			processor.setNumSamples(getContext().getSettings().getSamples());
		}
		
		//addOutline(geometry);
		
		viewPort.addProcessor(processor);
		
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		SSAOFilter ssaoFilter = new SSAOFilter(12f, 12f, 0.33f, 0.15f);
		//fpp.addFilter(ssaoFilter);
		viewPort.addProcessor(fpp);
		
		int degree = 4;
		renderer.setDefaultAnisotropicFilter(degree);
		
		for (Cell cell : world.listCells()) {
			rootNode.attachChild(cell);
		}
		
		inputManager.addMapping("Do tick",
				new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
			if (isPressed) {
				world.tick();
			}
		}, "Do tick");
		
	}
	
	@Override
	public synchronized void stop() {
		settings.running = false;
		stop(false);
	}
	
	float time = 0;
	
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		flashlight.setDirection(cam.getDirection());
		flashlight.setPosition(cam.getLocation());
		time += tpf;
		if (time > 0.1) {
			time = 0;
			world.tick();
		}
	}
	
	public void tick() {
		
		// Tick graphics
		gui.tick();
		// Tick net.volcano.world
		world.tick();
		
	}
	
	public void addOutline(Spatial spatial) {
		if (spatial instanceof Node) {
			
			Node node = (Node) spatial;
			
			for (Spatial s : node.getChildren()) {
				addOutline(s);
			}
			
		} else if (spatial instanceof Geometry) {
			
			Geometry geometry = (Geometry) spatial;
			Material material = geometry.getMaterial();
			
			if (material.getMaterialDef().getName().equals("Phong Lighting")) {
				
				Texture texture = assetManager.loadTexture("Textures/ColorRamp/toon.png");
				material.setTexture("ColorRamp", texture);
				material.setBoolean("UseMaterialColors", true);
				material.setColor("Specular", ColorRGBA.Black);
				material.setColor("Diffuse", ColorRGBA.White);
				material.setBoolean("VertexLighting", true);
			}
			
		}
	}
	
}
