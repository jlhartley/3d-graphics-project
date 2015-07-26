package model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import util.BufferUtils;

public class Model {
	
	private int vaoId; // Only one of these per entity of course, that is the whole point of the class.
	private ArrayList<Integer> vbos = new ArrayList<>(); // Multiple vbos, one for vertex positions, one for vertex colours, etc.
	private int iboId; // Only ever going to be one of these per entity, and it will be bound permanently as the GL_ELEMENT_ARRAY_BUFFER.
	
	private int vertexCount = 3; // Default of 3 for testing with a triangle. Value is set when the ibo is set. Needed for drawing.

	// Basic getter
	public int getVertexCount() {
		return vertexCount;
	}
	
	
	
	public Model() {
		
		// First thing to do is gen and bind the VAO - the 'main object' of this class
		genAndBindVAO();
		
		// Since there will only be one of them the next thing to do is to gen and bind the IBO
		genAndBindIBO();
		
	}
	
	
	// VAO-handling code
	// Public methods:
	public void bindVAO() {
		glBindVertexArray(vaoId);
	}
	
	public void unbindVAO() {
		glBindVertexArray(0);
	}
	
	// Private methods:
	private void genAndBindVAO() {
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId); // Before any more state change takes place the VAO must be bound to "capture / record" the state change.
	}
	
	
	
	// IBO-handling code
	// Public methods:
	public void setIBOData(int[] iboData) {
		vertexCount = iboData.length;
		putDataInIBO(iboData);
	}
	
	// Private methods:
	private void genAndBindIBO() {
		iboId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
	}
	
	private void putDataInIBO(int[] indices) {
		IntBuffer indicesBuffer = BufferUtils.toBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
	}
	
	private void unbindIBO() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	
	
	// VBO-handling code
	
	// Size = the number of values per vertex in the vertexData array
	public void addVertexAttrib(float[] vertexData, int index, int size) {
		genAndBindNewVBO();
		putDataInVBO(vertexData);
		setUpAttribArray(index, size);
		unbindVBO();
	}
	
	private void genAndBindNewVBO() {
		int vbo = glGenBuffers();
		vbos.add(vbo); // Keep track of vbo for cleanup at end
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
	}
	
	private void putDataInVBO(float[] vertexData) {
		FloatBuffer vertexBuffer = BufferUtils.toBuffer(vertexData);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
	}
	
	private void setUpAttribArray(int index, int size) {
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(index); // TODO : Should it be enabled before we are even rendering?
	}
	
	private void unbindVBO() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	

	// Clean up code
	public void cleanUp() {
		unbindVBO(); // There shouldn't be one bound as it should have been handled earlier when the vertex attribute is added, but just to make sure...
		unbindIBO();
		unbindVAO();
		
		
		for (int vboId : vbos) {
			glDeleteBuffers(vboId);
		}
		
		glDeleteBuffers(iboId);
		glDeleteVertexArrays(vaoId);
	}
	

}
