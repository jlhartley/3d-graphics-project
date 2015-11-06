package math;

public class Matrix4f {
	
	// Factory methods
	
	public static Matrix4f identity() {
		return new Matrix4f();
	}
	
	public static Matrix4f fromTranslation(Vector3f vec) {
		Matrix4f matrix = identity();
		matrix.elements[0 + 3 * 4] = vec.x;
		matrix.elements[1 + 3 * 4] = vec.y;
		matrix.elements[2 + 3 * 4] = vec.z;
		return matrix;
	}
	
	
	
	public static final int ROW_COUNT = 4;
	public static final int COLUMN_COUNT = 4;
	
	public static final int SIZE = ROW_COUNT * COLUMN_COUNT;
	
	
	public float[] elements;

	public Matrix4f() {
		// By default a matrix is created with the identity matrix set
		setToIdentity();
	}
	
	
	public void setToIdentity() {
		
		// Completely blank to zeros
		elements = new float[SIZE];
		
		// The Identity Matrix:
		// [ 1 , 0 , 0 , 0 ]
		// [ 0 , 1 , 0 , 0 ]
		// [ 0 , 0 , 1 , 0 ]
		// [ 0 , 0 , 0 , 1 ]
		
		// Using a flattened array
		// [column 0 + row 0 * 4] = 1.0f etc...
		
		elements[0 + 0 * 4] = 1.0f;
		elements[1 + 1 * 4] = 1.0f;
		elements[2 + 2 * 4] = 1.0f;
		elements[3 + 3 * 4] = 1.0f;
	}
	
	public void translate(Vector3f vec) {
		elements[3 + 0 * 4] += elements[0 + 0 * 4] * vec.x + elements[1 + 0 * 4] * vec.y + elements[2 + 0 * 4] * vec.z;
		elements[3 + 1 * 4] += elements[0 + 1 * 4] * vec.x + elements[1 + 1 * 4] * vec.y + elements[2 + 1 * 4] * vec.z;
		elements[3 + 2 * 4] += elements[0 + 2 * 4] * vec.x + elements[1 + 2 * 4] * vec.y + elements[2 + 2 * 4] * vec.z;
		elements[3 + 3 * 4] += elements[0 + 3 * 4] * vec.x + elements[1 + 3 * 4] * vec.y + elements[2 + 3 * 4] * vec.z;
	}
	
	// Axis must be a unit vector
	public void rotate(float angle, Vector3f axis) {
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = axis.x*axis.y;
		float yz = axis.y*axis.z;
		float xz = axis.x*axis.z;
		float xs = axis.x*s;
		float ys = axis.y*s;
		float zs = axis.z*s;

		float f00 = axis.x*axis.x*oneminusc+c;
		float f01 = xy*oneminusc+zs;
		float f02 = xz*oneminusc-ys;
		
		float f10 = xy*oneminusc-zs;
		float f11 = axis.y*axis.y*oneminusc+c;
		float f12 = yz*oneminusc+xs;
		
		float f20 = xz*oneminusc+ys;
		float f21 = yz*oneminusc-xs;
		float f22 = axis.z*axis.z*oneminusc+c;

		float t00 = elements[0 + 0 * 4] * f00 + elements[1 + 0 * 4] * f01 + elements[2 + 0 * 4] * f02;
		float t01 = elements[0 + 1 * 4] * f00 + elements[1 + 1 * 4] * f01 + elements[2 + 1 * 4] * f02;
		float t02 = elements[0 + 2 * 4] * f00 + elements[1 + 2 * 4] * f01 + elements[2 + 2 * 4] * f02;
		float t03 = elements[0 + 3 * 4] * f00 + elements[1 + 3 * 4] * f01 + elements[2 + 3 * 4] * f02;
		float t10 = elements[0 + 0 * 4] * f10 + elements[1 + 0 * 4] * f11 + elements[2 + 0 * 4] * f12;
		float t11 = elements[0 + 1 * 4] * f10 + elements[1 + 1 * 4] * f11 + elements[2 + 1 * 4] * f12;
		float t12 = elements[0 + 2 * 4] * f10 + elements[1 + 2 * 4] * f11 + elements[2 + 2 * 4] * f12;
		float t13 = elements[0 + 3 * 4] * f10 + elements[1 + 3 * 4] * f11 + elements[2 + 3 * 4] * f12;
		
		elements[2 + 0 * 4] = elements[0 + 0 * 4] * f20 + elements[1 + 0 * 4] * f21 + elements[2 + 0 * 4] * f22;
		elements[2 + 1 * 4] = elements[0 + 1 * 4] * f20 + elements[1 + 1 * 4] * f21 + elements[2 + 1 * 4] * f22;
		elements[2 + 2 * 4] = elements[0 + 2 * 4] * f20 + elements[1 + 2 * 4] * f21 + elements[2 + 2 * 4] * f22;
		elements[2 + 3 * 4] = elements[0 + 3 * 4] * f20 + elements[1 + 3 * 4] * f21 + elements[2 + 3 * 4] * f22;
		
		elements[0 + 0 * 4] = t00;
		elements[0 + 1 * 4] = t01;
		elements[0 + 2 * 4] = t02;
		elements[0 + 3 * 4] = t03;
		elements[1 + 0 * 4] = t10;
		elements[1 + 1 * 4] = t11;
		elements[1 + 2 * 4] = t12;
		elements[1 + 3 * 4] = t13;
	}
	
	public void scale(Vector3f vec) {
		elements[0 + 0 * 4] = elements[0 + 0 * 4] * vec.x;
		elements[0 + 1 * 4] = elements[0 + 1 * 4] * vec.x;
		elements[0 + 2 * 4] = elements[0 + 2 * 4] * vec.x;
		elements[0 + 3 * 4] = elements[0 + 3 * 4] * vec.x;
		elements[1 + 0 * 4] = elements[1 + 0 * 4] * vec.y;
		elements[1 + 1 * 4] = elements[1 + 1 * 4] * vec.y;
		elements[1 + 2 * 4] = elements[1 + 2 * 4] * vec.y;
		elements[1 + 3 * 4] = elements[1 + 3 * 4] * vec.y;
		elements[2 + 0 * 4] = elements[2 + 0 * 4] * vec.z;
		elements[2 + 1 * 4] = elements[2 + 1 * 4] * vec.z;
		elements[2 + 2 * 4] = elements[2 + 2 * 4] * vec.z;
		elements[2 + 3 * 4] = elements[2 + 3 * 4] * vec.z;
	}
	
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for (int row = 0; row < ROW_COUNT; row++) {
			sb.append("[ ");
			for (int column = 0; column < COLUMN_COUNT; column++) {
				float element = elements[column + row * 4];
				sb.append(element + " ");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}
	
	
	

}
