package entities;

import math.geometry.Vector3f;

public class Position extends Vector3f {
	
	// Movement methods
	public void moveX(float speed, float deltaTime) {
		x += speed * deltaTime;
	}
	
	public void moveY(float speed, float deltaTime) {
		y += speed * deltaTime;
	}
	
	public void moveZ(float speed, float deltaTime) {
		z += speed * deltaTime;
	}
	
	// Integrate the velocity by simple accumulation
	public void move(Vector3f velocity, float deltaTime) {
		x += velocity.x * deltaTime;
		y += velocity.y * deltaTime;
		z += velocity.z * deltaTime;
	}
	
}
