package math;

public class Vector3f {
	
	// TODO: Have a base vector class / interface that is independent of dimension
	// TODO: Have a bunch of static factory methods for the basis vectors

	public float x, y, z;

	// Empty constructor for default (0, 0, 0)
	public Vector3f() {
		
	}
	
	// Constructor for a copy of an existing vector
	public Vector3f(Vector3f vec) {
		set(vec.x, vec.y, vec.z);
	}

	// Constructor with components
	public Vector3f(float x, float y, float z) {
		set(x, y, z);
	}
	
	// Set vector components
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	// Translate by another vector by adding the components
	public void translate(Vector3f vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
	}
	
	// Multiply by a scalar
	public void scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
	}
	
	// Make each component negative
	public void negate() {
		x = -x;
		y = -y;
		z = -z;
	}
	
	// Pythagorean calculation for the magnitude of a vector
	public float magnitude() {
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	
	// Normalise the vector - divide each component by the magnitude
	public void normalise() {
		float mag = magnitude();
		set(x / mag, y / mag, z / mag);
		// Alternative
		//scale(1 / mag);
	}

	// Get the dot product of this vector and another vector
	public float dot(Vector3f vec) {
		return this.x * vec.x + this.y * vec.y + this.z * vec.z;
	}
	
	// Perform matrix multiplication on this vector
	public void multiply(Matrix4f matrix) {
		float[] elements = matrix.elements;
		float w = 1; // The 4th vector component
		x = x * elements[0 + 0 * 4] + y * elements[1 + 0 * 4] + z * elements[2 + 0 * 4] + w * elements[3 + 0 * 4];
		y = x * elements[0 + 1 * 4] + y * elements[1 + 1 * 4] + z * elements[2 + 1 * 4] + w * elements[3 + 1 * 4];
		z = x * elements[0 + 2 * 4] + y * elements[1 + 2 * 4] + z * elements[2 + 2 * 4] + w * elements[3 + 2 * 4];
	}
	
	
	@Override
	public String toString() {
		return "{x: " + x + ", y: " + y + ", z: " + z + "}"; 
	}
	
}
