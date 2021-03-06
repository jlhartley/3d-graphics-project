package testing;

import static logging.Logger.log;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import camera.AbsoluteControls;
import camera.Camera;
import camera.CameraControls;
import camera.RelativeControls;
import entities.Entity;
import entities.celestial.CelestialEntity;
import entities.celestial.Planet;
import entities.celestial.Star;
import input.Key;
import input.MouseButton;
import io.FileUtils;
import math.MathUtils;
import math.geometry.Vector2f;
import math.geometry.Vector3f;
import model.Model;
import model.Models;
import physics.Constants;
import render.Renderer;
import save.PlanetSaveData;
import ui.UIWindow;

public class Tester5 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester5().run();
	}
	
	// UI Input
	
	@Override
	public void onCameraControlTypeSet(boolean relative) {
		if (relative) {
			log("Relative Camera Controls");
			cameraControls = relativeCameraControls;
		} else {
			log("Absolute Camera Controls");
			cameraControls = absoluteCameraControls;
		}
	}
	
	@Override
	public void onCameraPositionSet(Vector3f newPosition) {
		camera.setPosition(newPosition);
	}
	
	@Override
	public void onCameraRotationSet(Vector3f newRotation) {
		camera.setRotation(newRotation);
	}
	
	@Override
	public void onTimeMultiplierSet(double timeMultiplier) {
		this.timeMultiplier = timeMultiplier;
	}
	
	// Keyboard Input

	@Override
	public void onKeyPressed(Key key) {
		if (key == Key.P) {
			paused = !paused;
		} else if (key == Key.R) {
			resetCamera();
			//initPlanets();
		} else if (key == Key.U) {
			// Place the camera directly up in y
			camera.setPosition(0, (float) Math.sqrt(350*350 + 350*350), 0);
			// Point camera straight down
			camera.setPitch(90);
			camera.setYaw(0);
		}
	}
	
	@Override
	public void onKeyReleased(Key key) {
		
	}
	
	
	// Mouse Input
	
	@Override
	public void onMouseDown(MouseButton mouseButton) {
		switch (mouseButton) {
		case LEFT:
			log("Left mouse button down.");
			break;
		case RIGHT:
			log("Right mouse button down.");
			cameraControls.onMouseDown();
			break;
		case MIDDLE:
			log("Middle mouse button down.");
			break;
		}
	}

	@Override
	public void onMouseUp(MouseButton mouseButton) {
		switch (mouseButton) {
		case LEFT:
			log("Left mouse button up.");
			break;
		case RIGHT:
			log("Right mouse button up.");
			cameraControls.onMouseUp();
			break;
		case MIDDLE:
			log("Middle mouse button up.");
			break;
		}
	}
	
	@Override
	public void onCursorPositionChanged(Vector2f cursorPosition) {
		
	}
	
	
	
	// Constants
	
	private static final double MIN_ORBITAL_RADIUS = 100;
	private static final double MAX_ORBITAL_RADIUS = 200;
	
	private static final double MAX_PLANET_Y = 10;
	private static final double MIN_PLANET_Y = -10;
	
	private static final double MIN_PLANET_MASS = 0.1;
	private static final double MAX_PLANET_MASS = 15;
	
	private static final double PLANET_DENSITY = 5;
	
	private static final double STAR_DENSITY = 1;
	
	private static final int PLANET_COUNT = 250;
	
	
	
	// State
	
	// Scales deltaTime
	private double timeMultiplier = 1;
	
	// Pause all logic other than camera movement
	boolean paused = false;
	
	
	// Main objects
	
	Camera camera = new Camera();
	
	RelativeControls relativeCameraControls = new RelativeControls(camera, window);
	AbsoluteControls absoluteCameraControls = new AbsoluteControls(camera, window);
	
	CameraControls cameraControls = relativeCameraControls;
	
	Model sphereModel = Models.getIcosphereModel();
	Model rockModel = Models.getRockModel();
	
	// Sun is at the origin with no velocity
	Star sun = new Star(sphereModel);
	
	List<Planet> planets = new ArrayList<>();
	
	
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
			double scale = getSphereRadius(mass, PLANET_DENSITY);
			Vector3f velocity = getOrbitalVelocity(planet, sun);
			
			planet.setScale((float) scale);
			planet.setVelocity(velocity);
			
			//planet.setAcceleration(new Vector3f(position.x * 1000, position.y * 1000, 900));
			
			planets.add(planet);
		}
		
		/*
		String json = FileUtils.getFileContents("planets.json");
		PlanetSaveData[] saveDataArray = gson.fromJson(json, PlanetSaveData[].class);
		for (PlanetSaveData saveData : saveDataArray) {
			Planet planet = new Planet(rockModel, saveData);
			double scale = getSphereRadius(saveData.mass, PLANET_DENSITY);
			planet.setScale((float) scale);
			planets.add(planet);
		}
		*/
		
	}
	
	// Generate a random mass
	private double getPlanetMass() {
		return MathUtils.randRange(MIN_PLANET_MASS, MAX_PLANET_MASS);
	}
	
	// Calculate the radius from the mass
	private double getSphereRadius(double mass, double density) {
		double volume = mass / density;
		double radius = Math.cbrt((3 * volume) / (4 * Math.PI));
		return radius;
	}
	
	// Get random planet position as a point on a circle
	// with a random radius, on the x-z plane. Also add
	// some random y variation.
	private Vector3f getPlanetPosition() {
		Vector3f position = new Vector3f();
		double theta = Math.random() * 2 * Math.PI;
		double r = MathUtils.randRange(MIN_ORBITAL_RADIUS, MAX_ORBITAL_RADIUS);
		position.x = (float) (r * Math.cos(theta));
		position.z = (float) (r * Math.sin(theta));
		// Introduce y variation
		position.y = (float) MathUtils.randRange(MIN_PLANET_Y, MAX_PLANET_Y);
		return position;
	}
	
	// Get a stable orbital velocity for a circular orbit of entity1 around entity2.
	// This is assuming entity2 is stationary, and both entities lie in the same plane.
	private Vector3f getOrbitalVelocity(CelestialEntity entity1, CelestialEntity entity2) {
		Vector3f p1 = entity1.getPosition();
		Vector3f p2 = entity2.getPosition();
		float m1 = entity1.getMass();
		float m2 = entity2.getMass();
		Vector3f r = Vector3f.sub(p2, p1);
		
		Vector3f velocity = new Vector3f();
		// Perpendicular vector in the x-z plane
		velocity.x = -r.z;
		velocity.z = r.x;
		// Calculate velocity magnitude (speed)
		double distance = r.magnitude();
		double speed = Math.sqrt(Constants.G  * (m1 + m2) / distance);
		velocity.setMagnitude((float) speed);
		
		return velocity;
	}
	
	
	private void initSun() {
		double mass = 1E6;
		double scale = getSphereRadius(mass, STAR_DENSITY);
		sun.setMass((float) mass);
		sun.setScale((float) scale);
	}
	
	private void resetCamera() {
		camera.setPosition(0, 350, 350);
		// Point camera downwards at 45 degrees
		camera.setPitch(45);
		camera.setYaw(0);
	}
	
	public Tester5() {
		resetCamera();
		initSun();
		initPlanets();
	}

	
	@Override
	protected void logic(float deltaTime) {
		
		cameraControls.move(deltaTime);
		
		if (paused) {
			return;
		}
		
		deltaTime *= timeMultiplier;
		
		// Euler integration
		
		for (int i = 0; i < planets.size(); i++) {
			Planet planet1 = planets.get(i);
			Vector3f acceleration1 = planet1.getAcceleration();
			
			// Can't zero here, because we would clear our calculations so far
			//acceleration1.zero();
			
			for (int j = i + 1; j < planets.size(); j++) {
				Planet planet2 = planets.get(j);
				Vector3f acceleration2 = planet2.getAcceleration();
				
				Vector3f force = planet1.forceTo(planet2);
				acceleration1.add(force);
				acceleration2.add(force.negate());
			}
			// Add the vector for the sun
			acceleration1.add(planet1.forceTo(sun));
			
			// Divide force vector by mass to give acceleration vector
			float mass = planet1.getMass();
			acceleration1.scale(1 / mass);
			planet1.updateVelocity(acceleration1, deltaTime);
			
			// Clear ready for next frame computation
			acceleration1.zero();
		}
		
		
		// Wait until all planet force calculations (dependent on position)
		// are done, before updating the position of each planet
		for (Planet planet : planets) {
			planet.updatePosition(deltaTime);
			// Rotate planet with time - a full rotation in 2 seconds
			planet.rotateY(180, deltaTime);
		}
		
		
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
		
	}
	
	@Override
	protected void updateUI(UIWindow window) {
		window.getSidePanel().updateCameraPosition(camera.getPosition());
		window.getSidePanel().updateCameraRotation(camera.getRotation());
	}
	

	
	
	static Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	
	@Override
	protected void close() {
		List<PlanetSaveData> saveDataList = new ArrayList<>();
		for (Planet planet : planets) {
			saveDataList.add(planet.getPlanetSaveData());
		}
		String json = gson.toJson(saveDataList);
		FileUtils.writeToFile("planets.json", json);
	}

	@Override
	public void onNew() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddPlanet(Vector3f velocity, boolean autoVelocity, float mass) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
