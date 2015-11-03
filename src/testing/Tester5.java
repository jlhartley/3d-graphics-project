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
	
	
	
	private int timeMultiplier = 1;
	
	@Override
	public void onKeyPressed(int keyCode) {
		
		if (keyCode == GLFW_KEY_SPACE) {
			// Cycle through values for timeMultiplier,
			// from 1 to MAX_TIME_MULTIPLIER
			timeMultiplier = (timeMultiplier % MAX_TIME_MULTIPLIER) + 1;
		}
		
	}
	
	@Override
	public void onKeyReleased(int keyCode) {
		
	}
	
	
	// Constants
	
	// Camera movement
	private static final float NORMAL_MOVEMENT_SPEED = 50;
	private static final float FAST_MOVEMENT_SPEED = 150;
	private static final float ROTATION_MOVEMENT_SPEED = 30; // Degrees per second
	
	// Entities
	private static final int PLANET_COUNT = 500;
	
	// Time
	private static final int MAX_TIME_MULTIPLIER = 5;
	
	
	Camera camera = new Camera();
	
	List<Planet> planets = new ArrayList<>();
	
	private void addPlanets() {
		
		Model sphereModel = Models.getUVsphereModel();
		
		for (int i = 0; i < PLANET_COUNT; i++) {
			
			Vector3f pos = new Vector3f();
			
			// Random position
			pos.x = (float) MathUtils.randRange(-100, 100);
			pos.z = (float) MathUtils.randRange(-100, 100);
			
			// Generate a random velocity based on position
			Vector3f velocity = new Vector3f(
					(float) MathUtils.randRange(-2000/pos.x, 2000/pos.x), 
					(float) MathUtils.randRange(-1, 1), // Small variation in y velocity
					(float) MathUtils.randRange(-2000/pos.z, 2000/pos.z));
			
			Planet planet = new Planet(sphereModel, pos, velocity);
			
			// Give each planet a random mass
			planet.setMass((float) MathUtils.randRange(2, 7));
			
			planets.add(planet);
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
		p0.setVelocity(new Vector3f(0, 0, 0)); // 21.5
		p0.setPosition(new Vector3f(0, 0, 0));
		
	}
	
	
	@Override
	protected void logic(float deltaTime) {
		
		deltaTime *= timeMultiplier;
		
		moveCamera(deltaTime);
		
		
		ArrayList<Vector3f> newPositions = new ArrayList<>();
		
		for (Planet planet1 : planets) {
			
			Vector3f resultantAcceleration = new Vector3f();
			
			// Avoid comparing a planet to itself
			for (Planet planet2 : planets) {
				
				if (planet1 == planet2) {
					continue;
				}
				
				// Adding vectors gives a resultant vector
				resultantAcceleration.add(planet1.accelerationVectorTo(planet2));
			}
			
			Vector3f newPosition = new Vector3f(planet1.getPosition());
			Vector3f velocity = planet1.getVelocity();
			newPosition.x += (deltaTime * velocity.x) + (0.5 * resultantAcceleration.x * deltaTime * deltaTime);
			newPosition.y += (deltaTime * velocity.y) + (0.5 * resultantAcceleration.y * deltaTime * deltaTime);
			newPosition.z += (deltaTime * velocity.z) + (0.5 * resultantAcceleration.z * deltaTime * deltaTime);
			newPositions.add(newPosition);
			
			planet1.updateVelocity(resultantAcceleration, deltaTime);
			//planet1.updatePosition(deltaTime);
		}
		
		// Update planet positions after all the acceleration vectors have been calculated
		int i = 0;
		for (Planet planet : planets) {
			planet.setPosition(newPositions.get(i++));
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
