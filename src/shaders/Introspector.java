package shaders;

import static org.lwjgl.opengl.GL20.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;

public class Introspector {
	
	private int program;
	
	private Map<String, Integer> uniformLocationCache = new HashMap<>();
	private Map<String, Integer> attributeLocationCache = new HashMap<>();
	
	public Introspector(int program) {
		this.program = program;
		
		// Concept of mapping between Java side classes
		// and shader data types
		
		//Map<Class<?>, Integer> classDataTypeMap = new HashMap<>();
		
		//classDataTypeMap.put(Vector3f.class, GL_FLOAT_VEC3);
		//classDataTypeMap.put(Matrix4f.class, GL_FLOAT_MAT4);
		
	}
	
	public int getUniformLocation(String name) {
		if (uniformLocationCache.containsKey(name)) {
			return uniformLocationCache.get(name);
		}
		int location = glGetUniformLocation(program, name);
		if (location == -1) {
			System.err.println("Could not get location for uniform variable \"" + name + "\"");
		} else {
			uniformLocationCache.put(name, location);
		}
		return location;
	}
	
	public int getAttributeLocation(String name) {
		if (attributeLocationCache.containsKey(name)) {
			return attributeLocationCache.get(name);
		}
		int location = glGetAttribLocation(program, name);
		if (location == -1) {
			System.err.println("Could not get location for vertex attribute \"" + name + "\"");
		} else {
			attributeLocationCache.put(name, location);
		}
		return location;
	}
	
	public List<VertexAttribute> listVertexAttributes() {
		
		List<VertexAttribute> vertexAttributes = new ArrayList<>();
		
		/*if (GL.getCapabilities().OpenGL43) {
			
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
			
		}*/
			
		int attributeCount = glGetProgrami(program, GL_ACTIVE_ATTRIBUTES);
		// Note that index is different to location!
		// This is an obscure and confusing part of OpenGL
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

			// Since we query the values, put them in the cache
			attributeLocationCache.put(name, location);
		}

		return vertexAttributes;
	}
	
}
