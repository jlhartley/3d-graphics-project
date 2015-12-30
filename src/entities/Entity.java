package entities;

import math.geometry.Matrix4f;
import math.geometry.MatrixUtils;
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
	
	
	// Model matrix held as instance variable to avoid object creation
	private Matrix4f modelMatrix = new Matrix4f();
	
	
	// Telescoping constructors
	public Entity(Model model) {
		this(model, new Vector3f());
	}
	
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
	public void moveX(float speed, float deltaTime) {
		position.x += speed * deltaTime;
	}
	
	public void moveY(float speed, float deltaTime) {
		position.y += speed * deltaTime;
	}
	
	public void moveZ(float speed, float deltaTime) {
		position.z += speed * deltaTime;
	}
	
	public void move(Vector3f velocity, float deltaTime) {
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		position.z += velocity.z * deltaTime;
	}
	
	// Rotational methods
	public void rotateX(float speed, float deltaTime) {
		rotation.x += speed * deltaTime;
	}
	
	public void rotateY(float speed, float deltaTime) {
		rotation.y += speed * deltaTime;
	}
	
	public void rotateZ(float speed, float deltaTime) {
		rotation.z += speed * deltaTime;
	}
	
	public void rotate(Vector3f velocity, float deltaTime) {
		rotation.x += velocity.x * deltaTime;
		rotation.y += velocity.y * deltaTime;
		rotation.z += velocity.z * deltaTime;
	}
	
	
	// Translate position by a vector
	public void translate(Vector3f vector) {
		position.translate(vector);
	}
	
	
	// Model matrix for rendering
	public Matrix4f getModelMatrix() {
		modelMatrix.identity();
		modelMatrix.translate(position);
		modelMatrix.rotate((float) Math.toRadians(rotation.x), MatrixUtils.X_AXIS);
		modelMatrix.rotate((float) Math.toRadians(rotation.y), MatrixUtils.Y_AXIS);
		modelMatrix.rotate((float) Math.toRadians(rotation.z), MatrixUtils.Z_AXIS);
		modelMatrix.scale(new Vector3f(scale, scale, scale)); // Uniform scale in all axes
		return modelMatrix;
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
	
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
	public Model getModel() {
		return model;
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

	
	@Override
	public String toString() {
		return "Entity [position=" + position + ", rotation=" + rotation + ", scale=" + scale + "]";
	}
	
}
