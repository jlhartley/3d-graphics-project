package camera;

import math.geometry.Matrix4f;
import math.geometry.Vector3f;

public class Camera {
	
	// Position in world space - x, y and z
	private Vector3f position;
	
	// Rotation
	private Vector3f rotation;
	
	
	// View matrix held as instance variable to avoid object creation
	private Matrix4f viewMatrix = new Matrix4f();
	
	
	// Telescoping constructors
	// Place the camera at the origin with no pitch, yaw or roll
	public Camera() {
		this(new Vector3f());
	}
	
	// Place the camera at the specified position with no pitch, yaw or roll
	public Camera(Vector3f position) {
		this(position, new Vector3f());
	}
	
	// Place the camera at the specified position with the specified rotation
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	// Place the camera at the specified position with the given pitch, yaw and roll
	public Camera(Vector3f position, float pitch, float yaw, float roll) {
		this.position = position;
		this.rotation = new Vector3f(pitch, yaw, roll);
	}
	
	
	// Movement methods
	public void moveX(float speed, float deltaTime) {
		position.x += speed * deltaTime;
	}
	
	public void moveY(float speed, float deltaTime) {
		position.y += speed * deltaTime;
	}
	
	public void moveZ(float speed, float deltaTime) {
		position.z += speed * deltaTime;
	}
	
	// Rotational methods
	public void increasePitch(float speed, float deltaTime) {
		rotation.x += speed * deltaTime;
	}
	
	public void increaseYaw(float speed, float deltaTime) {
		rotation.y += speed * deltaTime;
	}
	
	public void increaseRoll(float speed, float deltaTime) {
		rotation.z += speed * deltaTime;
	}
	
	
	// Translate position by a vector
	public void translate(Vector3f vec) {
		position.translate(vec);
	}
	
	
	// For rendering
	public Matrix4f getViewMatrix() {
		viewMatrix.identity();
		viewMatrix.rotateX((float) Math.toRadians(rotation.x));
		viewMatrix.rotateY((float) Math.toRadians(rotation.y));
		viewMatrix.rotateZ((float) Math.toRadians(rotation.z));
		Vector3f negativeTranslation = new Vector3f(position).negate();
		viewMatrix.translate(negativeTranslation);
		return viewMatrix;
	}
	
	
	
	// Getters and setters
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	// Set position using the provided components
	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
	}
	
	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	// Set rotation using the provided components
	public void setRotation(float x, float y, float z) {
		rotation.set(x, y, z);
	}
	
	
	
	// Component-wise rotation aliases
	
	// Pitch = rotation about x-axis
	// Yaw = rotation about y-axis
	// Roll = rotation about z-axis
	
	public float getPitch() {
		return rotation.x;
	}

	public void setPitch(float pitch) {
		rotation.x = pitch;
	}

	public float getYaw() {
		return rotation.y;
	}

	public void setYaw(float yaw) {
		rotation.y = yaw;
	}

	public float getRoll() {
		return rotation.z;
	}

	public void setRoll(float roll) {
		rotation.z = roll;
	}
	
	

}
