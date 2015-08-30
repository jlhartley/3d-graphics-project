package model;

import static org.lwjgl.opengl.GL30.*;
import static util.ModelUtils.*;

import shaders.ShaderProgram;

public class Model {
	
	private int vaoId;
	
	private float[] vertexPositions;
	private float[] vertexColours;
	private int[] indices;

	public Model(float[] vertexPositions, float[] vertexColours, int[] indices) {
		this.vertexPositions = vertexPositions;
		this.vertexColours = vertexColours;
		this.indices = indices;
		
		vaoId = genAndBindVAO();
		genAndBindIBO();
		addVertexAttrib(vertexPositions, ShaderProgram.POSITION_ATTRIB_LOCATION, 3);
		addVertexAttrib(vertexColours, ShaderProgram.COLOUR_ATTRIB_LOCATION, 3);
		setIndices(indices);
		unbindVAO();
	}
	
	public float[] getVertexPositions() {
		return vertexPositions;
	}

	public float[] getVertexColours() {
		return vertexColours;
	}

	public int[] getIndices() {
		return indices;
	}

	// Every element of the indices array corresponds to a vertex
	public int getVertexCount() {
		return indices.length;
	}
	
	// 3 vertices for every triangle
	public int getTriangleCount() {
		return getVertexCount() / 3;
	}
	
	
	public void bindVAO() {
		glBindVertexArray(vaoId);
	}
	

}
