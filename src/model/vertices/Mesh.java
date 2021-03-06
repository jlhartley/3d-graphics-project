package model.vertices;

import java.util.List;

import math.geometry.Vector3f;

public class Mesh {
	
	private List<Vertex> vertices;
	private List<Integer> indices;
	
	public Mesh(List<Vertex> vertices, List<Integer> indices) {
		this.vertices = vertices;
		this.indices = indices;
	}
	
	public float[] getPositionsArray() {
		// 3 components (x, y, z) for each vertex position
		float[] vertexPositions = new float[vertices.size() * 3];
		// Copy the vertex positions into the array
		for (int i = 0; i < vertices.size(); i++) {
			Vector3f position = vertices.get(i).position;
			vertexPositions[i * 3] = position.x;
			vertexPositions[i * 3 + 1] = position.y;
			vertexPositions[i * 3 + 2] = position.z;
		}
		return vertexPositions;
	}
	
	public float[] getNormalsArray() {
		// 3 components (x, y, z) for each vertex normal
		float[] vertexNormals = new float[vertices.size() * 3];
		// Copy the vertex normals into the array
		for (int i = 0; i < vertices.size(); i++) {
			Vector3f normal = vertices.get(i).normal;
			vertexNormals[i * 3] = normal.x;
			vertexNormals[i * 3 + 1] = normal.y;
			vertexNormals[i * 3 + 2] = normal.z;
		}
		return vertexNormals;
	}
	
	public int[] getIndicesArray() {
		int length = indices.size();
		int[] indicesArray = new int[length];
		for (int i = 0; i < length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}
	
	
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	public List<Integer> getIndices() {
		return indices;
	}
	
	public int getUniqueVertexCount() {
		return vertices.size();
	}
	
	public int getTotalVertexCount() {
		return indices.size();
	}
	
	public int getTriangleCount() {
		return getTotalVertexCount() / 3;
	}
	
	
}
