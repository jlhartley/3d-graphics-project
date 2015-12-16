package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import camera.AbsoluteControls;
import camera.Camera;
import camera.CameraControls;
import camera.RelativeControls;
import entities.Entity;
import entities.celestial.CelestialEntity;
import entities.celestial.Planet;
import entities.celestial.Star;
import math.geometry.Vector3f;
import model.Model;
import model.Models;
import physics.Constants;
import render.ProjectionType;
import render.Renderer;
import math.MathUtils;
import window.MouseButton;

public class Tester5 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester5().run();
	}
	
	
	
	// Keyboard input
	
	@Override
	public void onKeyPressed(int keyCode) {
		if (keyCode == GLFW_KEY_SPACE) {
			// Cycle through values for timeMultiplier,
			// from 1 to MAX_TIME_MULTIPLIER
			timeMultiplier = (timeMultiplier % MAX_TIME_MULTIPLIER) + 1;
		} else if (keyCode == GLFW_KEY_I) {
			paused = !paused;
		} else if (keyCode == GLFW_KEY_R) {
			resetCamera();
			initPlanets();
		} else if (keyCode == GLFW_KEY_O) {
			log("Orthographic Projection");
			switchProjection(ProjectionType.ORTHOGRAPHIC);
		} else if (keyCode == GLFW_KEY_P) {
			log("Perspective Projection");
			switchProjection(ProjectionType.PERSPECTIVE);
		} else if (keyCode == GLFW_KEY_U) {
			// Place the camera directly up in y
			camera.setPosition(0, (float) Math.sqrt(500*500 + 500*500), 0);
			// Point camera straight down
			camera.setPitch(90);
			camera.setYaw(0);
		} else if (keyCode == GLFW_KEY_1) {
			cameraControls = new AbsoluteControls(camera, window);
		} else if (keyCode == GLFW_KEY_2) {
			cameraControls = new RelativeControls(camera, window);
		}
	}
	
	@Override
	public void onKeyReleased(int keyCode) {
		
	}
	
	
	// Mouse input
	
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
	
	
	// Constants
	
	// Entities
	private static final double MIN_ORBITAL_RADIUS = 100;
	private static final double MAX_ORBITAL_RADIUS = 500;
	
	private static final double MAX_PLANET_Y = 10;
	private static final double MIN_PLANET_Y = -10;
	
	private static final double MIN_PLANET_MASS = 0.5;
	private static final double MAX_PLANET_MASS = 15;
	
	private static final double PLANET_DENSITY = 5;
	
	private static final double STAR_DENSITY = 1;
	
	private static final int PLANET_COUNT = 300;
	
	// Time
	private static final int MAX_TIME_MULTIPLIER = 5;
	
	
	
	// State
	
	// Scales deltaTime
	private int timeMultiplier = 1;
	
	// Pause all logic other than camera movement
	boolean paused = false;
	
	
	// Main objects
	
	Camera camera = new Camera();
	
	CameraControls cameraControls = new RelativeControls(camera, window);
	
	Model sphereModel = Models.getIcosphereModel();
	
	// Sun is at the origin with no velocity
	Star sun = new Star(sphereModel);
	
	List<Planet> planets = new ArrayList<>();
	
	
	private void initPlanets() {
		
		planets.clear();
		
		for (int i = 0; i < PLANET_COUNT; i++) {
			
			Planet planet = new Planet(sphereModel);
			
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
			
			planets.add(planet);
		}
		
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
		// Place the camera up and back from the origin
		camera.setPosition(0, 500, 500);
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
		
		for (Planet planet1 : planets) {
			
			Vector3f resultantForce = new Vector3f();
			
			for (Planet planet2 : planets) {
				
				// Avoid comparing a planet to itself
				if (planet1 == planet2) {
					continue;
				}
				
				// Add force vectors for each planet to get a resultant force
				resultantForce.add(planet1.forceTo(planet2));
			}
			
			// Add force vector for the sun to get a resultant force
			resultantForce.add(planet1.forceTo(sun));
			
			// Divide force vector by mass to give acceleration vector
			float mass = planet1.getMass();
			Vector3f resultantAcceleration = new Vector3f(resultantForce).scale(1 / mass);
			
			planet1.updateVelocity(resultantAcceleration, deltaTime);
			
		}
		
		// Wait until all planet velocity values have been updated,
		// before updating the position of each planet
		
		for (Planet planet : planets) {
			// Update position
			planet.updatePosition(deltaTime);
			// Rotate planet with time
			planet.setRotY(getTime() * 100);
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
	
	

}
