package entities;

import util.MathUtils;
import math.Matrix4f;
import math.Vector3f;
import model.VAOModel;

public class Entity3D {
	
	private VAOModel model;
	
	private Vector3f position;
	
	// Rotation
	private float rotX;
	private float rotY;
	private float rotZ;
	
	private float scale;
	
	public Entity3D(VAOModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	
	
	public void increaseRotation(float xIncAmt, float yIncAmt, float zIncAmt) {
		rotX += xIncAmt;
		rotY += yIncAmt;
		rotZ += zIncAmt;
	}
	
	public void translate(Vector3f vec) {
		position.x += vec.x;
		position.y += vec.y;
		position.z += vec.z;
	}
	
	
	// For rendering
	public Matrix4f getModelMatrix() {
		return MathUtils.createModelMatrix(position, rotX, rotY, rotZ, scale);
	}
	
	// Getters and setters
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}


	public VAOModel getModel() {
		return model;
	}
	
	
	

}
