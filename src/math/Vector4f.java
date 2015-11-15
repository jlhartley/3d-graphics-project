package math;

public class Vector4f {
	
	public float x, y, z, w;
	
	// Empty constructor for default (0, 0, 0)
	public Vector4f() {

	}
	
	// Constructor for a copy of an existing vector
	public Vector4f(Vector4f vec) {
		set(vec.x, vec.y, vec.z, vec.w);
	}
	
	// Constructor with components
	public Vector4f(float x, float y, float z, float w) {
		set(x, y, z, w);
	}
	
	// Constructor taking in a Vector3f and the additional "w" component
	public Vector4f(Vector3f vec, float w) {
		set(vec.x, vec.y, vec.z, w);
	}
	
	// Set vector components
	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	// Perform matrix multiplication on this vector
	public void multiply(Matrix4f matrix) {
		float[][] elements = matrix.elements;
		float xnew = x * elements[0][0] + y * elements[1][0] + z * elements[2][0] + w * elements[3][0];
		float ynew = x * elements[0][1] + y * elements[1][1] + z * elements[2][1] + w * elements[3][1];
		float znew = x * elements[0][2] + y * elements[1][2] + z * elements[2][2] + w * elements[3][2];
		float wnew = x * elements[0][3] + y * elements[1][3] + z * elements[2][3] + w * elements[3][3];
		set(xnew, ynew, znew, wnew);
	}
	
	@Override
	public String toString() {
		return "[x: " + x + ", y: " + y + ", z: " + z + ", w: " + w + "]"; 
	}
	
}
