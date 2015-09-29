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
import util.MathUtils;

public class Tester5 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester5().run();
	}

	private boolean fastToggle = false;
	
	@Override
	public void onKeyPressed(int keyCode) {
		if (keyCode == GLFW_KEY_SPACE) {
			fastToggle = !fastToggle;
		}
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
	// Entities
	private static final int PLANET_COUNT = 500;
	
	
	Camera camera = new Camera();
	
	List<Planet> planets = new ArrayList<>();
	
	//Entity sun = new Entity(Models.getUVsphereModel(), new Vector3f(), new Vector3f(), 5);
	
	private void addPlanets() {
		
		Model sphereModel = Models.getUVsphereModel();
		
		for (int i = 0; i < PLANET_COUNT; i++) {
			
			Vector3f pos = new Vector3f();
			
			//pos.x = (float) MathUtils.randRange(-1000, 1000);
			//pos.z = (float) MathUtils.randRange(-1000, 1000);
			
			pos.x = (float) MathUtils.randRange(-100, 100);
			pos.z = (float) MathUtils.randRange(-100, 100);
			
			// Make sure the sun isn't too crowded
			//if (Math.abs(pos.x) < 250 && Math.abs(pos.z) < 250) {
			//	pos.scale(10);
			//}
			
			//float orbitalRadius = calculateOrbitalRadius(pos);
			// Look at Kepler's third law here
			//float timePeriod = (float) (Math.sqrt(pos.x*pos.x*pos.x)/100); // Base the orbital time period on x^2
			
			Vector3f velocity = new Vector3f(
					(float) MathUtils.randRange(-2000/pos.x, 2000/pos.x), 
					(float) MathUtils.randRange(-1, 1), 
					(float) MathUtils.randRange(-2000/pos.z, 2000/pos.z));
			
			planets.add(new Planet(sphereModel, pos, velocity));
		}
		
	}
	
	//private float calculateOrbitalRadius(Vector3f pos) {
	//	return (float) Math.sqrt(pos.x*pos.x + pos.z*pos.z);
	//}
	
	
	public Tester5() {
		// Place the camera up and back from the origin
		Vector3f initialCameraPos = new Vector3f(0, 1000, 1000);
		camera.setPosition(initialCameraPos);
		camera.setPitch(45); // Point camera downwards
		
		addPlanets();
		
		// Give each planet a random mass
		for (Planet planet : planets) {
			planet.setMass(3);
		}
		
		
		// The new "sun"
		Planet p0 = planets.get(0);
		p0.setMass(1E6f);
		//p0.setScale(109); // Sun radius = 109x earth
		p0.setScale(10);
		p0.setVelocity(new Vector3f());
		p0.setPosition(new Vector3f());
		
		
		Planet p1 = planets.get(1);
		p1.setMass(4E5f);
		//p1.setScale(109); // Sun radius = 109x earth
		p1.setScale(7);
		p1.setVelocity(new Vector3f(30f, 0, -0.03f));
		p1.setPosition(new Vector3f(-100, 0, 100));
		
	}
	
	
	@Override
	protected void logic(float deltaTime) {
		
		displayFramerate(deltaTime);
		
		//deltaTime /= 100;
		
		if (fastToggle) {
			deltaTime *= 5;
		}
		
		moveCamera(deltaTime);
		
		for (Planet planet1 : planets) {
			
			Vector3f resultantAcceleration = new Vector3f();
			
			for (Planet planet2 : planets) {
				if (planet1 == planet2) {
					continue;
				}
				
				resultantAcceleration.add(planet1.accelerationVectorTo(planet2));
			}
			
			planet1.tick(resultantAcceleration, deltaTime);
		}
		
		//Planet p0 = planets.get(0);
		//p0.setVelocity(new Vector3f());
		
		
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
		
		//renderer.render(sun, camera);
		
		for (Entity planet : planets) {
			renderer.render(planet, camera);
		}
		
	}
	
	

}
