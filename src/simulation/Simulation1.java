package simulation;

import static logging.Logger.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.Entity;
import entities.celestial.CelestialEntity;
import entities.celestial.Planet;
import entities.celestial.Star;
import io.FileUtils;
import math.MathUtils;
import math.geometry.Vector3f;
import model.Model;
import model.Models;
import physics.Constants;
import physics.Integrator;
import render.Renderer;
import save.PlanetSaveData;

public class Simulation1 extends Simulation {
	
	public static void main(String[] args) {
		new Simulation1().run();
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
	
	Model sphereModel = Models.getIcosphereModel();
	Model rockModel = Models.getRockModel();
	
	// Sun is at the origin with no velocity
	Star sun = new Star(sphereModel);
	
	List<Planet> planets = new ArrayList<>();
	
	Integrator integrator;
	
	
	private void initPlanets() {
		
		planets.clear();
		
		for (int i = 0; i < PLANET_COUNT; i++) {
			
			Planet planet = new Planet(rockModel);
			
			// Initial independent values
			double mass = getPlanetMass();
			Vector3f position = getPlanetPosition();
			
			/*
			if (i % 50 == 0) {
				mass *= 500;
			}
			*/
			
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
		
		//onOpen(DEFAULT_SAVE_PATH);
		
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
	
	public Simulation1() {
		window.setTitle(TITLE);
		initSun();
		initPlanets();
		integrator = new Integrator(planets, sun);
	}
	
	@Override
	protected void logic(float deltaTime) {
		
		// Integration
		
		//integrator.symplecticEuler(deltaTime);
		integrator.symplecticEuler2(deltaTime);
		//integrator.verlet(deltaTime);
		
		
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
		List<PlanetSaveData> saveDataList = new ArrayList<>(planets.size());
		for (Planet planet : planets) {
			saveDataList.add(planet.getPlanetSaveData());
		}
		String json = gson.toJson(saveDataList);
		FileUtils.writeToFile(path, json);
	}

	@Override
	public void onOpen(String path) {
		paused = true;
		// Have to sleep for a while to ensure the logic thread has paused
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		planets.clear();
		String json = FileUtils.getFileContents(path);
		PlanetSaveData[] saveDataArray = gson.fromJson(json, PlanetSaveData[].class);
		for (PlanetSaveData saveData : saveDataArray) {
			Planet planet = new Planet(rockModel, saveData);
			// Manually restore scale based on mass
			double scale = getSphereRadius(saveData.mass, PLANET_DENSITY);
			planet.setScale((float) scale);
			planets.add(planet);
		}
		paused = false;
	}
	
	
}
