package math;

public class Vector3f {
	
	// TODO: Have a base vector class / interface that is independent of dimension

	public float x;
	public float y;
	public float z;

	public Vector3f() {
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void translate(Vector3f vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
	}
	
	public double magnitude() {
		return Math.sqrt(x*x + y*y + z*z);
	}

}
