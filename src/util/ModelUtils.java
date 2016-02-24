package util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class ModelUtils {
	
	private static ArrayList<Integer> vbos = new ArrayList<>();
	private static ArrayList<Integer> ibos = new ArrayList<>();
	
	
	
	

	
	
	

	
	

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
