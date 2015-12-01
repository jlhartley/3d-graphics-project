package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import camera.Camera;
import camera.CameraControls;
import entities.Entity;
import entities.celestial.Planet;
import entities.celestial.Star;
import math.geometry.Vector3f;
import model.Model;
import model.Models;
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
			// Cycle through values for timeMultiplier, from 1 to MAX_TIME_MULTIPLIER
			timeMultiplier = (timeMultiplier % MAX_TIME_MULTIPLIER) + 1;
		} else if (keyCode == GLFW_KEY_I) {
			paused = !paused;
		} else if (keyCode == GLFW_KEY_R) {
			resetCamera();
		} else if (keyCode == GLFW_KEY_O) {
			log("Orthographic Projection");
			switchProjection(ProjectionType.ORTHOGRAPHIC);
		} else if (keyCode == GLFW_KEY_P) {
			log("Perspective Projection");
			switchProjection(ProjectionType.PERSPECTIVE);
		} else if (keyCode == GLFW_KEY_U) {
			// Place the camera directly up in y
			camera.setPosition(0, 500, 0);
			// Point camera straight down
			camera.setPitch(90);
			camera.setYaw(0);
		} else if (keyCode == GLFW_KEY_N) {
			cameraControls.setControlType(CameraControls.ControlType.ABSOLUTE);
		} else if (keyCode == GLFW_KEY_M) {
			cameraControls.setControlType(CameraControls.ControlType.RELATIVE);
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
	private static final int PLANET_COUNT = 500;
	
	// Time
	private static final int MAX_TIME_MULTIPLIER = 5;
	
	
	
	// State
	
	// Scales deltaTime
	private int timeMultiplier = 1;
	
	// Pause all logic other than camera movement
	boolean paused = false;
	
	
	// Main objects
	
	Camera camera = new Camera();
	
	CameraControls cameraControls = new CameraControls(camera, window);
	
	Model sphereModel = Models.getIcosphereModel();
	
	// Sun is centred at 0, 0, 0 with no velocity
	Star sun = new Star(sphereModel, new Vector3f(0, 0, 0));
	
	List<Planet> planets = new ArrayList<>();
	
	
	private void addPlanets() {
		
		for (int i = 0; i < PLANET_COUNT; i++) {
			
			Vector3f position = new Vector3f();
			
			// Generate random position
			position.x = (float) MathUtils.randRange(-100, 100);
			position.z = (float) MathUtils.randRange(-100, 100);
			
			// Clear space close to the sun
			if (position.magnitude() < 20) {
				position.scale(3);
			}
			
			// Generate random velocity, in a range based on position
			Vector3f velocity = new Vector3f(
					(float) MathUtils.randRange(-2000/position.x, 2000/position.x), 
					(float) MathUtils.randRange(-5, 5), // Small variation in y velocity
					(float) MathUtils.randRange(-2000/position.z, 2000/position.z));
			
			// Generate a random mass
			float mass = (float) MathUtils.randRange(2, 7);
			
			// Base scale on cube root of mass
			float scale = (float) Math.cbrt(mass * 2 - 3);
			
			Planet planet = new Planet(sphereModel, position, velocity);
			planet.setMass(mass);
			planet.setScale(scale);
			
			planets.add(planet);
		}
		
	}
	
	private void initSun() {
		sun.setMass(1E6f);
		//sun.setScale(109); // Sun radius = 109x earth
		sun.setScale(7);
		sun.setVelocity(0, 0, 0); // 21.5
		sun.setPosition(0, 0, 0);
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
		addPlanets();
		initSun();
	}

	
	@Override
	protected void logic(float deltaTime) {
		
		deltaTime *= timeMultiplier;
		
		cameraControls.moveCamera(deltaTime);
		
		if (paused) {
			return;
		}
		
		
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
