package camera;

import static org.lwjgl.glfw.GLFW.*;

import math.geometry.Vector2f;
import math.geometry.Vector3f;
import ui.Window2;

public abstract class CameraControls {
	
	// Positional movement
	public static final float MOVEMENT_SPEED = 60;
	
	// Rotational movement
	public static final float ROTATION_SPEED = 25;
	
	// Scale speed for faster movement
	public static final float FAST_MOVEMENT_SPEED_MULTIPLIER = 4.2f;
	public static final float FAST_ROTATION_SPEED_MULTIPLIER = 2.5f;
	
	// Degrees per virtual screen coordinate
	// Scales mouse movement to camera rotation
	private static final float MOUSE_MOVEMENT_SCALE = 0.05f;
	
	// Key mappings
	public static final int SPEED_MODIFIER_KEY = GLFW_KEY_LEFT_CONTROL;
	
	public static final int RIGHT_KEY = GLFW_KEY_D;
	public static final int LEFT_KEY = GLFW_KEY_A;
	
	public static final int UP_KEY = GLFW_KEY_PAGE_UP;
	public static final int DOWN_KEY = GLFW_KEY_PAGE_DOWN;
	
	public static final int FORWARD_KEY = GLFW_KEY_W;
	public static final int BACK_KEY = GLFW_KEY_S;
	
	
	// Holds the cursor position at the instant the mouse is pressed down
	private Vector2f mouseDownCursorPosition = new Vector2f();
	// Holds the camera rotation at the instant the mouse is pressed down
	private Vector3f mouseDownCameraRotation = new Vector3f();
	// Tracks if the mouse button relevant to camera movement is held down or not
	boolean mouseDown = false;
	
	
	// TODO: Sort the accessibility issue
	protected Camera camera;
	protected Window2 window;
	
	public CameraControls(Camera camera, Window2 window) {
		this.camera = camera;
		this.window = window;
	}
	
	public void move(float deltaTime) {
		
		float movementSpeed = MOVEMENT_SPEED;
		float rotationSpeed = ROTATION_SPEED;
		
		if (window.isKeyPressed(SPEED_MODIFIER_KEY)) {
			movementSpeed *= FAST_MOVEMENT_SPEED_MULTIPLIER;
			rotationSpeed *= FAST_ROTATION_SPEED_MULTIPLIER;
		}
		
		moveCamera(movementSpeed, deltaTime);
		rotateCamera(rotationSpeed, deltaTime);
	}
	
	public abstract void moveCamera(float speed, float deltaTime);
	
	private void rotateCamera(float speed, float deltaTime) {
		
		// Keyboard controls
		// Pitch
		if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			camera.increasePitch(speed, deltaTime);
		} else if (window.isKeyPressed(GLFW_KEY_UP)) {
			camera.increasePitch(-speed, deltaTime);
		}
		
		// Yaw
		if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			camera.increaseYaw(speed, deltaTime);
		} else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			camera.increaseYaw(-speed, deltaTime);
		}
		
		
		// Mouse controls
		if (mouseDown) {
			
			Vector2f cursorPosition = window.getCursorPosition();
			
			// Find the mouse movement vector
			Vector2f mouseDelta = Vector2f.sub(mouseDownCursorPosition, cursorPosition);
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
		// Record the mouse cursor position and camera rotation
		mouseDownCursorPosition = window.getCursorPosition().getCopy();
		mouseDownCameraRotation = camera.getRotation().getCopy();
		System.out.println("Mouse Down Cursor Position: " + mouseDownCursorPosition);
	}
	
	public void onMouseUp() {
		mouseDown = false;
		window.enableCursor();
	}
	

}
