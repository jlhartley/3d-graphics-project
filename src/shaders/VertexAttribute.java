package shaders;

public class VertexAttribute {
	
	private int location;
	private String name;
	private int size;
	private int type;
	
	
	public VertexAttribute(int location, String name, int size, int type) {
		this.location = location;
		this.name = name;
		this.size = size;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public int getLocation() {
		return location;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getType() {
		return type;
	}
	
	
	
	@Override
	public String toString() {
		return "VertexAttribute [location=" + location + ", name=" + name + ", size=" + size + ", type=" + type + "]";
	}
}
