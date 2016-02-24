package model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static util.ModelUtils.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import shaders.ShaderProgram;
import util.BufferUtils;

public class Model {
	
	private int vao;
	private int ibo;
	
	private List<Integer> vbos = new ArrayList<>();

	public Model(float[] vertexPositions, float[] vertexNormals, float[] vertexColours, int[] indices) {
		
		// Generate and bind the Vertex Array Object (VAO)
		vao = glGenVertexArrays();
		// Binding ensures that subsequent state information is captured
		glBindVertexArray(vao);
		
		addVertexAttrib(vertexPositions, ShaderProgram.POSITION_ATTRIB_LOCATION, 3);
		addVertexAttrib(vertexNormals, ShaderProgram.NORMAL_ATTRIB_LOCATION, 3);
		addVertexAttrib(vertexColours, ShaderProgram.COLOUR_ATTRIB_LOCATION, 3);
		setIndices(indices);
		unbindVAO();
		
	}
	
	// Size = number of values per vertex in the vertexData array
	public void addVertexAttrib(float[] vertexData, int index, int size) {
		
		// Generate a new Vertex Buffer Object (VBO)
		int vbo = glGenBuffers();
		vbos.add(vbo); // Track for cleanup
		
		// Bind the buffer so data can be transferred to it, and
		// the vertex format can be specified
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		// 
		FloatBuffer vertexDataBuffer = BufferUtils.toBuffer(vertexData);
		glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
		
		putDataInVBO(vertexData);
		setUpAttribArray(index, size);
		unbindVBO();
	}
	
	private void genAndBindVBO() {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
	}
	
	private static void putDataInVBO(float[] vertexData) {

	}
	
	private static void setUpAttribArray(int index, int size) {
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(index);
	}
	
	private static void unbindVBO() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	
	
	
	public void bind() {
		glBindVertexArray(vao);
	}
	
	
	public static void unbindVAO() {
		glBindVertexArray(0);
	}

}
