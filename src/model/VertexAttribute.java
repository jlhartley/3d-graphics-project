package model;

public class VertexAttribute {
	
	public String name;
	public int index; // GLSL index TODO: Have the index auto increment with object creation, starting at 0;
	public int size; // e.g 3 for vec3
	
	float[] data;
	
	public VertexAttribute(float[] data, int index, String name, int size) {
		this.data = data;
		this.index = index;
		this.name = name;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}
	
	public int getSize() {
		return size;
	}
	

}
