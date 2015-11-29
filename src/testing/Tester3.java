package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import camera.Camera;
import entities.Entity;
import lighting.Light;
import math.geometry.Vector3f;
import model.Model;
import model.ModelBuilder;
import model.Models;
import render.ProjectionType;
import render.Renderer;
import math.MathUtils;
import window.MouseButton;

public class Tester3 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester3().run();
	}

	@Override
	public void onKeyPressed(int keyCode) {
		if (keyCode == GLFW_KEY_SPACE) {
			reset();
		} if (keyCode == GLFW_KEY_O) {
			switchProjection(ProjectionType.ORTHOGRAPHIC);
		} else if (keyCode == GLFW_KEY_P) {
			switchProjection(ProjectionType.PERSPECTIVE);
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
	
	
	Camera camera = new Camera();
	
	List<Entity> cubes = new ArrayList<>();
	
	Entity cubeField;
	
	Light light = new Light();
	
	private static final int CUBE_COUNT = 300000;
	
	public void generateCubes(Model cubeModel) {
		
		for (int i = 0; i < CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			Vector3f randomRotation = getRandomRotation();
			cubes.add(new Entity(cubeModel, randomPosition, randomRotation));
		}
		
		// Sort front-to-back for early depth testing optimisation
		// Could sort back-to-front and disable depth testing as 
		// an additional optimisation (painter's algorithm)
		cubes.sort(new Comparator<Entity>() {

			@Override
			public int compare(Entity entity1, Entity entity2) {
				
				if (entity1.getPosition().z < entity2.getPosition().z) {
					// If entity1 is further away, it should be drawn later
					return 1;
				} else {
					return -1;
				}
				
				// Very unlikely that two random z value will be exactly the same
				//return 0;
			}
		});
		
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
	
	// State information
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
	
	@Override
	protected void logic(float deltaTime) {
		
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
			
		}
		
		// Rotate the field
		//cubeField.setRotZ(getTime() * 20);
		
		Vector3f lightPosition = light.getPosition();
		lightPosition.z = (float) (-(Math.sin(getTime() / 2) + 1) * MAX_DISTANCE / 2);
		
		// Finally move the camera forward each frame
		camera.moveForward(cameraForwardSpeed, deltaTime);
		
	}
	
	
	private static final Vector3f WIN_COLOUR = new Vector3f((float)0/255, (float)97/255, (float)32/255);
	private static final Vector3f COLLISION_COLOUR = new Vector3f(0.9f, 0.2f, 0.2f);
	
	@Override
	protected void render(Renderer renderer) {
		
		renderer.enableLighting();
		renderer.setLightSource(light);
		
		if (collided) {
			renderer.setClearColour(COLLISION_COLOUR);
		} else if (won) {
			renderer.setClearColour(WIN_COLOUR);
		} else {
			renderer.setClearColour(0, 0, 0);
		}
		
		renderer.render(cubeField, camera);
	}
	
	

}
