package camera;

import static org.lwjgl.glfw.GLFW.*;

import math.geometry.Vector2f;
import math.geometry.Vector3f;
import window.Window;

public class CameraControls {
	
	// Positional movement
	private static final float MOVEMENT_SPEED = 45;
	
	// Rotational movement
	private static final float ROTATION_SPEED = 30;
	
	// Scale speed for faster movement
	private static final float FAST_MOVEMENT_SPEED_MULTIPLIER = 4;
	private static final float FAST_ROTATION_SPEED_MULTIPLIER = 1.5f;
	
	// Degrees per virtual screen coordinate
	// Scales mouse movement to camera rotation
	private static final float MOUSE_MOVEMENT_SCALE = 0.05f;
	
	// Holds the mouse position at the instant the mouse is pressed down
	private Vector2f mouseDownPosition = new Vector2f();
	// Holds the camera rotation at the instant the mouse is pressed down
	private Vector3f mouseDownCameraRotation = new Vector3f();
	// Tracks if the mouse button relevant to camera movement is held down or not
	boolean mouseDown = false;
	
	private Camera camera;
	private Window window;
	
	private Vector3f cameraVelocity = new Vector3f();
	
	// Relative controls by default
	private ControlType controlType = ControlType.RELATIVE;
	
	public void setControlType(ControlType controlType) {
		this.controlType = controlType;
	}
	
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
			movementSpeed *= FAST_MOVEMENT_SPEED_MULTIPLIER;
			rotationSpeed *= FAST_ROTATION_SPEED_MULTIPLIER;
		}
		
		rotateCamera(rotationSpeed, deltaTime);
		
		if (controlType == ControlType.ABSOLUTE) {
			moveCameraAbsolute(movementSpeed, deltaTime);
		} else {
			moveCameraRelative(movementSpeed, deltaTime);
		}
		
	}
	
	private void moveCameraAbsolute(float movementSpeed, float deltaTime) {
		
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
		
	}
	
	private void moveCameraRelative(float movementSpeed, float deltaTime) {
		
		Vector3f cameraPosition = camera.getPosition();
		Vector3f cameraRotation = camera.getRotation();
		
		//float pitch = cameraRotation.x;
		//float yaw = cameraRotation.y;
		
		
		// Project the movement speed onto the x-z plane
		float projectedMovementSpeed = (float) (movementSpeed * Math.cos(Math.toRadians(cameraRotation.x)));
		
		// Movement in x and z can now be considered as an orthographic projection
		// Angle is z, therefore x is sin and z is cosine (against the convention)
		cameraVelocity.x = (float) (projectedMovementSpeed * Math.sin(Math.toRadians(cameraRotation.y)));
		// z is negated since z decreases into the screen (with depth, when considering perspective)
		cameraVelocity.z = -(float) (projectedMovementSpeed * Math.cos(Math.toRadians(cameraRotation.y)));
		
		// Rotation around x increases as the camera is pointed down. Since we want to move down in y
		// more as the camera is pointed further down, the rotation needs to be negated.
		// Also note: sin(-theta)=-sin(theta)
		cameraVelocity.y = (float) (movementSpeed * Math.sin(Math.toRadians(-cameraRotation.x)));
		
		
		
		if (window.isKeyPressed(GLFW_KEY_W)) {
			
			cameraPosition.x += cameraVelocity.x * deltaTime;
			cameraPosition.y += cameraVelocity.y * deltaTime;
			cameraPosition.z += cameraVelocity.z * deltaTime;
			
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			
			cameraPosition.x -= cameraVelocity.x * deltaTime;
			cameraPosition.y -= cameraVelocity.y * deltaTime;
			cameraPosition.z -= cameraVelocity.z * deltaTime;
			
		}
		
	}
	
	private void rotateCamera(float rotationSpeed, float deltaTime) {
		
		// Keyboard controls
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
		
		
		// Mouse controls
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
