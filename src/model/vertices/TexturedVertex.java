package model.vertices;

import math.geometry.Vector2f;
import math.geometry.Vector3f;

public class TexturedVertex extends Vertex {
	
	Vector2f textureCoordinate;
	
	public TexturedVertex(Vector3f position, Vector3f normal, Vector2f textureCoordinate) {
		super(position, normal);
		this.textureCoordinate = textureCoordinate;
	}
	
	

}
