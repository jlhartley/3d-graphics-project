package entities;

import math.geometry.Vector3f;
import model.Model;

public class MovingEntity extends Entity {
	
	private Vector3f velocity;
	
	// Telescoping constructors
	public MovingEntity(Model model, Vector3f position, Vector3f rotation, Vector3f velocity) {
		this(model, position, rotation, velocity, 1);
	}

	public MovingEntity(Model model, Vector3f position, Vector3f rotation, Vector3f velocity, float scale) {
		super(model, position, rotation, scale);
		this.velocity = velocity;
	}
	
	public void move(float deltaTime) {
		super.move(velocity, deltaTime);
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	
	public void setVelocity(float x, float y, float z) {
		velocity.set(x, y, z);
	}
	

}
