package model;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import math.Matrix4f;
import math.Vector4f;

public class ModelBuilder {
	
	private List<Entity> entities = new ArrayList<>();
	
	private int totalVertexPositionsLength;
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
		totalVertexColoursLength += model.getVertexColours().length;
		totalIndicesLength += model.getIndices().length;
	}
	
	public void addEntities(List<Entity> entities) {
		this.entities.addAll(entities);
		for (Entity entity : entities) {
			Model model = entity.getModel();
			totalVertexPositionsLength += model.getVertexPositions().length;
			totalVertexColoursLength += model.getVertexColours().length;
			totalIndicesLength += model.getIndices().length;
		}
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
		Model model = entity.getModel();
		totalVertexPositionsLength -= model.getVertexPositions().length;
		totalVertexColoursLength -= model.getVertexColours().length;
		totalIndicesLength -= model.getIndices().length;
	}
	
	public Model build() {
		
		// Initialise the new model data arrays
		float[] vertexPositions = new float[totalVertexPositionsLength];
		float[] vertexColours = new float[totalVertexColoursLength];
		int[] indices = new int[totalIndicesLength];
		
		
		int vertexPosOffset = 0;
		int vertexColOffset = 0;
		
		int indexOffset = 0;
		int indexValueOffset = 0;
		
		
		for (Entity entity : entities) {
			
			// Grab the model and the transform to be applied to it
			Model model = entity.getModel();
			Matrix4f transform = entity.getModelMatrix();
			
			float[] modelVertexPositions = model.getVertexPositions();
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
				pos.multiply(transform);
				// Insert values into the array after the previous model in the correct places
				vertexPositions[vertexPosOffset + i] = pos.x;
				vertexPositions[vertexPosOffset + i+1] = pos.y;
				vertexPositions[vertexPosOffset + i+2] = pos.z;
			}
			vertexPosOffset += modelVertexPositions.length;
			
			
			// Straight forward copy over the colours after the previous model
			for (int i = 0; i < modelVertexColours.length; i++) {
				vertexColours[vertexColOffset + i] = modelVertexColours[i];
			}
			vertexColOffset += modelVertexColours.length;
			
		}
		
		return new Model(vertexPositions, vertexColours, indices);
		
		
	}
	
	
}
