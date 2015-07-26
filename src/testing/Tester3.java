package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedList;

import entities.Camera;
import entities.Entity3D;
import math.Vector3f;
import model.CubeModel;
import model.Model;
import model.Models;
import render.Renderer;

public class Tester3 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester3().run();
	}

	@Override
	public void onKeyPressed(int keyCode) {
		if (keyCode == GLFW_KEY_SPACE) {
			reset();
		}
	}

	@Override
	public void onKeyReleased(int keyCode) {
		
	}
	
	
	// Either side of (0,0)
	// E.g +5 to -5
	private static final int XY_LIMIT = 10;
	
	private static final int MAX_DISTANCE = 4000;
	private static final int MIN_DISTANCE = 5;
	
	private static final double MAX_SCALE = 1.5;
	private static final double MIN_SCALE = 0.5;
	
	
	
	// Get an X or Y position that ranges from -XY_LIMIT to XY_LIMIT
	private static float getRandomXYPosition() {
		return (float) (-XY_LIMIT + Math.random() * 2 * XY_LIMIT);
	}
	
	private static float getRandomZPosition() {
		return (float) (-MIN_DISTANCE - Math.random() * (MAX_DISTANCE - MIN_DISTANCE));
	}
	
	private static Vector3f getRandomPosition() {
		return new Vector3f(getRandomXYPosition(), getRandomXYPosition(), getRandomZPosition());
	}
	
	private Vector3f getRandomRotation() {
		return new Vector3f((float) (Math.random() * 360), (float) (Math.random() * 360), (float) (Math.random() * 360));
	}
	
	// Get a random scale from MIN_SCALE to MAX_SCALE
	private static float getRandomScale() {
		return (float) (MIN_SCALE + Math.random() * (MAX_SCALE - MIN_SCALE));
	}
	
	private static Vector3f getRandomColour() {
		return new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	
	private static Vector3f getRandomVelocity() {
		return new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	
	
	Camera camera = new Camera();
	
	LinkedList<Entity3D> cubes = new LinkedList<>();
	
	private static final int CUBE_COUNT = 5000;
	
	public void generateCubes(Model cubeModel) {
		for (int i = 0; i < CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			Vector3f randomColour = getRandomColour();
			Vector3f randomRotation = getRandomRotation();
			Vector3f randomVelocity = getRandomVelocity();
			float randomScale = getRandomScale();
			cubes.add(new Entity3D(cubeModel, randomPosition, randomRotation, 1));
		}
	}
	
	public Tester3() {
		CubeModel cubeModel = Models.getCubeModel(); // Must be cleaned up at the end
		generateCubes(cubeModel);
		reset();
	}
	
	private void reset() {
		collided = false;
		won = false;
		cameraForwardSpeed = 5;
		camera.setPosition(0, 0, 0);
	}
	
	// Some 'gameplay' state information
	float cameraForwardSpeed;
	boolean collided;
	boolean won;
	
	private static final float MOVEMENT_SPEED = 5.5f;
	
	// State information for output
	float lastReportedDistanceTime;
	
	private static final float DISTANCE_REPORT_INTERVAL = 1;
	
	private static final int LOW_FRAMERATE = 30;
	
	@Override
	protected void logic(double deltaTime) {
		
		// 60fps = 16 milliseconds
		double frameRate = 1/deltaTime;
		
		if (frameRate < LOW_FRAMERATE) {
			System.out.println("Low Framerate: " + frameRate + " FPS");
		}
		
		if (collided || won) {
			return;
		}
		
		// Negative since we travel into the screen in the negative z-direction
		float distance = -camera.getPosition().z;
		
		if (distance > MAX_DISTANCE) {
			won = true;
		}
		
		if (lastReportedDistanceTime > DISTANCE_REPORT_INTERVAL) {
			System.out.println("Distance: " + distance);
			lastReportedDistanceTime = 0;
		} else {
			lastReportedDistanceTime += deltaTime;
		}
		
		// The 'cheat' C key - position with an offset of 50 from the end
		if (isKeyPressed(GLFW_KEY_C)) {
			camera.setPosition(0, 0, -(MAX_DISTANCE - 50));
		}
		
		
		 // Look backwards when shift is pressed
		if (isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
			camera.setYaw(180);
		} else {
			camera.setYaw(0);
		}
		
		if (isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
			cameraForwardSpeed += 20 * deltaTime; // Accelerate faster!
		} else {
			cameraForwardSpeed += 0.3 * deltaTime; // Accelerate
		}
		
		if (isKeyPressed(GLFW_KEY_UP) && camera.getPosition().y < XY_LIMIT) {
			camera.moveUp(MOVEMENT_SPEED, (float) deltaTime);
		} 
		
		if (isKeyPressed(GLFW_KEY_DOWN) && camera.getPosition().y > -XY_LIMIT) {
			camera.moveDown(MOVEMENT_SPEED, (float) deltaTime);
		}
		
		if (isKeyPressed(GLFW_KEY_RIGHT) && camera.getPosition().x < XY_LIMIT) {
			camera.moveRight(MOVEMENT_SPEED, (float) deltaTime);
		}
		
		if (isKeyPressed(GLFW_KEY_LEFT) && camera.getPosition().x > -XY_LIMIT) {
			camera.moveLeft(MOVEMENT_SPEED, (float) deltaTime);
		}
		
		for (Entity3D cube : cubes) {
			
			// Bounding box collision detection - not correctly sized
			if (Math.abs(camera.getPosition().z - cube.getPosition().z) < 0.25
					&& Math.abs(camera.getPosition().x - cube.getPosition().x) < 0.25
					&& Math.abs(camera.getPosition().y - cube.getPosition().y) < 0.25) {
				collided = true;
			}
			
			// Rotate the cubes!
			cube.setRotX((float) (getTime()*100));
			//cube.setRotY((float) (getTime()*100));
			
		}
		
		// Finally move the camera forward each frame
		camera.moveForward(cameraForwardSpeed, (float) deltaTime);
		
		
	}

	@Override
	protected void render(Renderer renderer) {
		renderer.clear();
		
		if (collided) {
			renderer.setClearColour(0.9f, 0.2f, 0.2f);
		} else if (won) {
			renderer.setClearColour((float)0/255, (float)97/255, (float)32/255);
		} else {
			renderer.setClearColour(0, 0, 0);
		}
		
		for (Entity3D cube : cubes) {
			renderer.render(cube, camera, (float) getTime());
		}
	}

	@Override
	protected void cleanUpModels() {
		Models.getCubeModel().cleanUp();
	}
	
	

}
