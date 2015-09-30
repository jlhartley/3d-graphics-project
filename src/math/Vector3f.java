package math;

public class Vector3f {
	
	// TODO: Have a base vector class / interface that is independent of dimension
	// TODO: Have a bunch of static factory methods for the basis vectors
	
	// TODO: Review performance of the setMagnitude method
	
	
	// TODO: Make a VectorUtils class
	
	public static Vector3f add(Vector3f vec1, Vector3f vec2) {
		Vector3f vec = new Vector3f();
		vec.x = vec1.x + vec2.x;
		vec.y = vec1.y + vec2.y;
		vec.z = vec1.z + vec2.z;
		return vec;
	}
	
	public static Vector3f sub(Vector3f vec1, Vector3f vec2) {
		Vector3f vec = new Vector3f();
		vec.x = vec1.x - vec2.x;
		vec.y = vec1.y - vec2.y;
		vec.z = vec1.z - vec2.z;
		return vec;
	}
	
	

	
	
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
	
	// Translate by a vector by adding the components
	public void translate(Vector3f vec) {
		add(vec);
	}
	
	// Add a vector to this vector
	public void add(Vector3f vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
	}
	
	// Subtract a vector from this vector
	public void sub(Vector3f vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
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
	
	// For efficiency - sometimes this is required
	public float magnitudeSquared() {
		return x*x + y*y + z*z;
	}
	
	// Pythagorean calculation for the magnitude of a vector
	public float magnitude() {
		return (float) Math.sqrt(magnitudeSquared());
	}
	
	// Normalise the vector - divide each component by the magnitude
	public void normalise() {
		float mag = magnitude();
		set(x / mag, y / mag, z / mag);
		// Alternative
		//scale(1 / mag);
	}
	
	public void setMagnitude(float mag) {
		normalise();
		scale(mag);
	}

	// Get the dot product of this vector and another vector
	public float dot(Vector3f vec) {
		return this.x * vec.x + this.y * vec.y + this.z * vec.z;
	}
	
	
	// A static method for getting the cross product of two vectors
	public static Vector3f cross(Vector3f vec1, Vector3f vec2) {
		float x = vec1.y * vec2.z - vec1.z * vec2.y;
		float y = vec1.z * vec2.x - vec1.x * vec2.z;
		float z = vec1.x * vec2.y - vec1.y * vec2.x;
		return new Vector3f(x, y, z);
	}
	
	
	@Override
	public String toString() {
		return "{x: " + x + ", y: " + y + ", z: " + z + "}"; 
	}
	
}
