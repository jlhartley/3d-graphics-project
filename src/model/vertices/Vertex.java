package model.vertices;

import math.geometry.Vector3f;

public class Vertex {
	
	Vector3f position;
	Vector3f normal;
	
	public Vertex(Vector3f position, Vector3f normal) {
		this.position = position;
		this.normal = normal;
	}

}
