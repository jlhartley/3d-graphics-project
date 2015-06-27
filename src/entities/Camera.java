package entities;

import util.MathUtils;
import math.Matrix4f;
import math.Vector3f;

public class Camera {
	
	private Vector3f position;
	
	// Rotation
	private float pitch; // Rotation in x-axis
	private float yaw; // Rotation in y-axis
	private float roll; // Rotation in z-axis
	
	public Camera(Vector3f position, float pitch, float yaw, float roll) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	
	
	public void moveUp(float speed, float deltaTime) {
		position.y += speed*deltaTime;
	}
	
	public void moveDown(float speed, float deltaTime) {
		position.y -= speed*deltaTime;
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
	
	
	public void translate(Vector3f vec) {
		position.x += vec.x;
		position.y += vec.y;
		position.z += vec.z;
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
