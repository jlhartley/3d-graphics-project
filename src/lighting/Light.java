package lighting;

import math.geometry.Vector3f;

public class Light implements LightSource {
	
	private Vector3f position;
	
	private Vector3f colour;
	
	//private float intensity;
	
	// Empty constructor puts light at the origin
	public Light() {
		this(new Vector3f());
	}
	
	// Light is white if no colour is set
	public Light(Vector3f position) {
		this(position, new Vector3f(1, 1, 1));
	}
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
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

	@Override
	public Vector3f getLightPosition() {
		return getPosition();
	}
	
	@Override
	public Vector3f getLightColour() {
		return colour;
	}
	
	// TODO: Use intensity field
	@Override
	public float getLightIntensity() {
		return 1;
	}
	
}
