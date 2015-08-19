package math;

public class Vector3f {
	
	// TODO: Have a base vector class / interface that is independent of dimension
	// TODO: Have a bunch of static factory methods for the basis vectors

	public float x, y, z;

	// Empty constructor for default (0, 0, 0)
	public Vector3f() {
		
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
	
	// Make each component negative
	public void negate() {
		x = -x;
		y = -y;
		z = -z;
	}
	
	// Pythagorean calculation for the magnitude of a vector
	public double magnitude() {
		return Math.sqrt(x*x + y*y + z*z);
	}

}
