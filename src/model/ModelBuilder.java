package model;

import java.util.ArrayList;
import java.util.List;

import entities.Transform;
import math.geometry.Matrix4f;
import math.geometry.Vector3f;
import model.vertices.Mesh;
import model.vertices.Vertex;

public class ModelBuilder {
	
	public static class MeshTransform {
		
		Mesh mesh;
		Transform transform;
		
		public MeshTransform(Mesh mesh, Transform transform) {
			this.mesh = mesh;
			this.transform = transform;
		}
		
	}
	
	private List<MeshTransform> meshTransforms = new ArrayList<>();
	
	
	public ModelBuilder() {
		
	}
	
	public ModelBuilder(List<MeshTransform> meshTransforms) {
		this.meshTransforms = meshTransforms;
	}
	
	
	public void addMesh(Mesh mesh, Transform transform) {
		meshTransforms.add(new MeshTransform(mesh, transform));
	}
	
	public Mesh build() {
		
		int uniqueVertexCount = 0;
		int totalVertexCount = 0;
		
		for (MeshTransform meshTransform : meshTransforms) {
			uniqueVertexCount += meshTransform.mesh.getUniqueVertexCount();
			totalVertexCount += meshTransform.mesh.getTotalVertexCount();
		}
		
		List<Vertex> vertices = new ArrayList<>(uniqueVertexCount);
		List<Integer> indices = new ArrayList<>(totalVertexCount);
		
		for (MeshTransform meshTransform : meshTransforms) {
			
			Mesh mesh = meshTransform.mesh;
			Transform transform = meshTransform.transform;
			
			// Copy over the indices, offsetting them based on the
			// number of vertices previously added, so they point to the correct
			// set of vertices
			int verticesAddedSoFar = vertices.size();
			for (int index : mesh.getIndices()) {
				indices.add(index + verticesAddedSoFar);
			}
			
			
			Matrix4f matrix = transform.getMatrix();
			
			for (Vertex vertex : mesh.getVertices()) {
				Vertex transformedVertex = transformVertex(vertex, matrix);
				// Add the transformed vertex to the array
				vertices.add(transformedVertex);
			}
			
		}
		
		Mesh mesh = new Mesh(vertices, indices);
		
		System.out.println("Built New Mesh");
		System.out.println("Unique Vertex Count: " + mesh.getUniqueVertexCount());
		System.out.println("Total Vertex Count: " + mesh.getTotalVertexCount());
		System.out.println("Triangle Count: " + mesh.getTriangleCount());
		
		return mesh;
		
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
