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
		float[] elements = matrix.elements;
		float xnew = x * elements[0 + 0 * 4] + y * elements[1 + 0 * 4] + z * elements[2 + 0 * 4] + w * elements[3 + 0 * 4];
		float ynew = x * elements[0 + 1 * 4] + y * elements[1 + 1 * 4] + z * elements[2 + 1 * 4] + w * elements[3 + 1 * 4];
		float znew = x * elements[0 + 2 * 4] + y * elements[1 + 2 * 4] + z * elements[2 + 2 * 4] + w * elements[3 + 2 * 4];
		float wnew = x * elements[0 + 3 * 4] + y * elements[1 + 3 * 4] + z * elements[2 + 3 * 4] + w * elements[3 + 3 * 4];
		set(xnew, ynew, znew, wnew);
	}
	
}
