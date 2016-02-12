package model.vertices;

import math.geometry.Vector3f;

public class ColouredVertex extends Vertex {

	Vector3f colour;
	
	public ColouredVertex(Vector3f position, Vector3f normal, Vector3f colour) {
		super(position, normal);
		this.colour = colour;
	}

}
