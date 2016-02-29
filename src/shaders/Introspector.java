package shaders;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL43.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

public class Introspector {
	
	private int program;
	
	private Map<String, Integer> uniformLocations = new HashMap<>();
	private Map<String, Integer> attributeLocations = new HashMap<>();
	
	public Introspector(int program) {
		this.program = program;
		
		//Map<Class<?>, Integer> classDataTypeMap = new HashMap<>();
		
		//classDataTypeMap.put(Vector3f.class, GL_FLOAT_VEC3);
		//classDataTypeMap.put(Matrix4f.class, GL_FLOAT_MAT4);
		
	}
	
	public int getUniformLocation(String name) {
		if (uniformLocations.containsKey(name)) {
			return uniformLocations.get(name);
		}
		//if (GL.getCapabilities().OpenGL43) {
			
		//}
		int location = glGetUniformLocation(program, name);
		if (location == -1) {
			System.err.println("Could not get location for uniform variable \"" + name + "\"");
		} else {
			uniformLocations.put(name, location);
		}
		return location;
	}
	
	public int getAttributeLocation(String name) {
		if (attributeLocations.containsKey(name)) {
			return attributeLocations.get(name);
		}
		int location = glGetAttribLocation(program, name);
		if (location == -1) {
			System.err.println("Could not get location for vertex attribute \"" + name + "\"");
		} else {
			attributeLocations.put(name, location);
		}
		return location;
	}
	
	public List<VertexAttribute> listVertexAttributes() {
		
		List<VertexAttribute> vertexAttributes = new ArrayList<>();
		
		if (GL.getCapabilities().OpenGL43) {
			
			int attributeCount = glGetProgramInterfacei(program, GL_PROGRAM_INPUT, GL_ACTIVE_RESOURCES);
			for (int index = 0; index < attributeCount; index++) {
				
				String name;
				
				int location;
				int size;
				int type;
				
				int[] query = {GL_LOCATION, GL_TYPE};
				IntBuffer queryBuffer = BufferUtils.createIntBuffer(query.length);
				queryBuffer.put(GL_LOCATION).put(GL_TYPE);
				queryBuffer.flip();
				
				IntBuffer response = glGetProgramResourceiv(program, GL_PROGRAM_INPUT, index, queryBuffer, query.length);
				location = response.get(0);
				type = response.get(1);
				
				name = glGetProgramResourceName(program, GL_PROGRAM_INPUT, index);
				
				System.out.println("Name: " + name);
				System.out.println("Location: " + location);
				
				//VertexAttribute attribute = new VertexAttribute(index, name, size)
			}
			
		}
			
		int attributeCount = glGetProgrami(program, GL_ACTIVE_ATTRIBUTES);
		// Note that index is definitely different to location!
		for (int index = 0; index < attributeCount; index++) {

			IntBuffer sizeBuffer = BufferUtils.createIntBuffer(1);
			IntBuffer typeBuffer = BufferUtils.createIntBuffer(1);

			String name;

			int location;
			int size;
			int type;

			name = glGetActiveAttrib(program, index, sizeBuffer, typeBuffer);
			location = glGetAttribLocation(program, name);
			size = sizeBuffer.get();
			type = typeBuffer.get();

			VertexAttribute attribute = new VertexAttribute(location, name, size, type);
			vertexAttributes.add(attribute);
		}
			
		
		return vertexAttributes;
	}
	
}
