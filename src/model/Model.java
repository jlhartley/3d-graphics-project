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

import shaders.ShaderProgram;

public class Model {
	
	// The Vertex Array Object (VAO) is the OpenGL object
	// that this class wraps.
	private int vao;
	
	// Track the Index Buffer Object (IBO) for cleanup purposes
	private int ibo;
	
	// Keep track of all the generated Vertex Buffer Objects (VBOs),
	// again for cleanup purposes
	private List<Integer> vbos = new ArrayList<>();
	
	// The total vertex count used when rendering,
	// equal to indices.length
	//private int vertexCount;

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
	
	// Size = number of values per vertex in the vertexData array
	public void addVertexAttrib(float[] vertexData, int index, int size) {
		
		// Generate a new Vertex Buffer Object (VBO)
		int vbo = glGenBuffers();
		// Track for cleanup
		vbos.add(vbo);
		
		// Bind the buffer so data can be transferred to it, and
		// the vertex format can be specified.
		
		// The binding itself is not part of the VAO state, however the later
		// call to glVertexAttribPointer is and this specifies the source
		// for the vertex data as the currently bound GL_ARRAY_BUFFER, and specifies
		// the format of the data.
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		// Convert the float array to a Java NIO buffer so it can be passed to the OpenGL call.
		FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(vertexData.length).put(vertexData);
		// Transfer the data to the VBO, which resides in GPU memory
		glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
		
		// Describe the vertex format for the attribute array
		
		// The index is the index of the attribute that we have defined,
		// e.g. vertex position, normal, colour etc. which must match up with the attribute
		// in the shader. Effectively, it is a numerical identifier for an 
		// attribute. The call associates the currently bound GL_ARRAY_BUFFER to 
		// with the index.
		
		// Size specifies the number of values per vertex in the VBO
		
		// The next parameter is the data type, which is typically GL_FLOAT
		
		// The third parameter specifies whether fixed-point values of 0-255
		// should be normalised to the range 0-1. This is typically false, however one
		// common use case is for colours.
		
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(index);
		
		// Unbind the VBO. This does not change any VAO state,
		// or remove any association. It is just good practice since
		// nothing more needs to be done with it.
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void setIndices(int[] indices) {
		
		// Generate a new Index Buffer Object (IBO)
		// This is just a VBO
		ibo = glGenBuffers();
		
		// Bind the buffer to the target GL_ELEMENT_ARRAY_BUFFER
		// Note that this binding is part of the VAO state.
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		
		// Convert the integer array to a Java NIO buffer so it can be passed to the OpenGL call
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length).put(indices);
		// Transfer the data
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		
	}
	

	
	
	
	
	public void bind() {
		glBindVertexArray(vao);
	}
	
	
	public static void unbindVAO() {
		glBindVertexArray(0);
	}

}
