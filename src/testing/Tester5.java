package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import entities.Camera;
import entities.Entity;
import entities.celestial.Planet;
import math.Vector2f;
import math.Vector3f;
import model.Model;
import model.Models;
import render.Renderer;
import util.MathUtils;
import window.MouseButton;

public class Tester5 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester5().run();
	}
	
	
	
	// Handle keyboard input
	
	private int timeMultiplier = 1;
	
	@Override
	public void onKeyPressed(int keyCode) {
		
		if (keyCode == GLFW_KEY_SPACE) {
			
			// Cycle through values for timeMultiplier,
			// from 1 to MAX_TIME_MULTIPLIER
			timeMultiplier = (timeMultiplier % MAX_TIME_MULTIPLIER) + 1;
			
		} else if (keyCode == GLFW_KEY_R) {
			
			resetCamera();
			
		}
		
	}
	
	@Override
	public void onKeyReleased(int keyCode) {
		
	}
	
	
	// Handle mouse input
	
	// Save out the mouse position while the mouse is held down
	Vector2f mouseDownPosition = new Vector2f();
	
	// Save out the relevant camera rotation
	float mouseDownYaw = 0;
	float mouseDownPitch = 0;
	
	//Vector2f mouseDownCameraRotation = new Vector2f();
	
	boolean mouseDown = false;
	
	@Override
	public void onMouseDown(MouseButton mouseButton) {
		
		if (mouseButton == MouseButton.LEFT) {
			
			
			
		} else if (mouseButton == MouseButton.RIGHT) {
			
			mouseDown = true;
			System.out.println("Mouse Down");
			
			// Allow endless mouse movement
			disableCursor();
			
			// Make a copy of the mouse position at the instant the mouse is pressed down
			mouseDownPosition = new Vector2f(getMousePosition());
			
			// Make a copy of the camera rotation too
			mouseDownYaw = camera.getYaw();
			mouseDownPitch = camera.getPitch();
			
			System.out.println("Mouse Down Position: " + mouseDownPosition);
			
		}
		
	}

	@Override
	public void onMouseUp(MouseButton mouseButton) {
		mouseDown = false;
		System.out.println("Mouse Up");
		enableCursor();
	}
	
	
	// Constants
	
	// Camera movement
	// Positional movement
	private static final float MOVEMENT_SPEED = 50;
	private static final float MOVEMENT_SPEED_FAST = 150;
	
	// Rotational movement
	private static final float ROTATION_SPEED = 30;
	private static final float ROTATION_SPEED_FAST = 90;
	
	
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
			
			// Generate random position
			pos.x = (float) MathUtils.randRange(-100, 100);
			pos.z = (float) MathUtils.randRange(-100, 100);
			
			// Generate random velocity, in a range based on position
			Vector3f velocity = new Vector3f(
					(float) MathUtils.randRange(-2000/pos.x, 2000/pos.x), 
					(float) MathUtils.randRange(-1, 1), // Small variation in y velocity
					(float) MathUtils.randRange(-2000/pos.z, 2000/pos.z));
			
			// Generate a random mass
			float mass = (float) MathUtils.randRange(2, 7);
			
			// Base scale on mass
			float scale = 0.7f + mass / 10;
			
			Planet planet = new Planet(sphereModel, pos, velocity);
			
			planet.setMass(mass);
			planet.setScale(scale);
			
			planets.add(planet);
		}
		
	}
	
	private void addSun() {
		
		// The new "sun"
		Planet p0 = planets.get(0);
		p0.setMass(1E6f);
		//p0.setScale(109); // Sun radius = 109x earth
		p0.setScale(7);
		p0.setVelocity(new Vector3f(0, 0, 0)); // 21.5
		p0.setPosition(new Vector3f(0, 0, 0));
		
	}
	
	private void resetCamera() {
		// Place the camera up and back from the origin
		Vector3f position = new Vector3f(0, 500, 500);
		camera.setPosition(position);
		// Point camera downwards at 45 degrees
		camera.setPitch(45);
		camera.setYaw(0);
		camera.setYaw(0);
	}
	
	public Tester5() {
		
		resetCamera();
		
		addPlanets();
		
		addSun();
		
	}
	
	private static final int MOUSE_MOVEMENT_SCALE = 10;
	
	@Override
	protected void logic(float deltaTime) {
		
		deltaTime *= timeMultiplier;
		
		moveCamera(deltaTime);
		
		if (mouseDown) {
			
			Vector2f mousePosition = getMousePosition();
			
			// Find the mouse movement vector
			Vector2f mouseDelta = Vector2f.sub(mouseDownPosition, mousePosition);
			
			// Adjust the camera rotation when the mouse was pressed 
			// by an amount proportional to the mouseDelta vector 
			camera.setYaw(mouseDownYaw - mouseDelta.x / MOUSE_MOVEMENT_SCALE);
			camera.setPitch(mouseDownPitch + mouseDelta.y / MOUSE_MOVEMENT_SCALE);
		}
		
		
		
		//ArrayList<Vector3f> newPositions = new ArrayList<>();
		
		for (Planet planet1 : planets) {
			
			Vector3f resultantAcceleration = new Vector3f();
			
			for (Planet planet2 : planets) {
				
				// Avoid comparing a planet to itself
				if (planet1 == planet2) {
					continue;
				}
				
				// Adding vectors gives a resultant vector
				resultantAcceleration.add(planet1.accelerationVectorTo(planet2));
			}
			
			/*
			Vector3f newPosition = new Vector3f(planet1.getPosition());
			Vector3f velocity = planet1.getVelocity();
			newPosition.x += (deltaTime * velocity.x) + (0.5 * resultantAcceleration.x * deltaTime * deltaTime);
			newPosition.y += (deltaTime * velocity.y) + (0.5 * resultantAcceleration.y * deltaTime * deltaTime);
			newPosition.z += (deltaTime * velocity.z) + (0.5 * resultantAcceleration.z * deltaTime * deltaTime);
			newPositions.add(newPosition);
			*/
			
			planet1.updateVelocity(resultantAcceleration, deltaTime);
			
			// Wait until all planet velocity values have been updated
			
			//planet1.updatePosition(deltaTime);
		}
		
		// For basic Euler integration
		for (Planet planet : planets) {
			planet.updatePosition(deltaTime);
		}
		
		/*
		// Possible better SUVAT based integration
		// Update planet positions after all the acceleration vectors have been calculated
		int i = 0;
		for (Planet planet : planets) {
			planet.setPosition(newPositions.get(i++));
			//planet.setRotY(getTime() * planet.getVelocity().magnitude());
		}
		*/
		
		
	}
	
	
	
	private void moveCamera(float deltaTime) {
		
		float movementSpeed;
		float rotationSpeed;
		
		if (isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
			movementSpeed = MOVEMENT_SPEED_FAST;
			rotationSpeed = ROTATION_SPEED_FAST;
		} else {
			movementSpeed = MOVEMENT_SPEED;
			rotationSpeed = ROTATION_SPEED;
		}
		
		// Positional controls
		// Forward and back
		if (isKeyPressed(GLFW_KEY_W)) {
			camera.moveForward(movementSpeed, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_S)) {
			camera.moveBack(movementSpeed, deltaTime);
		}
		
		// Right and left
		if (isKeyPressed(GLFW_KEY_D)) {
			camera.moveRight(movementSpeed, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_A)) {
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
		if (isKeyPressed(GLFW_KEY_DOWN)) {
			camera.increasePitch(rotationSpeed, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_UP)) {
			camera.decreasePitch(rotationSpeed, deltaTime);
		}
		
		// Yaw
		if (isKeyPressed(GLFW_KEY_RIGHT)) {
			camera.increaseYaw(rotationSpeed, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_LEFT)) {
			camera.decreaseYaw(rotationSpeed, deltaTime);
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
