package model;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import math.geometry.Matrix4f;
import math.geometry.Vector3f;
import model.vertices.Mesh;
import model.vertices.Vertex;

public class ModelBuilder {
	
	/*
	public static class MeshTransform {
		
		Mesh mesh;
		Transform transform;
		
		public MeshTransform(Mesh mesh, Transform transform) {
			this.mesh = mesh;
			this.transform = transform;
		}
		
	}
	*/
	
	private List<Entity> entities = new ArrayList<>();
	
	
	public ModelBuilder() {
		
	}
	
	public ModelBuilder(List<Entity> entities) {
		this.entities = entities;
	}
	
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public Model build() {
		
		// Calculate initial capacities for ArrayList efficiency
		
		int uniqueVertexCount = 0;
		int totalVertexCount = 0;
		
		for (Entity entity : entities) {
			uniqueVertexCount += entity.getModel().getMesh().getUniqueVertexCount();
			totalVertexCount += entity.getModel().getMesh().getTotalVertexCount();
		}
		
		List<Vertex> vertices = new ArrayList<>(uniqueVertexCount);
		//List<Integer> indices = new ArrayList<>(totalVertexCount);
		int[] indices = new int[totalVertexCount];
		int verticesAddedSoFar = 0;
		int ptrOffset = 0;
		
		for (Entity entity : entities) {
			
			Mesh mesh = entity.getModel().getMesh();
			Matrix4f matrix = entity.getModelMatrix();
			
			// Copy over the indices, offsetting them based on the
			// number of vertices previously added, so they point to the correct
			// set of vertices
			for (int i = 0; i < mesh.getIndices().size(); i++) {
				int index = mesh.getIndices().get(i);
				indices[i + ptrOffset] = index + verticesAddedSoFar;
			}
			ptrOffset += mesh.getIndices().size();
			verticesAddedSoFar += vertices.size();
			// Correct code: 
			//verticesAddedSoFar = vertices.size();
			
			
			for (Vertex vertex : mesh.getVertices()) {
				Vertex transformedVertex = transformVertex(vertex, matrix);
				// Add the transformed vertex to the array
				vertices.add(transformedVertex);
			}
			
		}
		
		Mesh mesh = new Mesh(vertices, null);
		
		
		Model m = new Model(mesh.getPositionsArray(), mesh.getNormalsArray(), indices);
		
		System.out.println("Built New Mesh");
		System.out.println("Unique Vertex Count: " + mesh.getUniqueVertexCount());
		//System.out.println("Total Vertex Count: " + mesh.getTotalVertexCount());
		//System.out.println("Triangle Count: " + mesh.getTriangleCount());
		
		return m;
		
	}
	
	private Vertex transformVertex(Vertex vertex, Matrix4f matrix) {
		Vector3f position = vertex.position;
		Vector3f normal = vertex.normal;
		
		// Multiply with w = 1, so that translation is applied
		Vector3f transformedPosition = new Vector3f(position).multiply(matrix, 1);
		
		// Multiply with w = 0, so that the normal is not translated (only rotated and scaled)
		// Note that this will not work in cases where a non-uniform scale is applied.
		// For that case, the transpose of the inverse is required (see note in shader)
		Vector3f transformedNormal = new Vector3f(normal).multiply(matrix, 0);
		// Make the normal length 1 (a unit vector) again
		transformedNormal.normalise();
		
		return new Vertex(transformedPosition, transformedNormal);
	}
	
	
}
