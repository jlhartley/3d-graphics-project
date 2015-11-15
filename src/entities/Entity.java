package entities;

import math.MathUtils;
import math.geometry.Matrix4f;
import math.geometry.Vector3f;
import model.Model;

public class Entity {
	
	// Model holding the vertex attributes
	private Model model;
	
	// Position in world space - x, y and z
	private Vector3f position;
	
	// Rotation in x, y and z axes
	private Vector3f rotation;
	
	// Uniform scale in all axes
	private float scale;
	
	// Telescoping constructors
	public Entity(Model model, Vector3f position) {
		this(model, position, new Vector3f());
	}
	
	public Entity(Model model, Vector3f position, Vector3f rotation) {
		this(model, position, rotation, 1);
	}
	
	public Entity(Model model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
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
	
	
	// TODO: Rotational methods
	
	
	// Translate position by a vector
	public void translate(Vector3f vec) {
		position.translate(vec);
	}
	
	// Set position using the provided components
	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
	}
	
	
	// For rendering
	public Matrix4f getModelMatrix() {
		return MathUtils.createModelMatrix(position, rotation.x, rotation.y, rotation.z, scale);
	}
	
	// Getters and setters
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	// Convenience methods
	public float getRotX() {
		return rotation.x;
	}

	public void setRotX(float rotX) {
		rotation.x = rotX;
	}

	public float getRotY() {
		return rotation.y;
	}

	public void setRotY(float rotY) {
		this.rotation.y = rotY;
	}

	public float getRotZ() {
		return rotation.z;
	}

	public void setRotZ(float rotZ) {
		rotation.z = rotZ;
	}
	
	
	public Model getModel() {
		return model;
	}
	
	
	

}
