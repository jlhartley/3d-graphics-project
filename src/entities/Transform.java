package entities;

import math.geometry.Matrix4f;
import math.geometry.Vector3f;

public class Transform {
	
	// Position in world space - x, y and z
	private Vector3f position;
	
	// Rotation in x, y and z axes
	private Vector3f rotation;
	
	// Uniform scale in all axes
	private float scale;
	
	
	// Matrix representing the transformation
	private Matrix4f matrix = new Matrix4f();
	
	
	// Telescoping constructors
	public Transform() {
		this(new Vector3f());
	}
	
	public Transform(Vector3f position) {
		this(position, new Vector3f());
	}
	
	public Transform(Vector3f position, Vector3f rotation) {
		this(position, rotation, 1);
	}
	
	public Transform(Vector3f position, Vector3f rotation, float scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	
	public Matrix4f getMatrix() {
		matrix.identity();
		matrix.translate(position);
		matrix.rotateX((float) Math.toRadians(rotation.x));
		matrix.rotateY((float) Math.toRadians(rotation.y));
		matrix.rotateZ((float) Math.toRadians(rotation.z));
		matrix.scale(scale);
		return matrix;
	}
	
	
	
	
	// Getters and setters
	public Vector3f getPosition() {
		return position;
	}

	public Transform setPosition(Vector3f position) {
		this.position = position;
		return this;
	}
	
	// Set position using the provided components
	public Transform setPosition(float x, float y, float z) {
		position.set(x, y, z);
		return this;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public Transform setRotation(Vector3f rotation) {
		this.rotation = rotation;
		return this;
	}
	
	// Set rotation using the provided components
	public Transform setRotation(float x, float y, float z) {
		rotation.set(x, y, z);
		return this;
	}
	
	public float getScale() {
		return scale;
	}

	public Transform setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	
	
	// Convenience methods
	public float getRotX() {
		return rotation.x;
	}

	public Transform setRotX(float rotX) {
		rotation.x = rotX;
		return this;
	}

	public float getRotY() {
		return rotation.y;
	}

	public Transform setRotY(float rotY) {
		this.rotation.y = rotY;
		return this;
	}

	public float getRotZ() {
		return rotation.z;
	}

	public Transform setRotZ(float rotZ) {
		rotation.z = rotZ;
		return this;
	}
}
