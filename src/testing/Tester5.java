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

	private int speedMultiplier = 1;
	
	@Override
	public void onKeyPressed(int keyCode) {
		if (keyCode == GLFW_KEY_SPACE) {
			speedMultiplier = (speedMultiplier % 6) + 1;
		}
	}

	@Override
	public void onKeyReleased(int keyCode) {
		
	}
	
	
	// Constants
	
	// Output
	private static final float FRAMERATE_REPORT_INTERVAL = 5;
	// Camera movement
	private static final float NORMAL_MOVEMENT_SPEED = 50;
	private static final float FAST_MOVEMENT_SPEED = 150;
	private static final float ROTATION_MOVEMENT_SPEED = 30; // Degrees per second
	// Entities
	private static final int PLANET_COUNT = 500;
	
	
	Camera camera = new Camera();
	
	List<Planet> planets = new ArrayList<>();
	
	private void addPlanets() {
		
		Model sphereModel = Models.getUVsphereModel();
		
		for (int i = 0; i < PLANET_COUNT; i++) {
			
			Vector3f pos = new Vector3f();
			
			pos.x = (float) MathUtils.randRange(-100, 100);
			pos.z = (float) MathUtils.randRange(-100, 100);
			
			// Make sure the sun isn't too crowded
			//if (Math.abs(pos.x) < 250 && Math.abs(pos.z) < 250) {
			//	pos.scale(10);
			//}
			
			
			Vector3f velocity = new Vector3f(
					(float) MathUtils.randRange(-2000/pos.x, 2000/pos.x), 
					(float) MathUtils.randRange(-1, 1), 
					(float) MathUtils.randRange(-2000/pos.z, 2000/pos.z));
			
			planets.add(new Planet(sphereModel, pos, velocity));
		}
		
	}
	
	
	public Tester5() {
		// Place the camera up and back from the origin
		Vector3f initialCameraPos = new Vector3f(0, 500, 500);
		camera.setPosition(initialCameraPos);
		camera.setPitch(45); // Point camera downwards
		
		addPlanets();
		
		// Give each planet a random mass
		for (Planet planet : planets) {
			planet.setMass((float) MathUtils.randRange(2, 7));
		}
		
		
		// The new "sun"
		Planet p0 = planets.get(0);
		p0.setMass(1E6f);
		//p0.setScale(109); // Sun radius = 109x earth
		p0.setScale(7);
		p0.setVelocity(new Vector3f(0, 0, 21.5f));
		p0.setPosition(new Vector3f(-50, 0, 0));
		
		// The second sun
		Planet p1 = planets.get(1);
		p1.setMass(1E6f);
		//p1.setScale(109); // Sun radius = 109x earth
		p1.setScale(7);
		p1.setVelocity(new Vector3f(0, 0, -21.5f));
		p1.setPosition(new Vector3f(50, 0, 0));
		
	}
	
	
	@Override
	protected void logic(float deltaTime) {
		
		
		displayFramerate(deltaTime);
		
		deltaTime *= speedMultiplier;
		
		moveCamera(deltaTime);
		
		//ArrayList<Planet> planetsToRemove = new ArrayList<>();
		
		for (Planet planet1 : planets) {
			
			Vector3f resultantAcceleration = new Vector3f();
			
			// Avoid comparing a planet to itself
			for (Planet planet2 : planets) {
				if (planet1 == planet2) {
					continue;
				}
				
				/*if (checkCollision(planet1, planet2)) {
					if (planet1.getMass() > planet2.getMass()) {
						planet1.setMass(planet1.getMass() + planet2.getMass());
						planetsToRemove.add(planet2);
					} else {
						planet2.setMass(planet2.getMass() + planet1.getMass());
						planetsToRemove.add(planet1);
					}
				}*/
				
				
				// Adding vectors gives a resultant vector
				resultantAcceleration.add(planet1.accelerationVectorTo(planet2));
			}
			
			planet1.updateVelocity(resultantAcceleration, deltaTime);
			//planet1.updatePosition(deltaTime);
		}
		
		//planets.removeAll(planetsToRemove);
		
		// Update planet positions after all the acceleration vectors have been calculated
		for (Planet planet : planets) {
			planet.updatePosition(deltaTime);
			//planet.setScale((float) Math.log10(planet.getMass()));
		}
		
		
		
	}
	
	/*private boolean checkCollision(Planet p1, Planet p2) {
		if (Math.abs(p1.getPosition().z - p2.getPosition().z) < 10
				&& Math.abs(p1.getPosition().x - p2.getPosition().x) < 10
				&& Math.abs(p1.getPosition().y - p2.getPosition().y) < 10) {
			return true;
		}
		return false;
	}*/
	
	
	
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
