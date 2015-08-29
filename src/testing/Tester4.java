package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import entities.Camera;
import entities.Entity;
import entities.MovableEntity;
import math.Matrix4f;
import math.Vector3f;
import model.CubeModel;
import model.Model;
import model.Models;
import render.Renderer;
import shaders.ShaderProgram;
import util.MathUtils;

public class Tester4 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester4().run();
	}

	
	
	// Input callbacks
	@Override
	public void onKeyPressed(int keyCode) {
		if (keyCode == GLFW_KEY_SPACE) {
			reset();
		}
	}

	@Override
	public void onKeyReleased(int keyCode) {
		
	}
	
	
	// Constants
	
	// Output
	private static final float DISTANCE_REPORT_INTERVAL = 1;
	private static final int LOW_FRAMERATE = 30;
	
	// Spatial dimensions
	private static final int XY_LIMIT = 50;
	private static final int MAX_DISTANCE = 3000;
	private static final int MIN_DISTANCE = 5;
	
	// Cube count
	private static final int CUBE_COUNT = 1000;
	private static final int MOVING_CUBE_COUNT = 3000;
	
	// Cube movement
	private static final int MAX_VELOCITY_COMPONENT = 10;
	private static final int MIN_VELOCITY_COMPONENT = -10;
	
	// Camera movement
	private static final float NORMAL_ACCELERATION = 0.3f;
	private static final float RAPID_ACCELERATION = 20;
	
	private static final float MOVEMENT_SPEED = 9.5f;
	
	
	// Entity configuration methods
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
	
	private static float getRandomVelocityComponent() {
		return (float) MathUtils.randRange(MIN_VELOCITY_COMPONENT, MAX_VELOCITY_COMPONENT);
	}
	
	private static Vector3f getRandomVelocity() {
		return new Vector3f(getRandomVelocityComponent(), getRandomVelocityComponent(), getRandomVelocityComponent());
	}
	
	/*
	private static Vector3f getRandomColour() {
		return new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	*/
	
	
	Camera camera = new Camera();
	
	LinkedList<Entity> cubes = new LinkedList<>();
	LinkedList<MovableEntity> movingCubes = new LinkedList<>();
	
	Entity startSquare = new Entity(Models.getSquareModel(), new Vector3f(0, 0, -MIN_DISTANCE));
	
	public void generateCubes(Model cubeModel) {
		for (int i = 0; i < CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			Vector3f randomRotation = getRandomRotation();
			//Vector3f randomColour = getRandomColour();
			cubes.add(new Entity(cubeModel, randomPosition));
		}
		
		for (int i = 0; i < MOVING_CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			Vector3f randomRotation = getRandomRotation();
			//Vector3f randomColour = getRandomColour();
			Vector3f randomVelocity = getRandomVelocity();
			movingCubes.add(new MovableEntity(cubeModel, randomPosition, randomRotation, randomVelocity));
		}
		
		cubes.addAll(movingCubes);
		
	}
	
	// Should be passing in model here
	public ArrayList<Vector3f> getModelVertexPositions() {
		
		ArrayList<Vector3f> vertexPositions = new ArrayList<>();
		
		float[] cubeVertices = CubeModel.vertexPositions;
		
		for (int i = 0; i < cubeVertices.length; i+=3) {
			Vector3f pos = new Vector3f(cubeVertices[i], cubeVertices[i+1], cubeVertices[i+2]);
			vertexPositions.add(pos);
		}
		
		return vertexPositions;
	}
	
	public float[] vertexListToArray(List<Vector3f> vertexList) {
		
		float[] vertexPositions = new float[vertexList.size() * 3];
		
		int index = 0;
		for (Vector3f pos : vertexList) {
			vertexPositions[index] = pos.x;
			vertexPositions[index+1] = pos.y;
			vertexPositions[index+2] = pos.z;
			index += 3;
		}
		
		return vertexPositions;
		
	}
	
	public Model generateCubeFieldModel() {
		
		Model cubeField = new Model();
		
		CubeModel cubeModel = Models.getCubeModel();
		
		int cubeCount = 125;
		
		ArrayList<Vector3f> allVertexPositionsList = new ArrayList<>();
		
		int[] indices = new int[CubeModel.indices.length * cubeCount];
		float[] vertexColours = new float[CubeModel.vertexColours.length * cubeCount];
		float[] vertexPositions;
		
		// Random colours
		for (int i = 0; i < vertexColours.length; i++) {
			vertexColours[i] = (float) Math.random();
		}
		
		for (int i = 0; i < cubeCount; i++) {
			
			Entity cube = new Entity(cubeModel, new Vector3f((i%5) * 1.5f, (i/5)%5 * 1.5f, (i/25) * 1.5f));
			Matrix4f modelTransform = cube.getModelMatrix();
			//modelTransform.scale(new Vector3f(0.2f, 0.2f, 0.2f));
			ArrayList<Vector3f> vertexPositionsList = getModelVertexPositions();
			
			for (Vector3f pos : vertexPositionsList) {
				pos.multiply(modelTransform);
			}
			
			allVertexPositionsList.addAll(vertexPositionsList);
			
			
			// Index code
			// There will be a bunch more vertices
			int valueOffset = i * CubeModel.vertexPositions.length / 3;
			
			// The index buffer is now for multiple cubes
			int placementOffset = i * CubeModel.indices.length;
			
			for (int j = 0; j < CubeModel.indices.length; j++) {
				indices[j + placementOffset] = CubeModel.indices[j] + valueOffset;
			}
			
			
			
			
			
		}
		
		vertexPositions = vertexListToArray(allVertexPositionsList);
		
		System.out.println("Vertex Positions Length: " + vertexPositions.length);
		System.out.println("Vertex Colours Length: " + vertexColours.length);
		System.out.println("Indices Length: " + indices.length);
		
		for (int i = 0; i < vertexPositions.length; i += 3) {
			System.out.println(vertexPositions[i] + ", " + vertexPositions[i+1] + ", " + vertexPositions[i+2] + ",");
		}
		
		for (int i = 0; i < indices.length; i += 3) {
			System.out.println(indices[i] + ", " + indices[i+1] + ", " + indices[i+2] + ",");
		}
		
		cubeField.addVertexAttrib(vertexPositions, ShaderProgram.POSITION_ATTRIB_LOCATION, 3);
		cubeField.addVertexAttrib(vertexColours, ShaderProgram.COLOUR_ATTRIB_LOCATION, 3);
		cubeField.setIBOData(indices);
		cubeField.unbindVAO();
		
		return cubeField;
		
		
	}
	
	public Tester4() {
		CubeModel cubeModel = Models.getCubeModel(); // Must be cleaned up at the end
		Model cubeFieldModel = generateCubeFieldModel();
		generateCubes(cubeFieldModel);
		reset();
	}
	
	private void reset() {
		collided = false;
		won = false;
		cameraForwardSpeed = 5;
		camera.setPosition(0, 0, 0);
	}
	
	// Some 'game play' state information
	float cameraForwardSpeed;
	boolean collided;
	boolean won;
	
	// State information for output
	float lastReportedDistanceTime;
	
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
			
			
		}
		
		// Finally move the camera forward each frame
		camera.moveForward(cameraForwardSpeed, deltaTime);
		
		// Cubes bounce off of their container
		for (MovableEntity movEntity : movingCubes) {
			movEntity.tick(deltaTime);
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
		
		renderer.render(startSquare, camera, (float) getTime());
	}
	

}
