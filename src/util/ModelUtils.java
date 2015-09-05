package util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class ModelUtils {
	
	// Track all VAOs, VBOs and IBOs for cleanup purposes
	private static ArrayList<Integer> vaos = new ArrayList<>();
	private static ArrayList<Integer> vbos = new ArrayList<>();
	private static ArrayList<Integer> ibos = new ArrayList<>();
	
	// VAO code
	public static int genAndBindVAO() {
		int vaoId = glGenVertexArrays();
		vaos.add(vaoId);
		glBindVertexArray(vaoId);
		return vaoId;
	}
	
	public static void unbindVAO() {
		glBindVertexArray(0);
	}
	
	
	
	// IBO code
	public static void setIndices(int[] indices) {
		genAndBindIBO();
		putDataInIBO(indices);
	}
	
	private static int genAndBindIBO() {
		int iboId = glGenBuffers();
		ibos.add(iboId);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
		return iboId;
	}
	
	private static void putDataInIBO(int[] indices) {
		IntBuffer indicesBuffer = BufferUtils.toBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
	}
	
	private static void unbindIBO() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	
	
	// VBO code
	// Size = number of values per vertex in the vertexData array
	public static void addVertexAttrib(float[] vertexData, int index, int size) {
		genAndBindVBO();
		putDataInVBO(vertexData);
		setUpAttribArray(index, size);
		unbindVBO();
	}
	
	private static void genAndBindVBO() {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
	}
	
	private static void putDataInVBO(float[] vertexData) {
		FloatBuffer vertexBuffer = BufferUtils.toBuffer(vertexData);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
	}
	
	private static void setUpAttribArray(int index, int size) {
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(index);
	}
	
	private static void unbindVBO() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	

	// Clean up code
	public static void cleanUp() {
		
		unbindVBO();
		unbindIBO();
		unbindVAO();
		
		for (int vboId : vbos) {
			glDeleteBuffers(vboId);
		}
		
		for (int iboId : ibos) {
			glDeleteBuffers(iboId);
		}
		
		for (int vaoId : vaos) {
			glDeleteVertexArrays(vaoId);
		}
		
	}
	

}
