package entities;

// Moving forward could mean subtracting from z, or
// it could refer to moving relative to another coordinate 
// system (e.g camera)

public interface Movable {
	void moveRight(float speed, float deltaTime);
	void moveUp(float speed, float deltaTime);
	void moveForward(float speed, float deltaTime);
}
