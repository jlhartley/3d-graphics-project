package lighting;

import math.geometry.Vector3f;

public class Light {
	
	private Vector3f position;
	
	//private Vector3f colour;
	
	//private float intensity;
	
	public Light() {
		this(new Vector3f());
	}
	
	public Light(Vector3f position) {
		this.position = position;
	}
	
	
	// Set position using the provided components
	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
	}
	
	
	// Movement methods
	public void moveUp(float speed, float deltaTime) {
		position.y += speed * deltaTime;
	}
	
	public void moveDown(float speed, float deltaTime) {
		position.y -= speed *  deltaTime;
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
	
	
	
	public Vector3f getPosition() {
		return position;
	}
	
}
