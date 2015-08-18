package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedList;

import entities.Camera;
import entities.Entity;
import entities.MovableEntity;
import math.Vector3f;
import model.CubeModel;
import model.Model;
import model.Models;
import render.Renderer;

public class Tester4 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester4().run();
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
	private static final int XY_LIMIT = 50;
	
	private static final int MAX_DISTANCE = 3000;
	private static final int MIN_DISTANCE = 5;
	
	//private static final double MAX_SCALE = 1.5;
	//private static final double MIN_SCALE = 0.5;
	
	
	
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
	
	private static final int MAX_VELOCITY_COMPONENT = 10;
	private static final int MIN_VELOCITY_COMPONENT = -10;
	
	private static float getRandomVelocityComponent() {
		return (float) (MIN_VELOCITY_COMPONENT + Math.random() * (MAX_VELOCITY_COMPONENT - MIN_VELOCITY_COMPONENT));
	}
	
	private static Vector3f getRandomVelocity() {
		return new Vector3f(getRandomVelocityComponent(), getRandomVelocityComponent(), getRandomVelocityComponent());
	}
	
	
	Camera camera = new Camera();
	
	LinkedList<Entity> cubes = new LinkedList<>();
	
	LinkedList<MovableEntity> movingCubes = new LinkedList<>();
	
	private static final int CUBE_COUNT = 1000;
	private static final int MOVING_CUBE_COUNT = 3000;
	
	public void generateCubes(Model cubeModel) {
		for (int i = 0; i < CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			Vector3f randomRotation = getRandomRotation();
			//Vector3f randomColour = getRandomColour();
			//Vector3f randomVelocity = getRandomVelocity();
			//float randomScale = getRandomScale();
			cubes.add(new Entity(cubeModel, randomPosition, randomRotation, 1));
		}
		
		for (int i = 0; i < MOVING_CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			Vector3f randomRotation = getRandomRotation();
			//Vector3f randomColour = getRandomColour();
			Vector3f randomVelocity = getRandomVelocity();
			//float randomScale = getRandomScale();
			movingCubes.add(new MovableEntity(cubeModel, randomPosition, randomRotation, randomVelocity, 1));
		}
		
		cubes.addAll(movingCubes);
	}
	
	public Tester4() {
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
	
	private static final float NORMAL_ACCELERATION = 0.3f;
	
	private static final float RAPID_ACCELERATION = 20;
	
	private static final float MOVEMENT_SPEED = 9.5f;
	
	// State information for output
	float lastReportedDistanceTime;
	
	// The time interval between distance reports in seconds
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
		
		// Determine how quickly to accelerate
		if (isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
			cameraForwardSpeed += RAPID_ACCELERATION * deltaTime; // Accelerate faster!
		} else {
			cameraForwardSpeed += NORMAL_ACCELERATION * deltaTime; // Accelerate
		}
		
		// Camera movement controls and limits
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
		
		for (Entity cube : cubes) {
			
			// Bounding box collision detection - not correctly sized / adjusted for rotation
			if (Math.abs(camera.getPosition().z - cube.getPosition().z) < 0.5
					&& Math.abs(camera.getPosition().x - cube.getPosition().x) < 0.5
					&& Math.abs(camera.getPosition().y - cube.getPosition().y) < 0.5) {
				collided = true;
			}
			
			// Rotate the cubes!
			//cube.setRotX((float) (getTime()*100));
			//cube.setRotY((float) (getTime()*100));
			
		}
		
		// Finally move the camera forward each frame
		camera.moveForward(cameraForwardSpeed, (float) deltaTime);
		
		// Cubes bounce off of their container
		for (MovableEntity movEntity : movingCubes) {
			movEntity.tick((float) deltaTime);
			Vector3f position = movEntity.getPosition();
			Vector3f velocity = movEntity.getVelocity();
			if (Math.abs(position.x) > XY_LIMIT) {
				velocity.x = -velocity.x;
			}
			if (Math.abs(position.y) > XY_LIMIT) {
				velocity.y = -velocity.y;
			}
			if (Math.abs(position.z) > MAX_DISTANCE) {
				velocity.z = -velocity.z;
			}
		}
		
	}

	// TODO: Pass colours using some kind of Colour class
	//private static final Vector3f WIN_COLOUR = new Vector3f((float)0/255, (float)97/255, (float)32/255);
	
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
		
		for (Entity cube : cubes) {
			renderer.render(cube, camera, (float) getTime());
		}
	}

	@Override
	protected void cleanUpModels() {
		Models.getCubeModel().cleanUp();
	}
	
	

}