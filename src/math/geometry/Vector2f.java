package math.geometry;

public class Vector2f {
	
	public static Vector2f add(Vector2f vec1, Vector2f vec2) {
		Vector2f vec = new Vector2f();
		vec.x = vec1.x + vec2.x;
		vec.y = vec1.y + vec2.y;
		return vec;
	}
	
	public static Vector2f sub(Vector2f vec1, Vector2f vec2) {
		Vector2f vec = new Vector2f();
		vec.x = vec1.x - vec2.x;
		vec.y = vec1.y - vec2.y;
		return vec;
	}
	
	public float x, y;
	
	// Empty constructor for default zero vector (0, 0)
	public Vector2f() {
		
	}
	
	// Constructor for a copy of an existing vector
	public Vector2f(Vector2f vec) {
		set(vec.x, vec.y);
	}

	// Constructor with components
	public Vector2f(float x, float y) {
		set(x, y);
	}
	
	// Set vector components
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	
	// Translate by a vector by adding the components
	public Vector2f translate(Vector2f vec) {
		return add(vec);
	}
	
	// Add a vector to this vector
	public Vector2f add(Vector2f vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}
	
	// Subtract a vector from this vector
	public Vector2f sub(Vector2f vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		return this;
	}
	
	// Multiply by a scalar
	public Vector2f scale(float scale) {
		x *= scale;
		y *= scale;
		return this;
	}
	
	// Make each component negative
	public Vector2f negate() {
		x = -x;
		y = -y;
		return this;
	}
	
	// Returns a copy using the constructor
	public Vector2f getCopy() {
		return new Vector2f(this);
	}
	
	
	@Override
	public String toString() {
		return "Vector2f [x=" + x + ", y=" + y + "]";
	}
	

}
