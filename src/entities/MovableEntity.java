package entities;

import math.Vector3f;
import model.Model;

public class MovableEntity extends Entity {
	
	private Vector3f velocity;

	public MovableEntity(Model model, Vector3f position, Vector3f rotation, Vector3f velocity, float scale) {
		super(model, position, rotation, scale);
		this.velocity = velocity;
	}
	
	public void tick(float deltaTime) {
		super.moveRight(velocity.x, deltaTime);
		super.moveUp(velocity.y, deltaTime);
		super.moveForward(velocity.z, deltaTime);
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	

}
