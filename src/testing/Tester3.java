package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedList;
import java.util.List;

import camera.Camera;
import entities.Entity;
import math.Vector3f;
import model.Model;
import model.ModelBuilder;
import model.Models;
import render.Renderer;
import util.MathUtils;
import window.MouseButton;

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
	
	
	@Override
	public void onMouseDown(MouseButton mouseButton) {
		
	}

	@Override
	public void onMouseUp(MouseButton mouseButton) {
		
	}
	
	
	
	private static final int XY_LIMIT = 200;
	
	private static final int MAX_DISTANCE = 3000;
	private static final int MIN_DISTANCE = 5;
	
	//private static final double MAX_SCALE = 1.5;
	//private static final double MIN_SCALE = 0.5;
	
	
	
	private static float getRandomXYPosition() {
		return (float) MathUtils.randRange(-XY_LIMIT, XY_LIMIT);
	}
	
	private static float getRandomZPosition() {
		return (float) MathUtils.randRange(-MIN_DISTANCE, -MAX_DISTANCE);
	}
	
	private static Vector3f getRandomPosition() {
		return new Vector3f(getRandomXYPosition(), getRandomXYPosition(), getRandomZPosition());
	}
	
	private static Vector3f getRandomRotation() {
		return new Vector3f((float) (Math.random() * 360), (float) (Math.random() * 360), (float) (Math.random() * 360));
	}
	
	/*
	// Get a random scale from MIN_SCALE to MAX_SCALE
	private static float getRandomScale() {
		return (float) (MIN_SCALE + Math.random() * (MAX_SCALE - MIN_SCALE));
	}
	
	private static Vector3f getRandomColour() {
		return new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	*/
	
	Camera camera = new Camera();
	
	List<Entity> cubes = new LinkedList<>();
	
	Entity cubeField;
	
	private static final int CUBE_COUNT = 300000;
	
	public void generateCubes(Model cubeModel) {
		for (int i = 0; i < CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			Vector3f randomRotation = getRandomRotation();
			//Vector3f randomColour = getRandomColour();
			//float randomScale = getRandomScale();
			cubes.add(new Entity(cubeModel, randomPosition, randomRotation));
		}
	}
	
	public Tester3() {
		Model cubeModel = Models.getCubeModel();
		generateCubes(cubeModel);
		ModelBuilder cubeFieldBuilder = new ModelBuilder(cubes);
		cubeField = new Entity(cubeFieldBuilder.build(), new Vector3f());
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
	
	private static final float NORMAL_ACCELERATION = 0.3f;
	
	private static final float RAPID_ACCELERATION = 20;
	
	private static final float MOVEMENT_SPEED = 9.5f;
	
	// State information for output
	float lastReportedDistanceTime;
	
	// The time interval between distance reports in seconds
	private static final float DISTANCE_REPORT_INTERVAL = 1;
	
	private static final int LOW_FRAMERATE = 30;
	
	@Override
	protected void logic(float deltaTime) {
		
		// 60fps = 16 milliseconds
		float frameRate = 1/deltaTime;
		
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
		
		// Determine how quickly to accelerate
		if (isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
			cameraForwardSpeed += RAPID_ACCELERATION * deltaTime; // Accelerate faster!
		} else {
			cameraForwardSpeed += NORMAL_ACCELERATION * deltaTime; // Accelerate
		}
		
		// Camera movement controls and limits
		if (isKeyPressed(GLFW_KEY_UP) && camera.getPosition().y < XY_LIMIT) {
			camera.moveUp(MOVEMENT_SPEED, deltaTime);
		} 
		
		if (isKeyPressed(GLFW_KEY_DOWN) && camera.getPosition().y > -XY_LIMIT) {
			camera.moveDown(MOVEMENT_SPEED, deltaTime);
		}
		
		if (isKeyPressed(GLFW_KEY_RIGHT) && camera.getPosition().x < XY_LIMIT) {
			camera.moveRight(MOVEMENT_SPEED, deltaTime);
		}
		
		if (isKeyPressed(GLFW_KEY_LEFT) && camera.getPosition().x > -XY_LIMIT) {
			camera.moveLeft(MOVEMENT_SPEED, deltaTime);
		}
		
		for (Entity cube : cubes) {
			
			// Bounding box collision detection - not correctly sized / adjusted for rotation
			if (Math.abs(camera.getPosition().z - cube.getPosition().z) < 0.5
					&& Math.abs(camera.getPosition().x - cube.getPosition().x) < 0.5
					&& Math.abs(camera.getPosition().y - cube.getPosition().y) < 0.5) {
				collided = true;
			}
			
			// Rotate the cubes!
			//cube.setRotX(getTime() * 100);
			//cube.setRotY(getTime() * 100);
			
		}
		
		// Finally move the camera forward each frame
		camera.moveForward(cameraForwardSpeed, deltaTime);
		
		
	}

	// TODO: Pass colours using some kind of Colour class
	//private static final Vector3f WIN_COLOUR = new Vector3f((float)0/255, (float)97/255, (float)32/255);
	
	@Override
	protected void render(Renderer renderer) {
		
		if (collided) {
			renderer.setClearColour(0.9f, 0.2f, 0.2f);
		} else if (won) {
			renderer.setClearColour((float)0/255, (float)97/255, (float)32/255);
		} else {
			renderer.setClearColour(0, 0, 0);
		}
		
		//for (Entity cube : cubes) {
		//	renderer.render(cube, camera, getTime());
		//}
		
		renderer.render(cubeField, camera);
	}
	
	

}
