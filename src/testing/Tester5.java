package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import entities.Camera;
import entities.Entity;
import entities.celestial.Planet;
import math.Vector3f;
import model.Model;
import model.Models;
import render.Renderer;

public class Tester5 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester5().run();
	}

	@Override
	public void onKeyPressed(int keyCode) {
		
	}

	@Override
	public void onKeyReleased(int keyCode) {
		
	}
	
	
	// Constants
	
	// Output
	private static final float FRAMERATE_REPORT_INTERVAL = 1;
	
	// Camera movement
	private static final float NORMAL_MOVEMENT_SPEED = 50;
	private static final float FAST_MOVEMENT_SPEED = 150;
	private static final float ROTATION_MOVEMENT_SPEED = 30; // Degrees per second
	
	
	
	Camera camera = new Camera();
	
	List<Planet> planets = new ArrayList<>();
	
	Entity sun = new Entity(Models.getUVsphereModel(), new Vector3f(), new Vector3f(), 5);
	
	private void addPlanets() {
		
		Model sphereModel = Models.getUVsphereModel();
		
		int planetCount = 200;
		
		for (int i = 0; i < planetCount; i++) {
			//Vector3f pos = getPlanetPosition();
			Vector3f pos = new Vector3f();
			int xOffset = 25; // Offset to avoid overlapping the sun
			pos.x = i + xOffset; // Each planet has unique x;
			
			pos.x = (float) (Math.random() * 500);
			pos.z = (float) (Math.random() * 500);
			
			float orbitalRadius = calculateOrbitalRadius(pos);
			// Look at Kepler's third law here
			float timePeriod = (float) (Math.sqrt(pos.x*pos.x*pos.x)/100); // Base the orbital time period on x^2
			// Randomly set the initial angle of the planet
			planets.add(new Planet(sphereModel, pos, orbitalRadius, timePeriod, (float) (Math.random() * 360)));
		}
		
	}
	
	private float calculateOrbitalRadius(Vector3f pos) {
		return (float) Math.sqrt(pos.x*pos.x + pos.z*pos.z);
	}
	
	
	public Tester5() {
		// Place the camera up and back from the origin
		Vector3f initialCameraPos = new Vector3f(0, 250, 280);
		camera.setPosition(initialCameraPos);
		camera.setPitch(45); // Point camera downwards
		
		addPlanets();
		
		for (Planet planet : planets) {
			planet.setMass((float) (Math.random() * 1000));
		}
		
		Planet p0 = planets.get(0);
		p0.setMass(100000000);
		p0.setScale(10);
	}
	
	
	@Override
	protected void logic(float deltaTime) {
		
		displayFramerate(deltaTime);
		moveCamera(deltaTime);
		
		//for (Planet planet : planets) {
		//	planet.setPositionFromTime(getTime());
		//}
		
		//Planet p0 = planets.get(0);
		//Planet p1 = planets.get(1);
		
		//p0.tick(p0.getAccelerationVectorToPlanet(p1), deltaTime);
		
		//System.out.println(p0.getPosition());
		
		for (Planet planet1 : planets) {
			for (Planet planet2 : planets) {
				if (planet1 == planet2) {
					continue;
				}
				planet1.tick(planet1.getAccelerationVectorToPlanet(planet2), deltaTime);
				//System.out.println(planet1.getPosition());
			}
		}
		
	}
	
	
	
	
	
	private float lastFramerateReportTime;
	private void displayFramerate(float deltaTime) {
		
		if (lastFramerateReportTime > FRAMERATE_REPORT_INTERVAL) {
			float frameRate = 1/deltaTime;

			System.out.println("Framerate: " + frameRate + " FPS");
			lastFramerateReportTime = 0;
		} else {
			lastFramerateReportTime += deltaTime;
		}
	}
	
	
	private void moveCamera(float deltaTime) {
		
		float movementSpeed;
		if (isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
			movementSpeed = FAST_MOVEMENT_SPEED;
		} else {
			movementSpeed = NORMAL_MOVEMENT_SPEED;
		}
		
		// Positional controls
		// Forward and back
		if (isKeyPressed(GLFW_KEY_UP)) {
			camera.moveForward(movementSpeed, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_DOWN)) {
			camera.moveBack(movementSpeed, deltaTime);
		}
		
		// Right and left
		if (isKeyPressed(GLFW_KEY_RIGHT)) {
			camera.moveRight(movementSpeed, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_LEFT)) {
			camera.moveLeft(movementSpeed, deltaTime);
		}
		
		// Up and down
		if (isKeyPressed(GLFW_KEY_PAGE_UP)) {
			camera.moveUp(movementSpeed, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_PAGE_DOWN)){
			camera.moveDown(movementSpeed, deltaTime);
		}
		
		
		// Rotational controls
		// Pitch
		if (isKeyPressed(GLFW_KEY_S)) {
			camera.increasePitch(ROTATION_MOVEMENT_SPEED, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_W)) {
			camera.decreasePitch(ROTATION_MOVEMENT_SPEED, deltaTime);
		}
		
		// Yaw
		if (isKeyPressed(GLFW_KEY_D)) {
			camera.increaseYaw(ROTATION_MOVEMENT_SPEED, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_A)) {
			camera.decreaseYaw(ROTATION_MOVEMENT_SPEED, deltaTime);
		}
		
		
	}
	
	
	
	
	
	
	
	

	@Override
	protected void render(Renderer renderer) {
		
		renderer.render(sun, camera);
		
		for (Entity planet : planets) {
			renderer.render(planet, camera);
		}
		
	}
	
	

}
