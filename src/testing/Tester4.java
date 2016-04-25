package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedList;
import java.util.List;

import camera.Camera;
import entities.Entity;
import entities.MovingEntity;
import input.Key;
import input.MouseButton;
import lighting.Light;
import math.geometry.Vector2f;
import math.geometry.Vector3f;
import model.Model;
import model.Models;
import render.ProjectionType;
import render.Renderer;
import ui.UIWindow;
import math.MathUtils;

public class Tester4 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester4().run();
	}

	
	@Override
	public void onCameraControlTypeSet(boolean relative) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraPositionSet(Vector3f newPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraRotationSet(Vector3f newRotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeMultiplierSet(double timeMultiplier) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCursorPositionChanged(Vector2f cursorPosition) {
		// TODO Auto-generated method stub
		
	}
	
	
	// Input callbacks
	@Override
	public void onKeyPressed(Key key) {
		if (key == Key.SPACE) {
			reset();
		} if (key == Key.O) {
			switchProjection(ProjectionType.ORTHOGRAPHIC);
		} else if (key == Key.P) {
			switchProjection(ProjectionType.PERSPECTIVE);
		}
	}

	@Override
	public void onKeyReleased(Key key) {
		
	}
	
	
	@Override
	public void onMouseDown(MouseButton mouseButton) {
		
	}

	@Override
	public void onMouseUp(MouseButton mouseButton) {
		
	}
	
	
	// Constants
	
	// Output
	private static final float DISTANCE_REPORT_INTERVAL = 1;
	//private static final int LOW_FRAMERATE = 30;
	
	// Spatial dimensions
	private static final int XY_LIMIT = 50;
	private static final int MAX_DISTANCE = 3000;
	private static final int MIN_DISTANCE = 5;
	
	// Cube count
	private static final int CUBE_COUNT = 200;
	private static final int MOVING_CUBE_COUNT = 200;
	
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
	
	Light light = new Light();
	
	List<Entity> entities = new LinkedList<>();
	List<MovingEntity> movingEntities = new LinkedList<>();
	
	Entity startSquare = new Entity(Models.getSquareModel(), new Vector3f(0, 0, -MIN_DISTANCE));
	
	Entity dragon = new Entity(Models.getDragonModel(), new Vector3f(0, 0, -100));
	
	public void generateEntities(Model model) {
		for (int i = 0; i < CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			//Vector3f randomColour = getRandomColour();
			entities.add(new Entity(model, randomPosition));
		}
		
		for (int i = 0; i < MOVING_CUBE_COUNT; i++) {
			Vector3f randomPosition = getRandomPosition();
			Vector3f randomRotation = getRandomRotation();
			//Vector3f randomColour = getRandomColour();
			Vector3f randomVelocity = getRandomVelocity();
			movingEntities.add(new MovingEntity(model, randomPosition, randomRotation, randomVelocity));
		}
		
		entities.addAll(movingEntities);
		
	}
	
	public Tester4() {
		Model cubeModel = Models.getCubeModel();
		Model cubeGridModel = Models.getCubeGridModel();
		Model uvSphereModel = Models.getUVsphereModel();
		Model torusModel = Models.getTorusModel();
		Model explodedCubeModel = Models.getExplodedCubeModel();
		
		/*
		
		ModelBuilder modelBuilder = new ModelBuilder();
		int modelCount = 8;
		for (int i = 0; i < modelCount; i++) {
			Vector3f pos = new Vector3f((i%2) * 5, (i/2)%2 * 5, (i/4) * 5);
			// Correct offset
			pos.translate(new Vector3f(-2.5f, -2.5f, -2.5f));
			
			Entity entity = new Entity(Models.getIcosphereModel(), pos);
			modelBuilder.addEntity(entity);
		}
		Model customModel = modelBuilder.build();
		
		generateEntities(customModel);
		
		*/
		
		generateEntities(cubeModel);
		
		generateEntities(cubeGridModel);
		
		generateEntities(uvSphereModel);
		
		generateEntities(torusModel);
		
		generateEntities(explodedCubeModel);
		
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
	
	// State information for output
	float lastReportedDistanceTime;
	
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
		if (isKeyPressed(GLFW_KEY_RIGHT) && camera.getPosition().x < XY_LIMIT) {
			camera.moveX(MOVEMENT_SPEED, deltaTime);
		}
		
		if (isKeyPressed(GLFW_KEY_LEFT) && camera.getPosition().x > -XY_LIMIT) {
			camera.moveX(-MOVEMENT_SPEED, deltaTime);
		}
		
		if (isKeyPressed(GLFW_KEY_UP) && camera.getPosition().y < XY_LIMIT) {
			camera.moveY(MOVEMENT_SPEED, deltaTime);
		} 
		
		if (isKeyPressed(GLFW_KEY_DOWN) && camera.getPosition().y > -XY_LIMIT) {
			camera.moveY(-MOVEMENT_SPEED, deltaTime);
		}
		
		for (Entity entity : entities) {
			// Bounding box collision detection - not correctly sized / adjusted for rotation
			if (Math.abs(camera.getPosition().z - entity.getPosition().z) < 0.5
					&& Math.abs(camera.getPosition().x - entity.getPosition().x) < 0.5
					&& Math.abs(camera.getPosition().y - entity.getPosition().y) < 0.5) {
				collided = true;
			}
			// Rotate entity around y
			entity.setRotY((float) (getTime() * 100));
		}
		
		// Moving entities bounce off of their container
		for (MovingEntity movEntity : movingEntities) {
			movEntity.move(deltaTime);
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
		
		// Rotate the dragon
		dragon.setRotY((float) (getTime() * 100));
		
		Vector3f lightPosition = light.getPosition();
		lightPosition.z = (float) (-(Math.sin(getTime() / 2) + 1) * MAX_DISTANCE / 2);
		
		// Finally move the camera forward each frame
		camera.moveZ(-cameraForwardSpeed, deltaTime);
		
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
		
		for (Entity entity : entities) {
			renderer.render(entity, camera);
		}
		
		renderer.render(startSquare, camera);
		
		renderer.render(dragon, camera);
		
	}


	@Override
	protected void updateUI(UIWindow window) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void close() {
		
	}


	@Override
	public void onNew() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAddPlanet(Vector3f velocity, boolean autoVelocity, float mass) {
		// TODO Auto-generated method stub
		
	}

	

}
