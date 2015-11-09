package camera;

import util.MathUtils;
import math.Matrix4f;
import math.Vector3f;

public class Camera {
	
	// Position in world space - x, y and z
	private Vector3f position;
	
	// Rotation
	private float pitch; // Rotation in x-axis
	private float yaw; // Rotation in y-axis
	private float roll; // Rotation in z-axis
	
	
	// Telescoping constructors
	// Place the camera at the origin with no pitch, yaw or roll
	public Camera() {
		this(new Vector3f());
	}
	
	// Place the camera at the specified position with no pitch, yaw or roll
	public Camera(Vector3f position) {
		this(position, 0, 0, 0);
	}
	
	// Place the camera at the specified position with the given pitch, yaw and roll
	public Camera(Vector3f position, float pitch, float yaw, float roll) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	
	// Movement controls
	public void moveUp(float speed, float deltaTime) {
		position.y += speed * deltaTime;
	}
	
	public void moveDown(float speed, float deltaTime) {
		position.y -= speed * deltaTime;
	}
	
	public void moveForward(float speed, float deltaTime) {
		position.z -= speed * deltaTime;
	}
	
	public void moveBack(float speed, float deltaTime) {
		position.z += speed * deltaTime;
	}
	
	public void moveRight(float speed, float deltaTime) {
		position.x += speed * deltaTime;
	}
	
	public void moveLeft(float speed, float deltaTime) {
		position.x -= speed * deltaTime;
	}
	
	
	// Rotational controls
	// Speed in degrees per second
	public void increasePitch(float speed, float deltaTime) {
		pitch += speed * deltaTime;
	}
	
	public void decreasePitch(float speed, float deltaTime) {
		pitch -= speed * deltaTime;
	}
	
	public void increaseYaw(float speed, float deltaTime) {
		yaw += speed * deltaTime;
	}
	
	public void decreaseYaw(float speed, float deltaTime) {
		yaw -= speed * deltaTime;
	}
	
	public void increaseRoll(float speed, float deltaTime) {
		roll += speed * deltaTime;
	}
	
	public void decreaseRoll(float speed, float deltaTime) {
		roll -= speed * deltaTime;
	}
	
	
	// Translate position by a vector
	public void translate(Vector3f vec) {
		position.translate(vec);
	}
	
	// Set position using the provided components
	public void setPosition(float x, float y, float z) {
		position = new Vector3f(x, y, z);
	}
	
	
	// For rendering
	public Matrix4f getViewMatrix() {
		return MathUtils.createViewMatrix(position, pitch, yaw, roll);
	}
	
	
	// Getters and setters
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	

}
