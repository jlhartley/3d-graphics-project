package model;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import math.geometry.Matrix4f;
import math.geometry.Vector3f;
import math.geometry.Vector4f;

public class ModelBuilder {
	
	private List<Entity> entities = new ArrayList<>();
	
	private int totalVertexPositionsLength;
	private int totalVertexNormalsLength;
	private int totalVertexColoursLength;
	private int totalIndicesLength;
	
	
	public ModelBuilder() {
		
	}
	
	public ModelBuilder(List<Entity> entities) {
		addEntities(entities);
	}
	
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		Model model = entity.getModel();
		totalVertexPositionsLength += model.getVertexPositions().length;
		totalVertexNormalsLength += model.getVertexNormals().length;
		totalVertexColoursLength += model.getVertexColours().length;
		totalIndicesLength += model.getIndices().length;
	}
	
	public void addEntities(List<Entity> entities) {
		this.entities.addAll(entities);
		for (Entity entity : entities) {
			Model model = entity.getModel();
			totalVertexPositionsLength += model.getVertexPositions().length;
			totalVertexNormalsLength += model.getVertexNormals().length;
			totalVertexColoursLength += model.getVertexColours().length;
			totalIndicesLength += model.getIndices().length;
		}
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
		Model model = entity.getModel();
		totalVertexPositionsLength -= model.getVertexPositions().length;
		totalVertexNormalsLength -= model.getVertexNormals().length;
		totalVertexColoursLength -= model.getVertexColours().length;
		totalIndicesLength -= model.getIndices().length;
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
