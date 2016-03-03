package model;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.WorldTransform;
import math.geometry.Matrix4f;
import math.geometry.Vector3f;
import math.geometry.Vector4f;
import model.vertices.Mesh;
import model.vertices.Vertex;

public class ModelBuilder {
	
	private static class WorldMesh {
		Mesh mesh;
		WorldTransform worldTransform;
		
		public WorldMesh(Mesh mesh, WorldTransform worldTransform) {
			this.mesh = mesh;
			this.worldTransform = worldTransform;
		}
	}
	
	private List<WorldMesh> worldMeshes = new ArrayList<>();
	
	
	public ModelBuilder(List<WorldMesh> worldMeshes) {
		
	}
	
	
	
	public void addMesh(Mesh mesh) {
		
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
	
	public Mesh build() {
		
		int uniqueVertexCount = 0;
		int totalVertexCount = 0;
		
		for (WorldMesh worldMesh : worldMeshes) {
			uniqueVertexCount += worldMesh.mesh.getUniqueVertexCount();
			totalVertexCount += worldMesh.mesh.getTotalVertexCount();
		}
		
		List<Vertex> vertices = new ArrayList<>(uniqueVertexCount);
		List<Integer> indices = new ArrayList<>(totalVertexCount);
		
		for (WorldMesh worldMesh : worldMeshes) {
			
			Mesh mesh = worldMesh.mesh;
			
			// Copy over the indices, offsetting them based on the
			// number of vertices previously added
			int verticesAddedSoFar = vertices.size();
			for (int index : mesh.getIndices()) {
				indices.add(index + verticesAddedSoFar);
			}
			
			Matrix4f modelMatrix = worldMesh.worldTransform.getModelMatrix();
			
			for (Vertex vertex : mesh.getVertices()) {
				Vertex transformedVertex = transformVertex(vertex, modelMatrix);
				// Add the transformed vertex to the array
				vertices.add(transformedVertex);
			}
			
		}
		
		return new Mesh(vertices, indices)
		
	}
	
	
	public Model build() {
		
		// Initialise the new model data arrays
		float[] vertexPositions = new float[totalVertexPositionsLength];
		float[] vertexNormals = new float[totalVertexNormalsLength];
		float[] vertexColours = new float[totalVertexColoursLength];
		int[] indices = new int[totalIndicesLength];
		
		
		int vertexPosOffset = 0;
		int vertexNormOffset = 0;
		int vertexColOffset = 0;
		
		int indexOffset = 0;
		int indexValueOffset = 0;
		
		
		for (Entity entity : entities) {
			
			// Grab the model and the transform to be applied to it
			Model model = entity.getModel();
			Matrix4f modelMatrixTransform = entity.getModelMatrix();
			
			float[] modelVertexPositions = model.getVertexPositions();
			float[] modelVertexNormals = model.getVertexNormals();
			float[] modelVertexColours = model.getVertexColours();
			int[] modelIndices = model.getIndices();
			
			
			// Place each index value after the previous model (using the offset),
			// however add an offset to the value itself to account for the previous
			// model vertices when drawing
			indexValueOffset = vertexPosOffset / 3;
			for (int i = 0; i < modelIndices.length; i++) {
				indices[indexOffset + i] = modelIndices[i] + indexValueOffset;
			}
			indexOffset += modelIndices.length;
			
			
			
			// Copy over the vertex positions after the previous model, transforming as we go
			for (int i = 0; i < modelVertexPositions.length; i+=3) {
				// Since the position is a point the w component is set to 1
				Vector4f pos = new Vector4f(modelVertexPositions[i], modelVertexPositions[i+1], modelVertexPositions[i+2], 1);
				pos.multiply(modelMatrixTransform);
				// Insert values into the array after the previous model in the correct places
				vertexPositions[vertexPosOffset + i] = pos.x;
				vertexPositions[vertexPosOffset + i+1] = pos.y;
				vertexPositions[vertexPosOffset + i+2] = pos.z;
			}
			vertexPosOffset += modelVertexPositions.length;
			
			
			
			// Copy over the vertex normals after the previous model, transforming as we go
			for (int i = 0; i < modelVertexNormals.length; i+=3) {
				// Since the normals are direction vectors the w component is set to 0
				Vector4f norm4 = new Vector4f(modelVertexNormals[i], modelVertexNormals[i+1], modelVertexNormals[i+2], 0);
				norm4.multiply(modelMatrixTransform);
				// Ensure that the normal remains normalised
				Vector3f norm = new Vector3f(norm4.x, norm4.y, norm4.z);
				norm.normalise();
				// Insert values into the array after the previous model in the correct places
				vertexNormals[vertexNormOffset + i] = norm.x;
				vertexNormals[vertexNormOffset + i+1] = norm.y;
				vertexNormals[vertexNormOffset + i+2] = norm.z;
			}
			vertexNormOffset += modelVertexNormals.length;
			
			
			
			// Straight forward copy over the colours after the previous model
			for (int i = 0; i < modelVertexColours.length; i++) {
				vertexColours[vertexColOffset + i] = modelVertexColours[i];
			}
			vertexColOffset += modelVertexColours.length;
			
		}
		
		
		// Debugging info
		System.out.println("Built new model");
		System.out.println("Unique vertex count: " + vertexPositions.length / 3);
		System.out.println("Total vertex count: " + indices.length);
		System.out.println("Triangle count: " + indices.length / 3);
		
		return new Model(vertexPositions, vertexNormals, vertexColours, indices);
	}
	
	
}
