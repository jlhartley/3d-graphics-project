package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import entities.Camera;
import entities.Entity;
import entities.MovableEntity;
import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;
import model.Model;
import model.Models;
import render.Renderer;
import util.MathUtils;

public class Tester5 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester5().run();
	}

	@Override
	public void onKeyPressed(int keyCode) {
		
	}

	@Override
	public void onKeyReleased(int keyCode) {

	}
	
	
	// Constants
	
	// Output
	private static final float POSITION_REPORT_INTERVAL = 1;
	private static final int LOW_FRAMERATE = 30;
	
	// Spatial dimensions
	private static final int XY_LIMIT = 50;
	private static final int MAX_DISTANCE = 3000;
	private static final int MIN_DISTANCE = 5;
	
	// Cube count
	private static final int CUBE_COUNT = 1000;
	private static final int MOVING_CUBE_COUNT = 50;
	
	// Cube movement
	private static final int MAX_VELOCITY_COMPONENT = 10;
	private static final int MIN_VELOCITY_COMPONENT = -10;
	
	// Camera movement
	private static final float NORMAL_MOVEMENT_SPEED = 9.5f;
	private static final float FAST_MOVEMENT_SPEED = 15;
	private static final float ROTATION_MOVEMENT_SPEED = 30; // Degrees per second
	
	
	
	Camera camera = new Camera();
	
	List<MovableEntity> planets = new ArrayList<>();
	
	private void addPlanets() {
		
		Model sphereModel = Models.getUVsphereModel();
		
		int planetCount = 10;
		
		for (int i = 0; i < planetCount; i++) {
			Vector3f pos = getPlanetPosition();
			planets.add(new MovableEntity(sphereModel, pos, new Vector3f(), new Vector3f()));
		}
		
	}
	
	private float calculateOrbitRadius(Vector3f pos) {
		return (float) Math.sqrt(pos.x*pos.x + pos.z*pos.z);
	}
	
	private Vector3f getPlanetPosition() {
		float x = (float) MathUtils.randRange(-20, 20);
		float y = 0; // All planets in same y-plane
		float z = (float) MathUtils.randRange(-20, 20);
		return new Vector3f(x, y, z);
	}
	
	
	Entity sun = new Entity(Models.getUVsphereModel(), new Vector3f(), new Vector3f(), 3);
	
	public Tester5() {
		// Place the camera up and back from the origin
		Vector3f initialCameraPos = new Vector3f(0, 50, 50);
		camera.setPosition(initialCameraPos);
		camera.setPitch(45); // Point camera downwards
		
		addPlanets();
		//MovableEntity planet = new MovableEntity(Models.getUVsphereModel(), new Vector3f(), new Vector3f(), new Vector3f(), 2);
		//planets.add(planet);
	}
	
	
	
	private void updatePlanetVelocity(MovableEntity planet) {
		
	}
	
	//private float calcTimePeriod(float radius) {
		//return 
	//}
	
	private void updatePlanetPosition(Entity planet, float deltaTime) {
		//Matrix4f transform = Matrix4f.identity();
		//transform.rotate(deltaTime * 2, MathUtils.Y_AXIS);
		
		Vector3f planetPos = planet.getPosition();
		//Vector4f vec4 = new Vector4f(planetPos.x, planetPos.y, planetPos.z, 1);
		
		//vec4.multiply(transform);
		
		//planet.setPosition(vec4.x, vec4.y, vec4.z);
		
		float radius = calculateOrbitRadius(planetPos);
		// Full rotation every 3.6 seconds
		float x = (float) (radius * Math.cos(Math.toRadians(getTime() * 2000 / radius)));
		float z = (float) -(radius * Math.sin(Math.toRadians(getTime() * 2000 / radius)));
		planet.setPosition(x, 0, z);
	}
	
	
	
	
	@Override
	protected void logic(float deltaTime) {
		displayFramerate(deltaTime);
		moveCamera(deltaTime);
		
		for (MovableEntity planet : planets) {
			//updatePlanetVelocity(planet);
			//planet.tick(deltaTime);
			updatePlanetPosition(planet, deltaTime);
		}
	}
	
	private void displayFramerate(float deltaTime) {
		float frameRate = 1/deltaTime;
		
		if (frameRate < LOW_FRAMERATE) {
			System.out.println("Low Framerate: " + frameRate + " FPS");
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
		
		renderer.render(sun, camera);
		
		for (Entity planet : planets) {
			renderer.render(planet, camera);
		}
		
	}
	
	

}
