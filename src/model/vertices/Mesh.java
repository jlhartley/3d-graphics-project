package model.vertices;

import java.util.List;

import math.geometry.Vector3f;

public class Mesh {
	
	private List<Vertex> vertices;
	private int[] indices;
	
	public Mesh(List<Vertex> vertices, int[] indices) {
		this.vertices = vertices;
		this.indices = indices;
	}
	
	public float[] getVertexPositions() {
		// 3 components (x, y, z) for each vertex position
		float[] vertexPositions = new float[getUniqueVertexCount() * 3];
		// Copy the vertex positions into the array
		for (int i = 0; i < getUniqueVertexCount(); i++) {
			Vector3f position = vertices.get(i).position;
			vertexPositions[i * 3] = position.x;
			vertexPositions[i * 3 + 1] = position.y;
			vertexPositions[i * 3 + 2] = position.z;
		}
		return vertexPositions;
	}
	
	public float[] getVertexNormals() {
		// 3 components (x, y, z) for each vertex normal
		float[] vertexNormals = new float[getUniqueVertexCount() * 3];
		// Copy the vertex normals into the array
		for (int i = 0; i < getUniqueVertexCount(); i++) {
			Vector3f normal = vertices.get(i).normal;
			vertexNormals[i * 3] = normal.x;
			vertexNormals[i * 3 + 1] = normal.y;
			vertexNormals[i * 3 + 2] = normal.z;
		}
		return vertexNormals;
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	public int[] getIndices() {
		return indices;
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
