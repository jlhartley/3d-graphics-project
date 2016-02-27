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
	
	// Track all of the generated Vertex Buffer Objects (VBOs),
	// again for cleanup purposes
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
	
	// Interleaved
	public Model(Mesh mesh) {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		ModelUtils.addVertexAttributes(mesh.getVertices());
		
		setIndices(mesh.getIndices());
	}
	
	// Size = number of values per vertex in the vertexData array
	public void addVertexAttrib(float[] vertexData, int index, int size) {
		
		// Generate a new Vertex Buffer Object (VBO)
		int vbo = glGenBuffers();
		// Track for cleanup
		vbos.add(vbo);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		// Transfer the float array to a Java NIO buffer so that it can be passed to the OpenGL call.
		FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(vertexData.length);
		vertexDataBuffer.put(vertexData);
		vertexDataBuffer.flip();
		
		// Transfer the data to the VBO, which resides in GPU memory
		glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
		
		// Specify the format of the data
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(index);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void setIndices(int[] indices) {
		
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
		
		// Test if unbinding breaks stuff
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
