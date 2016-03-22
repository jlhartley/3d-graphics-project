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
import model.vertices.Vertex;
import shaders.ShaderProgram;

public class Model {
	
	// The Vertex Array Object (VAO) that this class wraps
	private final int vao;
	
	// Track the Index Buffer Object (IBO) for cleanup purposes
	private int ibo;
	
	// VBOs for vertex attributes
	private int positionsVbo;
	private int normalsVbo;
	
	// Track the Vertex Buffer Objects (VBOs) for cleanup purposes
	private List<Integer> vbos = new ArrayList<>();
	
	// The total vertex count = indices.length
	private int vertexCount;
	
	// Associates the VAO with a client side mesh representation
	private Mesh mesh;
	
	public Model(float[] vertexPositions, float[] vertexNormals, int[] indices) {
		
		// Generate and bind the Vertex Array Object (VAO)
		vao = glGenVertexArrays();
		// Binding ensures that subsequent state information is captured, when
		// we add the vertex attributes and set the indices.
		glBindVertexArray(vao);
		
		// Call methods to add each vertex attribute.
		positionsVbo = addVertexAttrib(vertexPositions, ShaderProgram.POSITION_ATTRIB_LOCATION, 3);
		normalsVbo = addVertexAttrib(vertexNormals, ShaderProgram.NORMAL_ATTRIB_LOCATION, 3);
		
		// Set the indices data in the IBO
		setIndices(indices);
		
		// Unbind the VAO, so no more state can be changed until it is bound again.
		// TODO: Replace with something like GL.NULL
		glBindVertexArray(0);
	}
	
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
		
		// Unbind the VAO, so no more state can be changed until it is bound again.
		// TODO: Replace with something like NULL
		glBindVertexArray(0);
	}
	
	// SCRAP
	public Model(List<VertexAttribute> vertexAttributes) {
		
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
		
		this(mesh.getPositionsArray(), mesh.getNormalsArray(), mesh.getIndicesArray());
		
		this.mesh = mesh;
		
		/*
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		ModelUtils.setVertexAttributes(mesh.getVertices());
		
		setIndices(mesh.getIndices());
		
		glBindVertexArray(0);
		*/
	}
	
	
	// Size = number of values per vertex in the vertexData array
	private int addVertexAttrib(float[] data, int index, int size) {
		
		// Generate a new Vertex Buffer Object (VBO)
		int vbo = glGenBuffers();
		// Track for cleanup
		vbos.add(vbo);
		
		// Bind ready for data transfer and format specification
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		// Transfer the float array to a Java NIO buffer so that it can be passed to the OpenGL call.
		FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(data.length);
		dataBuffer.put(data);
		dataBuffer.flip();
		
		// Transfer the data to the VBO, which resides in GPU memory
		glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
		
		// Specify the format of the data
		// Float values,
		// normalise = false (already between -1 and 1),
		// stride = 0 (values are tightly packed), 
		// offset = 0 (values start right from the beginning)
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(index);
		
		// Nothing more needs to be done with this VBO, so unbind
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return vbo;
	}
	
	// For a list
	/*
	private void setIndices(List<Integer> indices) {
		
		this.vertexCount = indices.size();
		
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		
		// Transfer the content of the indices list to the Java NIO buffer
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.size());
		for (int index : indices) {
			indicesBuffer.put(index);
		}
		indicesBuffer.flip();
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
	}*/
	
	// For an array
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
	
	// Currently just for testing
	public void updateFromMesh() {
		for (int i = 0; i < mesh.getVertices().size(); i++) {
			Vertex vertex = mesh.getVertices().get(i);
			vertex.position.scale((float) (1 + Math.random() / 100));
			vertex.normal.x *= Math.random();
		}
		glBindBuffer(GL_ARRAY_BUFFER, positionsVbo);
		float[] vertexPositions = mesh.getPositionsArray();
		// Transfer the float array to a Java NIO buffer so that it can be passed to the OpenGL call.
		FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(vertexPositions.length);
		vertexDataBuffer.put(vertexPositions);
		vertexDataBuffer.flip();
		glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, normalsVbo);
		float[] vertexNormals = mesh.getNormalsArray();
		// Transfer the float array to a Java NIO buffer so that it can be passed to the OpenGL call.
		FloatBuffer vertexNormalBuffer = BufferUtils.createFloatBuffer(vertexNormals.length);
		vertexNormalBuffer.put(vertexPositions);
		vertexNormalBuffer.flip();
		glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void bind() {
		glBindVertexArray(vao);
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public Mesh getMesh() {
		return mesh;
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
