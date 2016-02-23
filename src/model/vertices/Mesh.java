package model.vertices;

import java.util.List;

public class Mesh {
	
	List<Vertex> vertices;
	int[] indices;
	
	public Mesh(List<Vertex> vertices, int[] indices) {
		this.vertices = vertices;
		this.indices = indices;
	}
	
	public int getUniqueVertexCount() {
		return vertices.size();
	}
	
	public int getTotalVertexCount() {
		return indices.length;
	}
	
	public int getTriangleCount() {
		// 3 vertices per triangle
		return getTotalVertexCount() / 3;
	}
}
