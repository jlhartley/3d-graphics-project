package util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import math.geometry.Vector3f;
import model.vertices.Vertex;
import shaders.ShaderProgram;

public class ModelUtils {
	
	public static enum VBOFormat {
		INTERLEAVED,
		SEPARATE
	}
	

	private static List<Integer> vbos = new ArrayList<>();
	
	public static void setVertexAttributes(List<Vertex> vertices) {
		
		// 3 values for each position, 3 for each normal, 3 for each colour
		int floatsPerVertex = Vector3f.FLOATS + Vector3f.FLOATS;
		
		int totalFloats = floatsPerVertex * vertices.size();;
		
		
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(totalFloats);
		
		for (Vertex vertex : vertices) {
			
			Vector3f position = vertex.position;
			Vector3f normal = vertex.normal;
			
			
			verticesBuffer.put(position.x).put(position.y).put(position.z);
			verticesBuffer.put(normal.x).put(normal.y).put(normal.z);
			//verticesBuffer.put(colour.x).put(colour.y).put(colour.z);
		}
		
		verticesBuffer.flip();
		
		
		int vbo = glGenBuffers();
		vbos.add(vbo);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		
		
		
		// 1 float = 32 bits = 4 bytes
		// The stride is the number of bytes a vertex takes up in total
		int stride = floatsPerVertex * Float.BYTES;
		
		int index = ShaderProgram.POSITION_ATTRIB_LOCATION;
		glVertexAttribPointer(index, 3, GL_FLOAT, false, stride, 0);
		glEnableVertexAttribArray(index);
		
		// Offset is 3 floats (12 bytes) after the position data
		index = ShaderProgram.NORMAL_ATTRIB_LOCATION;
		glVertexAttribPointer(index, 3, GL_FLOAT, false, stride, 3 * Float.BYTES);
		glEnableVertexAttribArray(index);
		
		/*
		// Offset is 6 floats (24 bytes) after the position + colour data
		index = ShaderProgram.COLOUR_ATTRIB_LOCATION;
		glVertexAttribPointer(index, 3, GL_FLOAT, false, stride, 6 * Float.BYTES);
		glEnableVertexAttribArray(index);
		*/
		
		// Unbind for consistency
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
	}

	
	
	

	
	

	// Clean up code
	public static void cleanUp() {
		
		// Unbind any bound VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		for (int vboId : vbos) {
			glDeleteBuffers(vboId);
		}
		
		
	}
	

}
