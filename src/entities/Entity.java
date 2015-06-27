package entities;
// TODO: Move to a new entities package (sub-package under model)
import math.Vector3f;
import model.VAOModel;

public class Entity {
	
	private VAOModel vaoModel;
	
	private Vector3f position;
	private float rotationAngle;
	private float scale;
	
	private float speed; // Unit distance per second
	private float rotSpeed; // Degrees per second
	
	private Vector3f colour;
	
	public void setDefaults() {
		
		position = new Vector3f(0, 0, 0);
		rotationAngle = 0;
		scale = 1;
		speed = 0.5f;
		rotSpeed = 180;
		colour = new Vector3f(1, 1, 1);
		
	}
	
	
	public Entity(VAOModel vaoModel) {
		this(vaoModel, new Vector3f(0, 0, 0), 0);
	}
	
	public Entity(VAOModel vaoModel, Vector3f position, float rotationAngle) {
		this(vaoModel, position, rotationAngle, 0.5f, 180); // 180 degrees per second - 1 full rotation in 2 seconds
	}
	
	public Entity(VAOModel vaoModel, Vector3f position, float rotationAngle, float speed, float rotSpeed) {
		this(vaoModel, position, rotationAngle, speed, rotSpeed, new Vector3f(1, 1, 1));
	}
	
	public Entity(VAOModel vaoModel, Vector3f position, float rotationAngle, float speed, float rotSpeed, Vector3f colour) {
		this(vaoModel, position, rotationAngle, speed, rotSpeed, colour, 1);
	}
	
	public Entity(VAOModel vaoModel, Vector3f position, float rotationAngle, float speed, float rotSpeed, Vector3f colour, float scale) {
		
		this.vaoModel = vaoModel;
		this.position = position;
		this.rotationAngle = rotationAngle;
		this.speed = speed;
		this.rotSpeed = rotSpeed;
		this.colour = colour;
		this.scale = scale;
		
		
	}
	
	
	public void move(float deltaTime, boolean moveForward, boolean moveBack, boolean turnRight, boolean turnLeft) {
		
		if (moveForward) {
			goForward(deltaTime);
		} else if (moveBack) {
			goBack(deltaTime);
		}
		
		if (turnRight) {
			turnRight(deltaTime);
		} else if (turnLeft) {
			turnLeft(deltaTime);
		}
		
	}
	
	
	// NOT this.position = vec, as that will pass the reference!
	public void setPosition(Vector3f vec) {
		position.x = vec.x;
		position.y = vec.y;
		position.z = vec.z;
	}
	
	public void translate(Vector3f vec) {
		position.x += vec.x;
		position.y += vec.y;
		position.z += vec.z;
	}
	
	
	private void goForward(float deltaTime) {
		position.x += speed * Math.sin(Math.toRadians(rotationAngle+180)) * deltaTime;
		position.y += speed * Math.cos(Math.toRadians(rotationAngle+180)) * deltaTime;
	}
	
	private void goBack(float deltaTime) {
		position.x -= speed * Math.sin(Math.toRadians(rotationAngle+180)) * deltaTime;
		position.y -= speed * Math.cos(Math.toRadians(rotationAngle+180)) * deltaTime;
	}
	
	private void turnRight(float deltaTime) {
		rotationAngle += deltaTime*rotSpeed;
	}
	
	private void turnLeft(float deltaTime) {
		rotationAngle -= deltaTime*rotSpeed;
	}
	
	
	
	
	
	
	public void setRotationAngle(float rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public VAOModel getVaoModel() {
		return vaoModel;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotationAngle() {
		return rotationAngle;
	}
	
	
	public void cleanUp() {
		vaoModel.cleanUp();
	}

}
