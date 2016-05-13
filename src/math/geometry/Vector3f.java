package math.geometry;

public class Vector3f {
	
	
	// Note that the mutator methods use a fluid interface
	
	public static final int FLOATS = 3;
	
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
	
	// A static method for getting the cross product of two vectors
	public static Vector3f cross(Vector3f vec1, Vector3f vec2) {
		float x = vec1.y * vec2.z - vec1.z * vec2.y;
		float y = vec1.z * vec2.x - vec1.x * vec2.z;
		float z = vec1.x * vec2.y - vec1.y * vec2.x;
		return new Vector3f(x, y, z);
	}

	
	
	public float x, y, z;

	// Empty constructor for default zero vector (0, 0, 0)
	public Vector3f() {
		
	}
	
	// Constructor for a copy of an existing vector
	public Vector3f(Vector3f vec) {
		set(vec);
	}

	// Constructor with components
	public Vector3f(float x, float y, float z) {
		set(x, y, z);
	}
	
	// Set vector components
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	// Set this vector to another vector
	public Vector3f set(Vector3f vec) {
		set(vec.x, vec.y, vec.z);
		return this;
	}
	
	// Set every component to the same value
	public Vector3f set(float f) {
		return set(f, f, f);
	}
	
	
	// Translate by a vector by adding the components
	public Vector3f translate(Vector3f vec) {
		return add(vec);
	}
	
	// Add a vector to this vector
	public Vector3f add(Vector3f vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		return this;
	}
	
	// Subtract a vector from this vector
	public Vector3f sub(Vector3f vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
		return this;
	}
	
	// Multiply by a scalar
	public Vector3f scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		return this;
	}
	
	// Make each component negative
	public Vector3f negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	// Set every component to 0
	public Vector3f zero() {
		return set(0);
	}
	
	// For efficiency - sometimes this is required
	public float magnitudeSquared() {
		return x*x + y*y + z*z;
	}
	
	// Pythagorean calculation for the magnitude of a vector
	public float magnitude() {
		return (float) Math.sqrt(magnitudeSquared());
	}
	
	// Normalise the vector - make its magnitude 1
	public Vector3f normalise() {
		float mag = magnitude();
		set(x / mag, y / mag, z / mag);
		// Alternative
		//scale(1 / mag);
		return this;
	}
	
	public Vector3f setMagnitude(float newMag) {
		float oldMag = magnitude();
		scale(newMag / oldMag);
		// Alternative
		//normalise();
		//scale(newMag);
		return this;
	}

	// Get the dot product of this vector and another vector
	public float dot(Vector3f vec) {
		return this.x * vec.x + this.y * vec.y + this.z * vec.z;
	}
	
	// Perform matrix multiplication on this vector
	public Vector3f multiply(Matrix4f matrix, float w) {
		float[][] elements = matrix.elements;
		float xnew = x * elements[0][0] + y * elements[1][0] + z * elements[2][0] + w * elements[3][0];
		float ynew = x * elements[0][1] + y * elements[1][1] + z * elements[2][1] + w * elements[3][1];
		float znew = x * elements[0][2] + y * elements[1][2] + z * elements[2][2] + w * elements[3][2];
		// Discard w component
		set(xnew, ynew, znew);
		return this;
	}
	
	
	// Returns a copy using the constructor
	public Vector3f getCopy() {
		return new Vector3f(this);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3f other = (Vector3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}
	

	@Override
	public String toString() {
		return "Vector3f [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
}
