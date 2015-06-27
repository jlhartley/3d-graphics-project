package shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import math.Matrix4f;
import math.Vector3f;
import util.FileUtils;


public class ShaderProgram {
	
	// Constants used when binding named attributes to an index. Also used when setting up the VAO.
	public static final int POSITION_ATTRIB_LOCATION = 0;
	public static final int COLOUR_ATTRIB_LOCATION = 1;
	
	
	private int vertexShaderId;
	private int fragmentShaderId;
	private int programId;
	
	private HashMap<String, Integer> uniformLocationCache = new HashMap<>();
	
	public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
		vertexShaderId = loadShaderFromFile(vertexShaderPath, GL_VERTEX_SHADER);
		fragmentShaderId = loadShaderFromFile(fragmentShaderPath, GL_FRAGMENT_SHADER);
		
		programId = glCreateProgram();
		
		glAttachShader(programId, vertexShaderId);
		glAttachShader(programId, fragmentShaderId);
		
		bindAttributes();
		
		glLinkProgram(programId);
		glValidateProgram(programId);
	}
	
	private void bindAttributes() {
		glBindAttribLocation(programId, POSITION_ATTRIB_LOCATION, "position");
		glBindAttribLocation(programId, COLOUR_ATTRIB_LOCATION, "colour");
	}
	
	private static int loadShaderFromFile(String path, int type) {
		
		String shaderSource = FileUtils.getFileContents(path);
		
		int shaderId = glCreateShader(type);
		glShaderSource(shaderId, shaderSource);
		glCompileShader(shaderId);
		
		if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile shader!");
			System.err.println("Log: ");
			System.err.println(glGetShaderInfoLog(shaderId));
			System.exit(1);
		}
			
		return shaderId;
	}
	
	
	// Bunch of overloaded methods for accessing uniforms
	public void setUniformValue(String name, boolean value) {
		float floatValue = value ? 1 : 0; // No booleans in GLSL, so 1 for true 0 for false
		glUniform1f(getUniformLocation(name), floatValue);
	}
	
	public void setUniformValue(String name, float value) {
		glUniform1f(getUniformLocation(name), value);
	}
	
	public void setUniformValue(String name, Vector3f vector) {
		glUniform3f(getUniformLocation(name), vector.x, vector.y, vector.z);
	}
	
	
	FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(4*4); // Can't create a new buffer on each method call due to garbage collector issue.
	
	public void setUniformValue(String name, Matrix4f matrix) {
		matrix4fBuffer.put(matrix.elements);
		matrix4fBuffer.flip();
		glUniformMatrix4fv(getUniformLocation(name), false, matrix4fBuffer);
	}
	
	
	private int getUniformLocation(String name) {
		
		if (uniformLocationCache.containsKey(name)) {
			return uniformLocationCache.get(name);
		}
		
		int location = glGetUniformLocation(programId, name);
		
		if (location == -1) {
			System.err.println("Could not get location for uniform variable \"" + name + "\"!");
			// TODO: Maybe should call System.exit(1) here too?
		} else {
			uniformLocationCache.put(name, location);
		}
		
		return location;
	}
	
	
	// Maintenance methods
	public void useProgram() {
		glUseProgram(programId);
	}
	
	public void stopUsingProgram() {
		glUseProgram(0);
	}
	
	public void cleanUp() {
		stopUsingProgram();
		glDetachShader(programId, vertexShaderId);
		glDetachShader(programId, fragmentShaderId);
		glDeleteShader(vertexShaderId);
		glDeleteShader(fragmentShaderId);
		glDeleteProgram(programId);
	}

	
}
