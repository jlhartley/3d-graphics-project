package model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import model.vertices.Mesh;
import shaders.ShaderProgram;
import util.ModelUtils;

public class Model {
	
	// The Vertex Array Object (VAO) that this class wraps
	private final int vao;
	
	// Track the Index Buffer Object (IBO) for cleanup purposes
	private int ibo;
	
	// Track the Vertex Buffer Objects (VBOs) for cleanup purposes
	private List<Integer> vbos = new ArrayList<>();
	
	// The total vertex count = indices.length
	private int vertexCount;

	public Model(float[] vertexPositions, float[] vertexNormals, float[] vertexColours, int[] indices) {
		
		// Generate and bind the Vertex Array Object (VAO)
		vao = glGenVertexArrays();
		// Binding ensures that subsequent state information is captured, when
		// we add the vertex attributes and set the indices.
		glBindVertexArray(vao);
		
		// Call methods to add each vertex attribute.
		addVertexAttrib(vertexPositions, ShaderProgram.POSITION_ATTRIB_LOCATION, 3);
		addVertexAttrib(vertexNormals, ShaderProgram.NORMAL_ATTRIB_LOCATION, 3);
		addVertexAttrib(vertexColours, ShaderProgram.COLOUR_ATTRIB_LOCATION, 3);
		
		// Set the indices data in the IBO
		setIndices(indices);
		
		// Unbind the VAO, so no more state can be changed
		// unit it is bound again.
		// TODO: Replace with something like GL.NULL
		glBindVertexArray(0);
	}
	
	public Model(List<VertexAttribute> vertexAttributes, ModelUtils.VBOFormat vboFormat) {
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		for (VertexAttribute vertexAttribute : vertexAttributes) {
			float[] data = vertexAttribute.data;
			int index = vertexAttribute.index;
			int size = vertexAttribute.size;
			addVertexAttrib(data, index, size);
		}
		
		glBindVertexArray(0);
	}
	
	// Interleaved
	public Model(Mesh mesh) {
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		ModelUtils.addVertexAttributes(mesh.getVertices());
		
		setIndices(mesh.getIndices());
		
		glBindVertexArray(0);
	}
	
	
	// Size = number of values per vertex in the vertexData array
	// TODO: Change vertexData to data?
	public void addVertexAttrib(float[] vertexData, int index, int size) {
		
		// Generate a new Vertex Buffer Object (VBO)
		int vbo = glGenBuffers();
		// Track for cleanup
		vbos.add(vbo);
		
		// Bind ready for data transfer and format specification
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		// Transfer the float array to a Java NIO buffer so that it can be passed to the OpenGL call.
		FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(vertexData.length);
		vertexDataBuffer.put(vertexData);
		vertexDataBuffer.flip();
		
		// Transfer the data to the VBO, which resides in GPU memory
		glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
		
		// Specify the format of the data
		// Float values,
		// normalise = false (already between -1 and 1),
		// stride = 0 (values are tightly packed), 
		// offset = 0 (values start right from the beginning)
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(index);
		
		// Nothing more to do with this VBO, so unbind
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void setIndices(int[] indices) {
		
		// Vertex count = indices.length, in context of rendering
		this.vertexCount = indices.length;
		
		// Generate a new Index Buffer Object (IBO)
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		
		// Convert the integer array to a Java NIO buffer so it can be passed to the OpenGL call
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		
		// Transfer the data
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		// Note - must not unbind since the binding is part of VAO state
	}
	
	
	public int getVertexCount() {
		return vertexCount;
	}

	public void bind() {
		glBindVertexArray(vao);
	}
	
	
	
	public void cleanUp() {
		// Unbind everything
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		// Delete everything
		for (int vbo : vbos) {
			glDeleteBuffers(vbo);
		}
		glDeleteBuffers(ibo);
		glDeleteVertexArrays(vao);
	}

}
