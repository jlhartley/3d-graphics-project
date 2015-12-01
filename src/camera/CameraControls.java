package camera;

import static org.lwjgl.glfw.GLFW.*;

import math.geometry.Vector2f;
import math.geometry.Vector3f;
import window.Window;

public class CameraControls {
	
	// Positional movement
	private static final float MOVEMENT_SPEED = 35;
	
	// Rotational movement
	private static final float ROTATION_SPEED = 30;
	
	// Scale speed for faster movement
	private static final float FAST_SPEED_MULTIPLIER = 3;
	
	private static final float MOUSE_MOVEMENT_SCALE = 0.05f;
	
	// Holds the mouse position at the instant the mouse is pressed down
	private Vector2f mouseDownPosition = new Vector2f();
	// Holds the camera rotation at the instant the mouse is pressed down
	private Vector3f mouseDownCameraRotation = new Vector3f();
	// Tracks if the mouse button relevant to camera movement is held down or not
	boolean mouseDown = false;
	
	private Camera camera;
	private Window window;
	
	private Vector3f cameraTrajectory = new Vector3f();
	
	public enum ControlType {
		ABSOLUTE,
		RELATIVE
	}
	
	public CameraControls(Camera camera, Window window) {
		this.camera = camera;
		this.window = window;
	}
	
	
	public void moveCamera(float deltaTime) {
		
		float movementSpeed = MOVEMENT_SPEED;
		float rotationSpeed = ROTATION_SPEED;
		
		if (window.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
			movementSpeed *= FAST_SPEED_MULTIPLIER;
			rotationSpeed *= FAST_SPEED_MULTIPLIER;
		}
		
		/*
		// Positional controls
		// Forward and back
		if (window.isKeyPressed(GLFW_KEY_W)) {
			camera.moveForward(movementSpeed, deltaTime);
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			camera.moveBack(movementSpeed, deltaTime);
		}
		
		// Right and left
		if (window.isKeyPressed(GLFW_KEY_D)) {
			camera.moveRight(movementSpeed, deltaTime);
		} else if (window.isKeyPressed(GLFW_KEY_A)) {
			camera.moveLeft(movementSpeed, deltaTime);
		}
		
		// Up and down
		if (window.isKeyPressed(GLFW_KEY_PAGE_UP)) {
			camera.moveUp(movementSpeed, deltaTime);
		} else if (window.isKeyPressed(GLFW_KEY_PAGE_DOWN)){
			camera.moveDown(movementSpeed, deltaTime);
		}
		*/
		
		
		// Rotational controls
		// Pitch
		if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			camera.increasePitch(rotationSpeed, deltaTime);
		} else if (window.isKeyPressed(GLFW_KEY_UP)) {
			camera.decreasePitch(rotationSpeed, deltaTime);
		}
		
		// Yaw
		if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			camera.increaseYaw(rotationSpeed, deltaTime);
		} else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			camera.decreaseYaw(rotationSpeed, deltaTime);
		}
		
		rotateCamera();
		
		Vector3f cameraRotation = camera.getRotation();
		
		//float pitch = cameraRotation.x;
		
		//float yaw = cameraRotation.y;
		
		
		
		
		float projectedMovementSpeed = (float) (movementSpeed * Math.cos(Math.toRadians(cameraRotation.x)));
		
		cameraTrajectory.x = (float) (projectedMovementSpeed * Math.sin(Math.toRadians(cameraRotation.y)));
		
		cameraTrajectory.z = (float) (projectedMovementSpeed * Math.cos(Math.toRadians(cameraRotation.y)));
		
		
		cameraTrajectory.y = (float) (movementSpeed * Math.sin(Math.toRadians(-cameraRotation.x)));
		
		
		// Rotation is from z
		
		//cameraTrajectory.x = (float) (movementSpeed * Math.sin(Math.toRadians(cameraRotation.y)));
		
		
		// sin(-theta)=-sin(theta)
		
		// Pitch increases the more you point down
		
		//cameraTrajectory.y = (float) (movementSpeed * Math.sin(Math.toRadians(-cameraRotation.x * 2)));
		
		//cameraTrajectory.y = (float) (movementSpeed * Math.sin(Math.toRadians(-cameraRotation.x)));
		
		
		//cameraTrajectory.z = (float) (movementSpeed * Math.cos(Math.toRadians(cameraRotation.y)));
		
		//cameraTrajectory.z = (float) (movementSpeed * Math.sin(Math.toRadians(cameraRotation.x)));
		
		//cameraTrajectory.z = (float) (movementSpeed * Math.cos(Math.toRadians(cameraRotation.y)) + Math.sin(Math.toRadians(cameraRotation.x)) );
		
		
		
		if (window.isKeyPressed(GLFW_KEY_W)) {
			
			camera.moveRight(cameraTrajectory.x, deltaTime);
			camera.moveUp(cameraTrajectory.y, deltaTime);
			camera.moveForward(cameraTrajectory.z, deltaTime);
			
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			
			camera.moveLeft(cameraTrajectory.x, deltaTime);
			camera.moveDown(cameraTrajectory.y, deltaTime);
			camera.moveBack(cameraTrajectory.z, deltaTime);
			
		}
		
	}
	
	private void rotateCamera() {
		
		// Mouse control
		if (mouseDown) {
			
			Vector2f mousePosition = window.getMousePosition();
			
			// Find the mouse movement vector
			Vector2f mouseDelta = Vector2f.sub(mouseDownPosition, mousePosition);
			mouseDelta.scale(MOUSE_MOVEMENT_SCALE);
			
			float mouseDownYaw = mouseDownCameraRotation.y;
			float mouseDownPitch = mouseDownCameraRotation.x;
			
			// Adjust the camera rotation when the mouse was pressed 
			// by an amount proportional to the mouseDelta vector 
			camera.setYaw(mouseDownYaw - mouseDelta.x);
			camera.setPitch(mouseDownPitch + mouseDelta.y);
		}
		
	}

	public void onMouseDown() {
		mouseDown = true;
		// Allow endless cursor movement
		window.disableCursor();
		// Record the mouse position and camera rotation
		mouseDownPosition = window.getMousePosition().getCopy();
		mouseDownCameraRotation = camera.getRotation().getCopy();
		System.out.println("Mouse Down Position: " + mouseDownPosition);
	}
	
	public void onMouseUp() {
		mouseDown = false;
		window.enableCursor();
	}
	

}
