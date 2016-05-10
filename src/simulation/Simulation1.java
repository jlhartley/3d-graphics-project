package simulation;

import static logging.Logger.log;
import static math.MathUtils.getSphereRadius;
import static physics.PhysicsUtils.getStableOrbitalVelocity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.Entity;
import entities.celestial.Planet;
import entities.celestial.Star;
import input.Key;
import input.MouseButton;
import io.FileUtils;
import math.MathUtils;
import math.geometry.Matrix4f;
import math.geometry.Vector2f;
import math.geometry.Vector3f;
import model.Model;
import model.Models;
import physics.Integrator;
import render.Renderer;
import save.PlanetSaveData;

public class Simulation1 extends Simulation {
	
	public static void main(String[] args) {
		new Simulation1().run();
	}
	
	// Keyboard Input

	@Override
	public void onKeyPressed(Key key) {
		super.onKeyPressed(key);
		if (key == Key.ONE) {
			planetToAdd = new Planet(rockModel);
			float mass = 1000000;
			planetToAdd.setMass(mass);
			double volume = mass / PLANET_DENSITY;
			planetToAdd.setScale((float) getSphereRadius(volume));
		}
	}
	
	// Mouse Input
	
	@Override
	public void onCursorPositionChanged(Vector2f cursorPosition) {
		if (planetToAdd != null) {
			int width = window.getCanvas().getWidth();
			int height = window.getCanvas().getHeight();
			Matrix4f viewMatrix = camera.getViewMatrix();
			Matrix4f viewInverse = viewMatrix.invert();
			Matrix4f projMatrix = renderer.getProjectionMatrix();
			Matrix4f projInverse = projMatrix.invert();
			
			Vector2f ndc = MathUtils.cursorPositionToNDC(cursorPosition, width, height);
			Vector3f pos = new Vector3f(ndc.x, ndc.y, -1);
			
			pos.multiply(projInverse, 1);
			
			pos.scale(300);
			
			
			pos.multiply(viewInverse, 1);
			
			
			planetToAdd.setPosition(pos);
		}
	}
	
	@Override
	public void onMouseDown(MouseButton mouseButton) {
		super.onMouseDown(mouseButton);
		if (mouseButton == MouseButton.LEFT && planetToAdd != null) {
			// If autoVelocity is true, then override current velocity with a stable one.
			if (autoVelocity) {
				planetToAdd.setVelocity(getStableOrbitalVelocity(planetToAdd, sun));
			}
			planets.add(planetToAdd);
			planetToAdd = null;
		}
	}
	
	
	// UI Callbacks

	@Override
	public void onAddPlanet(Vector3f velocity, boolean autoVelocity, float mass) {
		planetToAdd = new Planet(rockModel);
		planetToAdd.setMass(mass);
		double volume = mass / PLANET_DENSITY;
		planetToAdd.setScale((float) getSphereRadius(volume));
		planetToAdd.setVelocity(velocity);
		this.autoVelocity = autoVelocity;
	}
	
	
	// Constants
	
	private static final String TITLE = "Orbitator";
	
	
	private static final double MIN_ORBITAL_RADIUS = 100;
	private static final double MAX_ORBITAL_RADIUS = 200;
	
	private static final double MAX_PLANET_Y = 10;
	private static final double MIN_PLANET_Y = -10;
	
	private static final double MIN_PLANET_MASS = 0.1;
	private static final double MAX_PLANET_MASS = 15;
	
	private static final double PLANET_DENSITY = 5;
	
	private static final double STAR_DENSITY = 1;
	
	private static final int PLANET_COUNT = 250;
	
	
	private static final String DEFAULT_SAVE_PATH = "autosave.json";
	
	
	// Main objects
	
	private Model sphereModel = Models.getIcosphereModel();
	private Model rockModel = Models.getRockModel();
	
	// Sun is at the origin with no velocity
	private Star sun = new Star(sphereModel);
	
	private List<Planet> planets = new ArrayList<>();
	
	
	private Planet planetToAdd;
	
	private boolean autoVelocity;
	
	private Integrator integrator;
	
	
	private void initPlanets() {
		
		planets.clear();
		
		for (int i = 0; i < PLANET_COUNT; i++) {
			
			Planet planet = new Planet(rockModel);
			
			// Initial independent values
			double mass = getPlanetMass();
			Vector3f position = getPlanetPosition();
			
			planet.setMass((float) mass);
			planet.setPosition(position);
			
			// Dependent values
			double volume = mass / PLANET_DENSITY;
			double scale = getSphereRadius(volume);
			planet.setScale((float) scale);
			
			Vector3f velocity = getStableOrbitalVelocity(planet, sun);
			planet.setVelocity(velocity);
			
			planets.add(planet);
		}
		
	}
	
	// Generate a random mass
	private double getPlanetMass() {
		return MathUtils.randRange(MIN_PLANET_MASS, MAX_PLANET_MASS);
	}
	
	// Get random planet position as a point on a circle
	// with a random radius, on the x-z plane. Also add
	// some random y variation.
	private Vector3f getPlanetPosition() {
		Vector3f position = new Vector3f();
		// Random angle
		double theta = Math.random() * 2 * Math.PI;
		double r = MathUtils.randRange(MIN_ORBITAL_RADIUS, MAX_ORBITAL_RADIUS);
		position.x = (float) (r * Math.cos(theta));
		position.z = (float) (r * Math.sin(theta));
		// Introduce y variation
		position.y = (float) MathUtils.randRange(MIN_PLANET_Y, MAX_PLANET_Y);
		return position;
	}
	
	
	
	private void initSun() {
		double mass = 1E6;
		double volume = mass / STAR_DENSITY;
		double scale = getSphereRadius(volume);
		sun.setMass((float) mass);
		sun.setScale((float) scale);
	}
	
	public Simulation1() {
		window.setTitle(TITLE);
		initSun();
		initPlanets();
		integrator = new Integrator(planets, sun);
	}
	
	
	
	@Override
	protected void logic(float deltaTime) {
		
		// Integration
		integrator.symplecticEuler2(deltaTime);
		
	}
	
	

	@Override
	protected void render(Renderer renderer) {
		
		// Render the sun model without lighting so that
		// it will actually appear
		renderer.disableLighting();
		renderer.render(sun, camera);
		
		// Enable lighting and set the sun as the light source so
		// that the planets are lit by subsequent render calls
		renderer.enableLighting();
		renderer.setLightSource(sun);
		
		for (Entity planet : planets) {
			renderer.render(planet, camera);
		}
		
		if (planetToAdd != null) {
			renderer.render(planetToAdd, camera);
		}
		
	}
	
	
	
	private static Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	
	@Override
	protected void close() {
		log("Close");
		onSave(DEFAULT_SAVE_PATH);
	}

	@Override
	public void onSave(String path) {
		paused = true;
		// Have to sleep for a while to ensure the logic thread has paused
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<PlanetSaveData> saveDataList = new ArrayList<>(planets.size());
		for (Planet planet : planets) {
			saveDataList.add(planet.getPlanetSaveData());
		}
		String json = gson.toJson(saveDataList);
		FileUtils.writeToFile(path, json);
		paused = false;
	}

	@Override
	public void onOpen(String path) {
		paused = true;
		// Have to sleep for a while to ensure the logic thread has paused
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		planets.clear();
		String json = FileUtils.getFileContents(path);
		PlanetSaveData[] saveDataArray = gson.fromJson(json, PlanetSaveData[].class);
		for (PlanetSaveData saveData : saveDataArray) {
			Planet planet = new Planet(rockModel, saveData);
			// Manually restore scale based on mass
			double volume = saveData.mass / PLANET_DENSITY;
			double scale = getSphereRadius(volume);
			planet.setScale((float) scale);
			planets.add(planet);
		}
		paused = false;
	}

	@Override
	public void onNew() {
		paused = true;
		// Have to sleep for a while to ensure the logic thread has paused
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		initPlanets();
		paused = false;
	}
	
	
}
