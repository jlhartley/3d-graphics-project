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
		
	}

	@Override
	public void onKeyReleased(int keyCode) {
		
	}
	
	
	
	// Either side of (0,0)
	// E.g +5 to -5
	private static final int XY_LIMIT = 50;
	
	private static final int MAX_DISTANCE = 200;
	private static final int MIN_DISTANCE = 5;
	
	private static final double MAX_SCALE = 1.5;
	private static final double MIN_SCALE = 0.5;
	
	private static Vector3f getRandomPosition() {
		return new Vector3f(getRandomXYPosition(), getRandomXYPosition(), getRandomZPosition());
	}
	
	// Get an X or Y position that ranges from -XY_LIMIT to XY_LIMIT
	private static float getRandomXYPosition() {
		return (float) (-XY_LIMIT + Math.random() * 2 * XY_LIMIT);
	}
	
	private static float getRandomZPosition() {
		return (float) (-MIN_DISTANCE - Math.random() * (MAX_DISTANCE-MIN_DISTANCE));
	}
	
	private Vector3f getRandomRotation() {
		return new Vector3f((float) (Math.random() * 360), (float) (Math.random() * 360), (float) (Math.random() * 360));
	}
	
	// Get a random scale from MIN_SCALE to MAX_SCALE
	private static float getRandomScale() {
		return (float) (MIN_SCALE + Math.random() * (MAX_SCALE-MIN_SCALE));
	}
	
	private static Vector3f getRandomColour() {
		return new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	
	private static Vector3f getRandomVelocity() {
		return new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	
	
	static final int CUBE_COUNT = 600;
	
	LinkedList<Entity3D> cubes = new LinkedList<>();
	
	public void generateCubes(Model cubeModel) {
		for (int i = 0; i < CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			Vector3f randomColour = getRandomColour();
			Vector3f randomRotation = getRandomRotation();
			Vector3f randomVelocity = getRandomVelocity();
			float randomScale = getRandomScale();
			cubes.add(new Entity3D(cubeModel, randomPosition, randomRotation, randomScale));
		}
	}

	Camera camera = new Camera();
	
	public Tester3() {
		
		CubeModel cubeModel = Models.getCubeModel(); // Must be cleaned up at the end
		
		generateCubes(cubeModel);
		
		
		
	}
	
	
	
	
	float speed = 5;
	
	boolean collided;
	boolean won;
	
	float lastReportedDistanceTime;
	
	@Override
	protected void logic(double deltaTime) {
		
		double frameRate = 1/deltaTime;
		
		if (frameRate < 55) {
			System.out.println("Low framerate: " + frameRate);
		}
		
		if (isKeyPressed(GLFW_KEY_SPACE)) {
			collided = false;
			won = false;
			speed = 5;
			camera.setPosition(new Vector3f(0, 0, 0));
		}
		
		if (collided || won) {
			return;
		}
		
		float distance = camera.getPosition().z;
		
		if (distance < -4000) {
			won = true;
		}
		
		if (lastReportedDistanceTime > 1) {
			System.out.println("Distance: " + distance);
			lastReportedDistanceTime = 0;
		} else {
			lastReportedDistanceTime += deltaTime;
		}
		
		
		if (isKeyPressed(GLFW_KEY_C)) {
			camera.setPosition(new Vector3f(0, 0, -3900));
		}
		
		
		
		if (isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
			camera.setYaw(180); // Look backwards
		} else {
			camera.setYaw(0);
		}
		
		if (isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
			speed += 20 * deltaTime; // Accelerate faster!
		} else {
			speed += 0.3 * deltaTime; // Accelerate
		}
		
		camera.moveForward(speed, (float) deltaTime);
		
		if (isKeyPressed(GLFW_KEY_UP) && camera.getPosition().y < 3) {
			camera.moveUp(1.5f, (float) deltaTime);
		} 
		
		if (isKeyPressed(GLFW_KEY_DOWN) && camera.getPosition().y > -3) {
			camera.moveDown(1.5f, (float) deltaTime);
		}
		
		if (isKeyPressed(GLFW_KEY_RIGHT) && camera.getPosition().x < 3) {
			camera.moveRight(1.5f, (float) deltaTime);
		}
		
		if (isKeyPressed(GLFW_KEY_LEFT) && camera.getPosition().x > -3) {
			camera.moveLeft(1.5f, (float) deltaTime);
		}
		
		for (Entity3D cube : cubes) {
			if (Math.abs(camera.getPosition().z - cube.getPosition().z) < 0.25
					&& Math.abs(camera.getPosition().x - cube.getPosition().x) < 0.25
					&& Math.abs(camera.getPosition().y - cube.getPosition().y) < 0.25) {
				collided = true;
			}
			
			// Rotate the cubes!
			cube.setRotX((float) (getTime()*100));
			cube.setRotY((float) (getTime()*100));
			
		}
		
		
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
